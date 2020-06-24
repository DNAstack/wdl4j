package com.dnastack.wdl4j.stdlib;

import com.dnastack.wdl4j.api.EngineFunction;
import com.dnastack.wdl4j.exception.ArityException;
import com.dnastack.wdl4j.exception.TypeCoercionException;
import com.dnastack.wdl4j.exception.WdlValidationError;
import com.dnastack.wdl4j.typing.StringType;
import com.dnastack.wdl4j.typing.Type;

import java.util.List;

public class Sub implements EngineFunction {

    @Override
    public Type evaluateReturnType(List<Type> argumentTypes) throws WdlValidationError {
        Type argumentType = argumentTypes.get(0);
        Type regexType = argumentTypes.get(1);
        Type subType = argumentTypes.get(2);

        if (!argumentType.isCoercibleTo(StringType.getType())) {
            throw new TypeCoercionException(
                    "Illegal argument type for sub function argument [1], expecting String but got " + argumentType.getTypeName());
        }

        if (!(regexType instanceof StringType)) {

            throw new TypeCoercionException(
                    "Illegal argument type for sub function argument [2], expecting String but got " + argumentType.getTypeName());
        }

        if (!subType.isCoercibleTo(StringType.getType())) {
            throw new TypeCoercionException(
                    "Illegal argument type for sub function argument [3], expecting String but got " + argumentType.getTypeName());
        }

        return StringType.getType();
    }

    @Override
    public void checkArity(List<Type> argumentTypes) throws ArityException {
        if (argumentTypes == null || argumentTypes.size() != 3) {
            throw new ArityException("Invalid number of arguments for function sub. Expecting 3 but got " + (
                    argumentTypes == null
                    ? "none"
                    : argumentTypes.size()));
        }
    }
}
