package com.dnastack.wdl4j.lib;

import com.dnastack.wdl4j.lib.api.NamedElement;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.expression.Expression;
import com.dnastack.wdl4j.lib.typing.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class Declaration implements WdlElement, NamedElement {

    @NonNull
    private Type declType;
    @NonNull
    private String name;
    private Expression expression;

    @NonNull
    private int id;


}
