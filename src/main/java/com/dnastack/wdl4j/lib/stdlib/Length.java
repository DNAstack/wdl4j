package com.dnastack.wdl4j.lib.stdlib;

import com.dnastack.wdl4j.lib.api.EngineFunction;
import com.dnastack.wdl4j.lib.exception.ArityException;
import com.dnastack.wdl4j.lib.exception.TypeCoercionException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.typing.*;

import java.util.List;

public class Length implements EngineFunction {

    @Override
    public Type evaluateReturnType(List<Type> argumentTypes, CoercionOptions options) throws WdlValidationError {
        Type argumentType = argumentTypes.get(0);
        Type requiredType = ArrayType.getType(AnyType.getType(), false);

        if (!argumentType.isCoercibleTo(options, requiredType)) {
            throw new TypeCoercionException("Illegal argument type for length function, expecting one Array[X] but got " + argumentType
                    .getTypeName());
        }

        return IntType.getType();
    }

    @Override
    public void checkArity(List<Type> argumentTypes) throws ArityException {
        if (argumentTypes == null || argumentTypes.size() != 1) {
            throw new ArityException("Invalid number of arguments for function length. Expecting 1 but got " + (
                    argumentTypes == null
                    ? "none"
                    : argumentTypes.size()));
        }
    }
}
