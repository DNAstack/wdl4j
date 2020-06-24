package com.dnastack.wdl4j.expression;

import com.dnastack.wdl4j.Namespace;
import com.dnastack.wdl4j.api.WdlElement;
import com.dnastack.wdl4j.exception.ExpressionEvaluationException;
import com.dnastack.wdl4j.exception.WdlValidationError;
import com.dnastack.wdl4j.typing.BooleanType;
import com.dnastack.wdl4j.typing.FloatType;
import com.dnastack.wdl4j.typing.IntType;
import com.dnastack.wdl4j.typing.Type;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class Negate extends Expression {

    Expression expression;

    public Negate(@NonNull Expression expression,@NonNull int id) {
        super(id);
        this.expression = expression;
    }

    @Override
    public Type typeCheck(WdlElement target, Namespace namespace) throws WdlValidationError {
        Type evaluatedType = expression.typeCheck(target, namespace);

        if (evaluatedType instanceof FloatType || evaluatedType instanceof IntType || evaluatedType instanceof BooleanType){
            return evaluatedType;
        } else {
            throw new ExpressionEvaluationException("Illegal Type for negation. Expecting an expression evaluating to one of [Boolean,Int,Float] but got " + evaluatedType.getTypeName());
        }
    }
}
