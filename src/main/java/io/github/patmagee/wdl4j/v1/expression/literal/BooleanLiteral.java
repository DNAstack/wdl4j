package io.github.patmagee.wdl4j.v1.expression.literal;

import io.github.patmagee.wdl4j.v1.Namespace;
import io.github.patmagee.wdl4j.v1.api.WdlElement;
import io.github.patmagee.wdl4j.v1.exception.WdlValidationError;
import io.github.patmagee.wdl4j.v1.expression.Expression;
import io.github.patmagee.wdl4j.v1.typing.BooleanType;
import io.github.patmagee.wdl4j.v1.typing.Type;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class BooleanLiteral extends Expression {

    boolean value;

    public BooleanLiteral(boolean value,@NonNull int id) {
        super(id);
        this.value = value;
    }


    @Override
    public Type typeCheck(WdlElement target, Namespace namespace) throws WdlValidationError {
        return BooleanType.getType();
    }
}
