package com.dnastack.wdl4j.lib.stdlib;

import com.dnastack.wdl4j.lib.api.EngineFunction;
import com.dnastack.wdl4j.lib.exception.ArityException;
import com.dnastack.wdl4j.lib.exception.TypeCoercionException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.typing.*;

import java.util.List;

public class WriteObject implements EngineFunction {

    private final boolean writeMultiple;

    public WriteObject(boolean writeMultiple) {
        this.writeMultiple = writeMultiple;
    }

    @Override
    public Type evaluateReturnType(List<Type> argumentTypes, CoercionOptions options) throws WdlValidationError {
        Type argumentType = argumentTypes.get(0);
        Type requiredType = writeMultiple ? ArrayType.getType(ObjectType.getType(), false) : ObjectType.getType();

        if (!argumentType.isCoercibleTo(options, requiredType)) {
            throw new TypeCoercionException(
                    "Illegal argument type for write_object function, expecting Array[String] but got " + argumentType.getTypeName());
        }
        return FileType.getType();
    }

    @Override
    public void checkArity(List<Type> argumentTypes) throws ArityException {
        if (argumentTypes == null || argumentTypes.size() != 1) {
            throw new ArityException("Invalid number of arguments for function write_object. Expecting 1 but got " + (
                    argumentTypes == null
                    ? "none"
                    : argumentTypes.size()));
        }
    }
}
