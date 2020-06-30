package com.dnastack.wdl4j.lib.stdlib;

import com.dnastack.wdl4j.lib.api.EngineFunction;
import com.dnastack.wdl4j.lib.exception.ArityException;
import com.dnastack.wdl4j.lib.exception.TypeCoercionException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.typing.*;

import java.util.List;

public class ReadMap implements EngineFunction {

    @Override
    public Type evaluateReturnType(List<Type> argumentTypes, CoercionOptions options) throws WdlValidationError {
        Type argumentType = argumentTypes.get(0);
        if (!argumentType.isCoercibleTo(options, FileType.getType()) && !argumentType.isCoercibleTo(options,
                                                                                                    StringType.getType())) {
            throw new TypeCoercionException(
                    "Illegal argument type for read_map function, expecting one of [String,File] but got " + argumentType
                            .getTypeName());
        }
        return MapType.getType(StringType.getType(), StringType.getType());
    }

    @Override
    public void checkArity(List<Type> argumentTypes) throws ArityException {
        if (argumentTypes == null || argumentTypes.size() != 1) {
            throw new ArityException("Invalid number of arguments for function read_map. Expecting 1 but got " + (
                    argumentTypes == null
                    ? "none"
                    : argumentTypes.size()));
        }
    }
}
