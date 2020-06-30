package com.dnastack.wdl4j.lib.expression.literal;

import com.dnastack.wdl4j.lib.Namespace;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.exception.TypeCoercionException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.expression.Expression;
import com.dnastack.wdl4j.lib.typing.AnyType;
import com.dnastack.wdl4j.lib.typing.ArrayType;
import com.dnastack.wdl4j.lib.typing.Type;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class ArrayLiteral extends Expression {

    List<Expression> values;

    public ArrayLiteral(@NonNull List<Expression> values, @NonNull int id) {
        super(id);
        this.values = values;
    }

    @Override
    public Type typeCheck(WdlElement target, Namespace namespace) throws WdlValidationError {
        if (values == null || values.isEmpty()) {
            return ArrayType.getType(AnyType.getType(), false);
        } else {
            Type targetType = null;
            for (Expression expression : values) {
                Type evalutedType = expression.typeCheck(target, namespace);

                if (targetType == null) {
                    targetType = evalutedType;
                } else if (!evalutedType.isCoercibleTo(namespace.getCoercionOptions(), targetType)) {
                    throw new TypeCoercionException("Illegal type coercion. Cannot coerce type " + evalutedType.getTypeName() + " to " + targetType
                            .getTypeName());
                }
            }
            return ArrayType.getType(targetType, false);
        }
    }
}
