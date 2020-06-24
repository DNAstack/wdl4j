package com.dnastack.wdl4j.expression;

import com.dnastack.wdl4j.Namespace;
import com.dnastack.wdl4j.api.WdlElement;
import com.dnastack.wdl4j.exception.TypeCoercionException;
import com.dnastack.wdl4j.exception.WdlValidationError;
import com.dnastack.wdl4j.typing.StringType;
import com.dnastack.wdl4j.typing.Type;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class SepPlaceholder extends Expression {

    Expression value;

    public SepPlaceholder(@NonNull Expression value, @NonNull int id) {
        super(id);
        this.value = value;
    }

    @Override
    public Type typeCheck(WdlElement target, Namespace namespace) throws WdlValidationError {
        Type valueType = value.typeCheck(target, namespace);
        if (!valueType.isCoercibleTo(StringType.getType())) {
            throw new TypeCoercionException(
                    "Illegal type evaluation for sep placeholder. Expression must be coercible to type String but got " + valueType
                            .getTypeName());
        }
        return StringType.getType();
    }
}
