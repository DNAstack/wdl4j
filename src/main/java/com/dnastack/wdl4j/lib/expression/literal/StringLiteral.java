package com.dnastack.wdl4j.lib.expression.literal;

import com.dnastack.wdl4j.lib.Namespace;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.exception.ExpressionEvaluationException;
import com.dnastack.wdl4j.lib.exception.TypeCoercionException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.expression.Expression;
import com.dnastack.wdl4j.lib.typing.StringType;
import com.dnastack.wdl4j.lib.typing.Type;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Value
@EqualsAndHashCode(callSuper = true)
public class StringLiteral extends Expression {

    List<StringPart> stringParts;

    @Getter
    @ToString
    @EqualsAndHashCode
    public static class StringPart implements WdlElement {

        private final String stringPart;
        private final Expression expression;
        private final List<Expression> placeholders;
        private final int id;

        public StringPart(String stringPart, int id) {
            Objects.requireNonNull(stringPart, "The string of a StringPart cannot be null");
            this.stringPart = stringPart;
            this.expression = null;
            this.placeholders = null;
            this.id = id;
        }

        public StringPart(List<Expression> placeholders, Expression expression, int id) {
            Objects.requireNonNull(expression, "the Expression of StringPart cannot be null");
            this.stringPart = null;
            this.placeholders = placeholders;
            this.expression = expression;
            this.id = id;
        }
    }

    public StringLiteral(List<StringPart> stringParts, int id) {
        super(id);
        if (stringParts == null) {
            stringParts = Collections.emptyList();
        }
        this.stringParts = stringParts;
    }

    public boolean requiresEvaluation() {
        if (stringParts == null) {
            return false;
        }

        if (stringParts.size() == 1) {
            StringPart part = stringParts.get(0);
            return part.getStringPart() != null && part.getExpression() == null && part.getPlaceholders() == null;
        } else {
            return true;
        }
    }

    public String getUnevaluatedString() throws ExpressionEvaluationException {
        if (requiresEvaluation()) {
            throw new ExpressionEvaluationException(
                    "Could not construct unevaluated string. Expression part requires evaluation");
        }

        return stringParts.get(0).getStringPart();
    }

    @Override
    public Type typeCheck(WdlElement target, Namespace namespace) throws WdlValidationError {
        if (stringParts != null) {
            for (StringPart part : stringParts) {
                if (part.getExpression() != null) {
                    Type evaluatedType = part.getExpression().typeCheck(target, namespace);
                    if (!evaluatedType.isCoercibleTo(namespace.getCoercionOptions(), StringType.getType())) {
                        throw new TypeCoercionException("Illegal type coercion. Cannot cast type " + evaluatedType.getTypeName() + " to String");
                    }
                }
            }
        }

        return StringType.getType();

    }
}
