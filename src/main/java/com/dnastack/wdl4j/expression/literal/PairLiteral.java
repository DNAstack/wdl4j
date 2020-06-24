package com.dnastack.wdl4j.expression.literal;

import com.dnastack.wdl4j.Namespace;
import com.dnastack.wdl4j.api.WdlElement;
import com.dnastack.wdl4j.exception.WdlValidationError;
import com.dnastack.wdl4j.expression.Expression;
import com.dnastack.wdl4j.typing.PairType;
import com.dnastack.wdl4j.typing.Type;
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

