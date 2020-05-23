package io.github.patmagee.wdl4j.v1.stdlib;

import io.github.patmagee.wdl4j.v1.api.EngineFunction;
import io.github.patmagee.wdl4j.v1.exception.ArityException;
import io.github.patmagee.wdl4j.v1.exception.TypeCoercionException;
import io.github.patmagee.wdl4j.v1.exception.WdlValidationError;
import io.github.patmagee.wdl4j.v1.typing.AnyType;
import io.github.patmagee.wdl4j.v1.typing.ArrayType;
import io.github.patmagee.wdl4j.v1.typing.IntType;
import io.github.patmagee.wdl4j.v1.typing.Type;

import java.util.List;

public class Length implements EngineFunction {

    @Override
    public Type evaluateReturnType(List<Type> argumentTypes) throws WdlValidationError {
        Type argumentType = argumentTypes.get(0);
        Type requiredType = ArrayType.getType(AnyType.getType(),false);

        if (!argumentType.isCoercibleTo( requiredType)) {
            throw new TypeCoercionException(
                    "Illegal argument type for length function, expecting one Array[X] but got " + argumentType
                            .getTypeName());
        }

        return IntType.getType();
    }

    @Override
    public void checkArity(List<Type> argumentTypes) throws ArityException {
        if (argumentTypes == null || argumentTypes.size() != 1){
            throw new ArityException("Invalid number of arguments for function length. Expecting 1 but got " + (argumentTypes == null ? "none" : argumentTypes.size()));
        }
    }
}
