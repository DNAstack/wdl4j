package com.dnastack.wdl4j.stdlib;

import com.dnastack.wdl4j.api.EngineFunction;
import com.dnastack.wdl4j.exception.ArityException;
import com.dnastack.wdl4j.exception.TypeCoercionException;
import com.dnastack.wdl4j.exception.WdlValidationError;
import com.dnastack.wdl4j.typing.AnyType;
import com.dnastack.wdl4j.typing.ArrayType;
import com.dnastack.wdl4j.typing.IntType;
import com.dnastack.wdl4j.typing.Type;

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