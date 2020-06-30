package com.dnastack.wdl4j.lib;

import com.dnastack.wdl4j.lib.api.NamedElement;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class Struct implements WdlElement, NamedElement {

    @NonNull
    private String name;
    @NonNull
    private List<Declaration> members;
    @NonNull
    private int id;

    public void validate(Namespace namespace) throws NamespaceException {
        for (Declaration member : members) {
            member.getDeclType().typecheck(namespace);
        }
    }

}
