package com.dnastack.wdl4j.lib;

import com.dnastack.wdl4j.lib.api.NamedElement;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.expression.Expression;
import com.dnastack.wdl4j.lib.typing.ArrayType;
import com.dnastack.wdl4j.lib.typing.OptionalType;
import com.dnastack.wdl4j.lib.typing.Type;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class Workflow implements WdlElement, NamedElement {

    @NonNull
    private String name;
    private Inputs inputs;
    private List<WdlElement> elements;
    private Outputs outputs;
    private Meta meta;
    private ParameterMeta parameterMeta;
    @NonNull
    private int id;

    private void typeCheckElementArray(Namespace namespace, List<? extends WdlElement> wdlElements) throws WdlValidationError {
        for (WdlElement element : wdlElements) {
            if (element instanceof Declaration) {
                typeCheckDeclaration(namespace, (Declaration) element);
            } else if (element instanceof Call) {
                typeCheckCall(namespace, (Call) element);
            } else if (element instanceof Conditional) {
                Namespace conditionalNamespace = typeCheckConditionalNamespace((Conditional) element, namespace);
                addChildElementsToOuterWrappingScope(namespace, conditionalNamespace, type -> {
                    if (type instanceof OptionalType) {
                        return type;
                    } else {
                        return OptionalType.getType(type);
                    }
                });
            } else if (element instanceof Scatter) {
                Scatter scatter = (Scatter) element;
                Namespace scatterNamespace = typeCheckScatterNamespace(scatter, namespace);
                addChildElementsToOuterWrappingScope(namespace, scatterNamespace, ArrayType::getType);
            }
        }
    }

    private void addChildElementsToOuterWrappingScope(Namespace wrappedNamespace, Namespace currentNamespace, Function<Type, Type> wrappingFunc) throws NamespaceException {

        Map<String, WdlElement> elements = currentNamespace.getElements();
        for (Map.Entry<String, WdlElement> entry : elements.entrySet()) {
            String key = entry.getKey();
            WdlElement value = entry.getValue();
            if (value instanceof Declaration && ((Declaration) value).isExposeToOuterScope()) {
                Declaration declaration = (Declaration) value;
                Type wrappedType = wrappingFunc.apply(declaration.getDeclType());
                wrappedNamespace.addElement(key,
                                            new Declaration(wrappedType,
                                                            declaration.getName(),
                                                            null,
                                                            declaration.getId()));
            } else if (value instanceof Call) {
                Call call = (Call) value;
                Map<String, Declaration> outputs = call.getEffectiveCallOutputs();
                if (outputs != null) {
                    Map<String, Declaration> wrappedCallOutputs = new HashMap<>();
                    outputs.forEach((outputKey, outputDecl) -> {
                        wrappedCallOutputs.put(outputKey,
                                               new Declaration(wrappingFunc.apply(outputDecl.getDeclType()),
                                                               outputDecl.getName(),
                                                               null,
                                                               outputDecl.getId()));
                    });
                    wrappedNamespace.addElement(key,
                                                new Call(call.getTaskOrWorkflowName(),
                                                         call.getCallAlias(),
                                                         call.getInputs(),
                                                         call.getEffectiveCallInputs(),
                                                         wrappedCallOutputs,
                                                         call.getId()));
                }
            }
        }

    }

    private void typeCheckDeclaration(Namespace namespace, Declaration declaration) throws WdlValidationError {
        namespace.addDeclaration(declaration.getName(), declaration);
        declaration.getDeclType().typecheck(namespace);
        if (declaration.getExpression() != null) {
            declaration.getExpression()
                       .typeCheck(declaration, namespace)
                       .assertIsCoercibleTo(namespace.getCoercionOptions(), declaration.getDeclType());
        }
    }

    private void typeCheckCall(Namespace namespace, Call call) throws WdlValidationError {
        Namespace childNamespace = namespace.getChildNamespace(call.getTaskOrWorkflowName());
        Map<String, Declaration> callDeclarations = childNamespace.filterElementsForThisNamespace(Declaration.class);
        Map<String, Declaration> callInputs = callDeclarations.entrySet()
                                                              .stream()
                                                              .filter(e -> e.getValue().isFromInputs())
                                                              .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                                                                  Declaration declaration = e.getValue();
                                                                  return new Declaration(declaration.getDeclType(),
                                                                                         declaration.getName(),
                                                                                         declaration.getExpression(),
                                                                                         call.getId());
                                                              }));

        Map<String, Declaration> callOutputs = callDeclarations.entrySet()
                                                               .stream()
                                                               .filter(e -> e.getValue().isFromOutputs())
                                                               .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                                                                   Declaration declaration = e.getValue();
                                                                   return new Declaration(declaration.getDeclType(),
                                                                                          declaration.getName(),
                                                                                          declaration.getExpression(),
                                                                                          call.getId());
                                                               }));

        if (call.getInputs() != null) {
            for (Map.Entry<String, Expression> entry : call.getInputs().entrySet()) {
                Declaration inputDecl = callInputs.get(entry.getKey());
                if (inputDecl == null) {
                    throw new WdlValidationError("Reference to input \"" + entry.getKey() + "\" for call \"" + call.getName() + "\" does not exist");
                }

                Type checkedType = entry.getValue().typeCheck(call, namespace);
                checkedType.assertIsCoercibleToSome(namespace.getCoercionOptions(), inputDecl.getDeclType());
            }
        }

        call.setEffectiveCallInputs(callInputs);
        call.setEffectiveCallOutputs(callOutputs);
        namespace.addElement(call.getName(), call);
    }

    private Namespace typeCheckConditionalNamespace(Conditional conditionalElement, Namespace parent) throws WdlValidationError {
        Namespace namespace = new Namespace();
        namespace.setParent(parent);
        typeCheckElementArray(namespace, conditionalElement.getElements());
        return namespace;
    }

    private Namespace typeCheckScatterNamespace(Scatter scatterElement, Namespace parent) throws WdlValidationError {
        Namespace namespace = new Namespace();
        namespace.setParent(parent);
        Type scatterType = scatterElement.getExpression().typeCheck(scatterElement, parent);
        if (!(scatterType instanceof ArrayType)) {
            throw new WdlValidationError(
                    "Illegal expression in scatter operation. Scatters can only be conducted on expressions evaluated to an Array, but got " + scatterType
                            .getTypeName());
        }
        namespace.addDeclaration(scatterElement.getVarname(),
                                 new Declaration(((ArrayType) scatterType).getInnerType(),
                                                 scatterElement.getVarname(),
                                                 null,
                                                 scatterElement.getId(),
                                                 false,
                                                 false,
                                                 false));
        typeCheckElementArray(namespace, scatterElement.getWorkflowElements());
        return namespace;
    }

    private Namespace captureInputNameSpace(Namespace parent) throws NamespaceException {
        Namespace namespace = new Namespace();
        namespace.setParent(parent);

        if (inputs != null && inputs.getDeclarations() != null) {
            for (Declaration inputDeclaration : inputs.getDeclarations()) {
                namespace.addDeclaration(inputDeclaration.getName(), inputDeclaration);
            }
        }
        return namespace;
    }

    public void validate(Namespace parent) throws WdlValidationError {
        Namespace namespace = captureInputNameSpace(parent);
        //Typecheck elements in recursive order
        typeCheckElementArray(namespace, elements);

        if (outputs != null) {
            typeCheckElementArray(namespace, outputs.getDeclarations());
        }
    }
}
