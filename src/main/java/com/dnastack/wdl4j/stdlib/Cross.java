package com.dnastack.wdl4j.stdlib;

import com.dnastack.wdl4j.api.EngineFunction;
import com.dnastack.wdl4j.exception.ArityException;
import com.dnastack.wdl4j.exception.TypeCoercionException;
import com.dnastack.wdl4j.exception.WdlValidationError;
import com.dnastack.wdl4j.typing.AnyType;
import com.dnastack.wdl4j.typing.ArrayType;
import com.dnastack.wdl4j.typing.PairType;
import com.dnastack.wdl4j.typing.Type;

import java.util.List;

public class Cross implements EngineFunction {

    @Override
    public Type evaluateReturnType(List<Type> argumentTypes) throws WdlValidationError {
        Type firstArgument = argumentTypes.get(0);
        Type secondArgument = argumentTypes.get(1);
        Type requiredType = ArrayType.getType(AnyType.getType(),false);


        if (!firstArgument.isCoercibleTo(requiredType)) {
            throw new TypeCoercionException(
                    "Illegal argument type for cross function, expecting one Array[X] but got " + firstArgument
                            .getTypeName());
        }


        if (!secondArgument.isCoercibleTo(requiredType)) {
            throw new TypeCoercionException("Illegal argument type for cross function, expecting one Array[Y] but got " + secondArgument
                    .getTypeName());
        }


        Type firstInnerType = ((ArrayType) firstArgument).getInnerType();
        Type secondInnerType = ((ArrayType) secondArgument).getInnerType();
        return ArrayType.getType(PairType.getType(firstInnerType,secondInnerType),false);
    }

    @Override
    public void checkArity(List<Type> argumentTypes) throws ArityException {
        if (argumentTypes == null || argumentTypes.size() != 2){
            throw new ArityException("Invalid number of arguments for function cross. Expecting 2 but got " + (argumentTypes == null ? "none" : argumentTypes.size()));
        }
    }
}
