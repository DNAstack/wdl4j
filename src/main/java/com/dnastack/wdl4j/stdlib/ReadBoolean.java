package com.dnastack.wdl4j.stdlib;

import com.dnastack.wdl4j.api.EngineFunction;
import com.dnastack.wdl4j.exception.ArityException;
import com.dnastack.wdl4j.exception.TypeCoercionException;
import com.dnastack.wdl4j.exception.WdlValidationError;
import com.dnastack.wdl4j.typing.BooleanType;
import com.dnastack.wdl4j.typing.FileType;
import com.dnastack.wdl4j.typing.StringType;
import com.dnastack.wdl4j.typing.Type;

import java.util.List;

public class ReadBoolean implements EngineFunction {

    @Override
    public Type evaluateReturnType(List<Type> argumentTypes) throws WdlValidationError {
        Type argumentType = argumentTypes.get(0);
        if (!argumentType.isCoercibleTo(FileType.getType()) && !argumentType.isCoercibleTo(StringType.getType())) {
            throw new TypeCoercionException(
                    "Illegal argument type for read_boolean function, expecting one of [String,File] but got " + argumentType
                            .getTypeName());
        }
        return BooleanType.getType();
    }

    @Override
    public void checkArity(List<Type> argumentTypes) throws ArityException {
        if (argumentTypes == null || argumentTypes.size() != 1) {
            throw new ArityException("Invalid number of arguments for function read_boolean. Expecting 1 but got " + (
                    argumentTypes == null
                    ? "none"
                    : argumentTypes.size()));
        }
    }
}
