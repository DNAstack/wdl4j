package com.dnastack.wdl4j;

import com.dnastack.wdl4j.api.NamedElement;
import com.dnastack.wdl4j.api.WdlElement;
import com.dnastack.wdl4j.expression.Expression;
import com.dnastack.wdl4j.typing.Type;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
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
