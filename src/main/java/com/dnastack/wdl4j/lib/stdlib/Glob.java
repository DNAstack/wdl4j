package com.dnastack.wdl4j.lib.stdlib;

import com.dnastack.wdl4j.lib.api.EngineFunction;
import com.dnastack.wdl4j.lib.exception.ArityException;
import com.dnastack.wdl4j.lib.exception.TypeCoercionException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.typing.*;

import java.util.List;

public class Glob implements EngineFunction {

    @Override
    public Type evaluateReturnType(List<Type> argumentTypes, CoercionOptions options) throws WdlValidationError {
        Type argumentType = argumentTypes.get(0);
        if (!argumentType.isCoercibleTo(options, StringType.getType())) {
            throw new TypeCoercionException("Illegal argument type for glob function, expecting String but got " + argumentType
                    .getTypeName());
        }
        return ArrayType.getType(FileType.getType(), false);
    }

    @Override
    public void checkArity(List<Type> argumentTypes) throws ArityException {
        if (argumentTypes == null || argumentTypes.size() != 1) {
            throw new ArityException("Invalid number of arguments for function glob. Expecting 1 but got " + (
                    argumentTypes == null
                    ? "none"
                    : argumentTypes.size()));
        }
    }
}
