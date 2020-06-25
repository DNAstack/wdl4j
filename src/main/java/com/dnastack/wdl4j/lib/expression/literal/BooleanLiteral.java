package com.dnastack.wdl4j.lib.expression.literal;

import com.dnastack.wdl4j.lib.Namespace;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.expression.Expression;
import com.dnastack.wdl4j.lib.typing.BooleanType;
import com.dnastack.wdl4j.lib.typing.Type;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class BooleanLiteral extends Expression {

    boolean value;

    public BooleanLiteral(boolean value,@NonNull int id) {
        super(id);
        this.value = value;
    }


    @Override
    public Type typeCheck(WdlElement target, Namespace namespace) throws WdlValidationError {
        return BooleanType.getType();
    }
}
