package com.dnastack.wdl4j.lib.expression.literal;

import com.dnastack.wdl4j.lib.Namespace;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.expression.Expression;
import com.dnastack.wdl4j.lib.typing.PairType;
import com.dnastack.wdl4j.lib.typing.Type;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class PairLiteral extends Expression {

    Expression leftValue;
    Expression rightValue;

    public PairLiteral(@NonNull Expression leftValue, @NonNull Expression rightValue, @NonNull int id) {
        super(id);
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    @Override
    public Type typeCheck(WdlElement target, Namespace namespace) throws WdlValidationError {
        Type leftType = leftValue.typeCheck(target, namespace);
        Type rightType = rightValue.typeCheck(target, namespace);

        return PairType.getType(leftType, rightType);
    }
}

