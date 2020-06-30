package com.dnastack.wdl4j.lib;

import com.dnastack.wdl4j.lib.api.NamedElement;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.expression.Expression;
import com.dnastack.wdl4j.lib.typing.Type;
import lombok.*;

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

    @Getter
    @Setter
    private boolean exposeToOuterScope = true;

    @Setter
    @Getter
    private boolean fromInputs = false;
    @Getter
    @Setter
    private boolean fromOutputs = false;

    public Declaration(@NonNull Type declType, @NonNull String name, Expression expression, @NonNull int id) {
        this.declType = declType;
        this.name = name;
        this.expression = expression;
        this.id = id;
    }

}
