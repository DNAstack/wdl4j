package com.dnastack.wdl4j.lib;

import com.dnastack.wdl4j.lib.api.NamedElement;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.typing.ArrayType;
import com.dnastack.wdl4j.lib.typing.OptionalType;
import com.dnastack.wdl4j.lib.typing.Type;
import lombok.*;

import java.util.List;
import java.util.Map;
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

    public void typeCheck(Namespace parent) throws WdlValidationError {
        Namespace namespace = captureInputNameSpace(parent);
        //Typecheck elements in recursive order
        typeCheckElementArray(namespace, elements);
    }

    private void typeCheckElementArray(Namespace namespace, List<WdlElement> wdlElements) throws WdlValidationError {
        for (WdlElement element : wdlElements) {
            if (element instanceof Declaration) {
                typeCheckDeclaration(namespace, (Declaration) element);
            } else if (element instanceof Call) {
                typeCheckCall(namespace, (Call) element);
            } else if (element instanceof Conditional) {
                Namespace conditionalNamespace = typeCheckConditionalNamespace((Conditional) element, namespace);
                for (Map.Entry<String, Declaration> entry : conditionalNamespace.getDeclarations().entrySet()) {
                    String key = entry.getKey();
                    Declaration value = entry.getValue();
                    Declaration condDecl;
                    if (!(value.getDeclType() instanceof OptionalType)) {
                        condDecl = new Declaration(OptionalType.getType(value.getDeclType()),
                                                   value.getName(),
                                                   null,
                                                   value.getId());
                    } else {
                        condDecl = new Declaration(value.getDeclType(), value.getName(), null, value.getId());
                    }
                    namespace.addDeclaration(key, condDecl);
                }
            } else if (element instanceof Scatter) {
                Scatter scatter = (Scatter) element;
                Namespace scatterNamespace = typeCheckScatterNamespace(scatter, namespace);
                for (Map.Entry<String, Declaration> entry : scatterNamespace.getDeclarations().entrySet()) {
                    String key = entry.getKey();
                    Declaration value = entry.getValue();
                    namespace.addDeclaration(key,
                                             new Declaration(ArrayType.getType(value.getDeclType()),
                                                             value.getName(),
                                                             null,
                                                             value.getId()));
                }
            }
        }
    }

    private void typeCheckDeclaration(Namespace namespace, Declaration declaration) throws WdlValidationError {
        namespace.addDeclaration(declaration.getName(), declaration);
        if (declaration.getExpression() != null) {
            declaration.getExpression().typeCheck(declaration, namespace);
        }
    }

    private void typeCheckCall(Namespace namespace, Call call) throws WdlValidationError {
        Task task = namespace.getTask(call.getTaskName());
        if (task.getOutputs() != null && task.getOutputs().getDeclarations() != null) {
            for (Declaration declaration : task.getOutputs().getDeclarations()) {
                namespace.addDeclaration(call.getName() + "." + declaration.getName(), declaration);
            }
        }
    }

    private Namespace typeCheckConditionalNamespace(Conditional conditionalElement, Namespace parent) throws WdlValidationError {
        Namespace namespace = new Namespace();
        namespace.setParent(parent);
        typeCheckElementArray(namespace,conditionalElement.getElements());
        return namespace;
    }

    private Namespace typeCheckScatterNamespace(Scatter scatterElement, Namespace parent) throws WdlValidationError {
        Namespace namespace = new Namespace();
        namespace.setParent(parent);
        Type scatterType = scatterElement.getExpression().typeCheck(scatterElement, parent);
        namespace.addDeclaration(scatterElement.getVarname(),new Declaration(scatterType,scatterElement.getVarname(),null,scatterElement.getId()));
        typeCheckElementArray(namespace,scatterElement.getWorkflowElements());
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
}
