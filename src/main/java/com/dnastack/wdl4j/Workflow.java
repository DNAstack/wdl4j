package com.dnastack.wdl4j;

import com.dnastack.wdl4j.api.NamedElement;
import com.dnastack.wdl4j.api.WdlElement;
import com.dnastack.wdl4j.exception.WdlValidationError;
import lombok.*;

import java.util.List;

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



    public void typeCheck(Namespace namespace) throws WdlValidationError {

    }

//    private Namespace captureNamespaceWithoutOutputs(Namespace parent) throws NamespaceException {
//        Namespace namespace = new Namespace();
//        namespace.setParent(parent);
//        if (inputs != null && inputs.getDeclarations() != null) {
//            for (Declaration inputDeclaration : inputs.getDeclarations()) {
//                namespace.addDeclaration(inputDeclaration.getName(), inputDeclaration);
//            }
//        }
//
//        if (declarations != null) {
//            for (Declaration declaration : declarations) {
//                namespace.addDeclaration(declaration.getName(), declaration);
//            }
//        }
//
//        return namespace;
//    }
}
