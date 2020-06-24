package com.dnastack.wdl4j.expression.literal;

import com.dnastack.wdl4j.Namespace;
import com.dnastack.wdl4j.api.WdlElement;
import com.dnastack.wdl4j.exception.WdlValidationError;
import com.dnastack.wdl4j.expression.Expression;
import com.dnastack.wdl4j.typing.ObjectType;
import com.dnastack.wdl4j.typing.Type;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;

@Value
@EqualsAndHashCode(callSuper = true)
public class ObjectLiteral extends Expression {

    Map<String, Expression> values;

    public ObjectLiteral(@NonNull Map<String, Expression> values, @NonNull int id) {
        super(id);
        this.values = values;
    }

    @Override
    public Type typeCheck(WdlElement target, Namespace namespace) throws WdlValidationError {
        if (values == null && values.isEmpty()) {
            return ObjectType.getType();
        }

        Map<String, Type> typeMap = new HashMap<>();
        for (Map.Entry<String, Expression> entry : values.entrySet()) {
            Type evaluatedType = entry.getValue().typeCheck(target, namespace);
            typeMap.put(entry.getKey(), evaluatedType);
        }

        return ObjectType.getType(typeMap);
    }
}
