package com.dnastack.wdl4j.lib.stdlib;

import com.dnastack.wdl4j.lib.api.EngineFunction;
import com.dnastack.wdl4j.lib.exception.ArityException;
import com.dnastack.wdl4j.lib.exception.TypeCoercionException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.typing.AnyType;
import com.dnastack.wdl4j.lib.typing.ArrayType;
import com.dnastack.wdl4j.lib.typing.OptionalType;
import com.dnastack.wdl4j.lib.typing.Type;

import java.util.List;

public class SelectAll implements EngineFunction {

    @Override
    public Type evaluateReturnType(List<Type> argumentTypes) throws WdlValidationError {
        Type argumentType = argumentTypes.get(0);
        Type requiredType = ArrayType.getType(OptionalType.getType(AnyType.getType()), false);

        if (!argumentType.isCoercibleTo(requiredType)) {
            throw new TypeCoercionException(
                    "Illegal argument type for select_all function, expecting Array[X?] but got " + argumentType.getTypeName());
        }

        OptionalType innerType = (OptionalType) ((ArrayType) argumentType).getInnerType();
        return ArrayType.getType(innerType.getInnerType(), false);
    }

    @Override
    public void checkArity(List<Type> argumentTypes) throws ArityException {
        if (argumentTypes == null || argumentTypes.size() != 1) {
            throw new ArityException("Invalid number of arguments for function select_all. Expecting 1 but got " + (
                    argumentTypes == null
                    ? "none"
                    : argumentTypes.size()));
        }
    }
}
