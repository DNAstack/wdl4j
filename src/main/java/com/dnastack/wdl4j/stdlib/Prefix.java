package com.dnastack.wdl4j.stdlib;

import com.dnastack.wdl4j.api.EngineFunction;
import com.dnastack.wdl4j.exception.ArityException;
import com.dnastack.wdl4j.exception.TypeCoercionException;
import com.dnastack.wdl4j.exception.WdlValidationError;
import com.dnastack.wdl4j.typing.ArrayType;
import com.dnastack.wdl4j.typing.StringType;
import com.dnastack.wdl4j.typing.Type;

import java.util.List;

public class Prefix implements EngineFunction {

    @Override
    public Type evaluateReturnType(List<Type> argumentTypes) throws WdlValidationError {
        Type argumentType = argumentTypes.get(0);
        Type stringArgumentType = argumentTypes.get(1);

        if (!argumentType.isCoercibleTo(StringType.getType())) {
            throw new TypeCoercionException(
                    "Illegal argument type for prefx function argument [1], expecting String but got " + argumentType.getTypeName());
        }

        if (!stringArgumentType.isCoercibleTo(ArrayType.getType(StringType.getType(), false))) {
            throw new TypeCoercionException(
                    "Illegal argument type for sub function argument [2], expecting Array[String] but got " + argumentType
                            .getTypeName());
        }

        return ArrayType.getType(StringType.getType(), false);
    }

    @Override
    public void checkArity(List<Type> argumentTypes) throws ArityException {
        if (argumentTypes == null || argumentTypes.size() != 2) {
            throw new ArityException("Invalid number of arguments for function prefix. Expecting 3 but got " + (
                    argumentTypes == null
                    ? "none"
                    : argumentTypes.size()));
        }
    }
}
