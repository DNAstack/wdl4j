package com.dnastack.wdl4j.lib.stdlib;

import com.dnastack.wdl4j.lib.api.EngineFunction;
import com.dnastack.wdl4j.lib.exception.ArityException;
import com.dnastack.wdl4j.lib.exception.TypeCoercionException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.typing.AnyType;
import com.dnastack.wdl4j.lib.typing.ArrayType;
import com.dnastack.wdl4j.lib.typing.CoercionOptions;
import com.dnastack.wdl4j.lib.typing.Type;

import java.util.List;

public class Transpose implements EngineFunction {

    @Override
    public Type evaluateReturnType(List<Type> argumentTypes, CoercionOptions options) throws WdlValidationError {
        Type argumentType = argumentTypes.get(0);
        Type requiredType = ArrayType.getType(ArrayType.getType(AnyType.getType(), false), false);

        if (!argumentType.isCoercibleTo(options, requiredType)) {
            throw new TypeCoercionException(
                    "Illegal argument type for transpose function, expecting one Array[Array[X]] but got " + argumentType
                            .getTypeName());
        }

        return argumentType;
    }

    @Override
    public void checkArity(List<Type> argumentTypes) throws ArityException {
        if (argumentTypes == null || argumentTypes.size() != 1) {
            throw new ArityException("Invalid number of arguments for function transpose. Expecting 1 but got " + (
                    argumentTypes == null
                    ? "none"
                    : argumentTypes.size()));
        }
    }
}
