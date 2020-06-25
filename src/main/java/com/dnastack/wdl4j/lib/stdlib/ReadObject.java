package com.dnastack.wdl4j.lib.stdlib;

import com.dnastack.wdl4j.lib.api.EngineFunction;
import com.dnastack.wdl4j.lib.exception.ArityException;
import com.dnastack.wdl4j.lib.exception.TypeCoercionException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.typing.*;

import java.util.List;

public class ReadObject implements EngineFunction {

    private final boolean readMultiple;

    public ReadObject(boolean readMultiple) {
        this.readMultiple = readMultiple;
    }

    @Override
    public Type evaluateReturnType(List<Type> argumentTypes) throws WdlValidationError {
        Type argumentType = argumentTypes.get(0);
        if (!argumentType.isCoercibleTo(FileType.getType()) && !argumentType.isCoercibleTo(StringType.getType())) {
            throw new TypeCoercionException(
                    "Illegal argument type for read_object function, expecting one of [String,File] but got " + argumentType
                            .getTypeName());
        }

        if (readMultiple) {
            return ArrayType.getType(ObjectType.getType(), false);
        }
        return ObjectType.getType();
    }

    @Override
    public void checkArity(List<Type> argumentTypes) throws ArityException {
        if (argumentTypes == null || argumentTypes.size() != 1) {
            throw new ArityException("Invalid number of arguments for function read_object. Expecting 1 but got " + (
                    argumentTypes == null
                    ? "none"
                    : argumentTypes.size()));
        }
    }
}
