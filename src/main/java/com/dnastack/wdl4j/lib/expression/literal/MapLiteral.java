package com.dnastack.wdl4j.lib.expression.literal;

import com.dnastack.wdl4j.lib.Namespace;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.exception.TypeCoercionException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.expression.Expression;
import com.dnastack.wdl4j.lib.typing.AnyType;
import com.dnastack.wdl4j.lib.typing.MapType;
import com.dnastack.wdl4j.lib.typing.Type;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class MapLiteral extends Expression {

    @NonNull List<MapEntry> values;

    public static class MapEntry {

        final Expression key;
        final Expression value;

        public MapEntry(Expression key, Expression value) {
            this.key = key;
            this.value = value;
        }

        public Expression getKey() {
            return key;
        }

        public Expression getValue() {
            return value;
        }
    }

    public MapLiteral(@NonNull List<MapEntry> values, @NonNull int id) {
        super(id);
        this.values = values;
    }

    @Override
    public Type typeCheck(WdlElement target, Namespace namespace) throws WdlValidationError {
        if (values == null || values.isEmpty()) {
            return MapType.getType(AnyType.getType(), AnyType.getType());
        }

        Type targetKeyType = null;
        Type targetValueType = null;

        List<String> literalNames = new ArrayList<>();
        for (MapEntry entry : values) {
            Expression key = entry.getKey();
            Expression value = entry.getValue();
            if (entry.getKey() instanceof StringLiteral && !((StringLiteral) key).requiresEvaluation()) {
                literalNames.add(((StringLiteral) key).getUnevaluatedString());
            }

            Type expressionKeyType = key.typeCheck(target, namespace);
            Type expressionValueType = value.typeCheck(target, namespace);

            if (targetKeyType == null && targetValueType == null) {
                targetKeyType = expressionKeyType;
                targetValueType = expressionValueType;
            } else if (!expressionKeyType.isCoercibleTo(namespace.getCoercionOptions(), targetKeyType)) {
                throw new TypeCoercionException("Illegal type coercion. Cannot coerce type " + expressionKeyType.getTypeName() + " to " + targetKeyType
                        .getTypeName());

            } else if (!expressionValueType.isCoercibleTo(namespace.getCoercionOptions(), targetValueType)) {
                throw new TypeCoercionException("Illegal type coercion. Cannot coerce type " + expressionValueType.getTypeName() + " to " + targetValueType
                        .getTypeName());

            }

        }
        if (literalNames.size() == values.size()) {
            return MapType.getType(targetKeyType, targetValueType, literalNames);
        } else {
            return MapType.getType(targetKeyType, targetValueType);
        }
    }
}
