package com.dnastack.wdl4j;

import com.dnastack.wdl4j.api.NamedElement;
import com.dnastack.wdl4j.api.WdlElement;
import com.dnastack.wdl4j.exception.NamespaceException;
import com.dnastack.wdl4j.exception.WdlValidationError;
import com.dnastack.wdl4j.expression.Expression;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class Task implements WdlElement, NamedElement {

    @NonNull
    private String name;
    private Inputs inputs;
    private List<Declaration> declarations;
    private Command command;
    private Runtime runtime;
    private Outputs outputs;
    private Meta meta;
    private ParameterMeta parameterMeta;
    @NonNull
    private int id;

    public void typeCheck(Namespace parent) throws WdlValidationError {
        Namespace currentNamespace = captureNamespaceWithoutOutputs(parent);
        if (inputs != null && inputs.getDeclarations() != null) {
            for (Declaration declaration : inputs.getDeclarations()) {

                if (declaration.getExpression() != null) {
                    declaration.getExpression().typeCheck(declaration, currentNamespace);
                }
            }
        }

        if (declarations != null) {
            for (Declaration declaration : declarations) {
                declaration.getExpression().typeCheck(declaration, currentNamespace);
            }
        }

        if (command == null) {
            throw new WdlValidationError("A task Requires at least one command");
        } else {
            for (Command.CommandPart part : command.getCommandParts()) {
                if (part.getExpression() != null) {
                    part.getExpression().typeCheck(command, currentNamespace);
                }
            }
        }

        if (runtime != null && runtime.getAttributes() != null) {
            for (Map.Entry<String, Expression> entry : runtime.getAttributes().entrySet()) {
                entry.getValue().typeCheck(runtime, currentNamespace);
            }
        }

        if (outputs != null && outputs.getDeclarations() != null) {
            for (Declaration outputDeclaration : outputs.getDeclarations()) {
                currentNamespace.addDeclaration(outputDeclaration.getName(), outputDeclaration);
            }
            for (Declaration declaration : outputs.getDeclarations()) {
                if (declaration.getExpression() != null) {
                    declaration.getExpression().typeCheck(declaration, currentNamespace);
                }
            }
        }
    }

    private Namespace captureNamespaceWithoutOutputs(Namespace parent) throws NamespaceException {
        Namespace namespace = new Namespace();
        namespace.setParent(parent);
        if (inputs != null && inputs.getDeclarations() != null) {
            for (Declaration inputDeclaration : inputs.getDeclarations()) {
                namespace.addDeclaration(inputDeclaration.getName(), inputDeclaration);
            }
        }

        if (declarations != null) {
            for (Declaration declaration : declarations) {
                namespace.addDeclaration(declaration.getName(), declaration);
            }
        }

        return namespace;
    }
}