package com.dnastack.wdl4j.lib.expression;

import com.dnastack.wdl4j.lib.Namespace;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.typing.Type;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class ApplyFunction extends Expression {

    String name;
    List<Expression> arguments;

    public ApplyFunction(@NonNull String name, @NonNull List<Expression> arguments, @NonNull int id) {
        super(id);
        this.name = name;
        this.arguments = arguments;
    }

    @Override
    public Type typeCheck(WdlElement target, Namespace namespace) throws WdlValidationError {
        List<Type> argumentTypes = new ArrayList<>();
        for (Expression argument : arguments) {
            argumentTypes.add(argument.typeCheck(target, namespace));
        }
        return namespace.getLib().evaluateReturnType(name, argumentTypes, namespace.getCoercionOptions());
    }
}
