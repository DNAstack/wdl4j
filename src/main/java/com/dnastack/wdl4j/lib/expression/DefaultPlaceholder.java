package com.dnastack.wdl4j.lib.expression;

import com.dnastack.wdl4j.lib.Namespace;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.exception.TypeCoercionException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.typing.StringType;
import com.dnastack.wdl4j.lib.typing.Type;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class DefaultPlaceholder extends Expression {

    Expression value;

    public DefaultPlaceholder(@NonNull Expression value, @NonNull int id) {
        super(id);
        this.value = value;
    }

    @Override
    public Type typeCheck(WdlElement target, Namespace namespace) throws WdlValidationError {
        Type evaluatedType = value.typeCheck(target, namespace);
        if (!evaluatedType.isCoercibleTo(namespace.getCoercionOptions(), StringType.getType())) {
            throw new TypeCoercionException("Illegal Type Coercion. Cannot coerce " + evaluatedType.getTypeName() + " into String");
        }
        return StringType.getType();
    }
}
