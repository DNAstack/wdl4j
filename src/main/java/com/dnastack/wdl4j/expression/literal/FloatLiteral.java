package com.dnastack.wdl4j.expression.literal;

import com.dnastack.wdl4j.Namespace;
import com.dnastack.wdl4j.api.WdlElement;
import com.dnastack.wdl4j.exception.WdlValidationError;
import com.dnastack.wdl4j.expression.Expression;
import com.dnastack.wdl4j.typing.FloatType;
import com.dnastack.wdl4j.typing.Type;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class FloatLiteral extends Expression {

    float value;

    public FloatLiteral(float value, @NonNull int id) {
        super(id);
        this.value = value;
    }

    @Override
    public Type typeCheck(WdlElement target, Namespace namespace) throws WdlValidationError {
        return FloatType.getType();
    }
}
