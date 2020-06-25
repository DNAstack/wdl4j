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
public class TrueFalsePlaceholder extends Expression {

    Expression value;
    Condition condition;

    public enum Condition {
        TRUE, FALSE
    }

    public TrueFalsePlaceholder(@NonNull Expression value, @NonNull Condition condition, @NonNull int id) {
        super(id);
        this.value = value;
        this.condition = condition;
    }


    @Override
    public Type typeCheck(WdlElement target, Namespace namespace) throws WdlValidationError {
        Type valueType = value.typeCheck(target, namespace);
        if (!valueType.isCoercibleTo(StringType.getType())) {
            throw new TypeCoercionException(
                    "Illegal type evaluation for true/false placeholder. Expression must be coercible to type String but got " + valueType
                            .getTypeName());
        }
        return StringType.getType();
    }
}
