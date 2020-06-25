package com.dnastack.wdl4j.lib.expression;

import com.dnastack.wdl4j.lib.Namespace;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.exception.ExpressionEvaluationException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.typing.BooleanType;
import com.dnastack.wdl4j.lib.typing.FloatType;
import com.dnastack.wdl4j.lib.typing.IntType;
import com.dnastack.wdl4j.lib.typing.Type;
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
