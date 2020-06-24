package com.dnastack.wdl4j.stdlib;

import com.dnastack.wdl4j.api.EngineFunction;
import com.dnastack.wdl4j.exception.ArityException;
import com.dnastack.wdl4j.exception.TypeCoercionException;
import com.dnastack.wdl4j.exception.WdlValidationError;
import com.dnastack.wdl4j.typing.ArrayType;
import com.dnastack.wdl4j.typing.FileType;
import com.dnastack.wdl4j.typing.ObjectType;
import com.dnastack.wdl4j.typing.Type;

import java.util.List;

public class WriteObject implements EngineFunction {

    private final boolean writeMultiple;

    public WriteObject(boolean writeMultiple) {
        this.writeMultiple = writeMultiple;
    }

    @Override
    public Type evaluateReturnType(List<Type> argumentTypes) throws WdlValidationError {
        Type argumentType = argumentTypes.get(0);
        Type requiredType = writeMultiple ? ArrayType.getType(ObjectType.getType(), false) : ObjectType.getType();

        if (!argumentType.isCoercibleTo(requiredType)) {
            throw new TypeCoercionException(
                    "Illegal argument type for write_object function, expecting Array[String] but got " + argumentType.getTypeName());
        }
        return FileType.getType();
    }

    @Override
    public void checkArity(List<Type> argumentTypes) throws ArityException {
        if (argumentTypes == null || argumentTypes.size() != 1){
            throw new ArityException("Invalid number of arguments for function write_object. Expecting 1 but got " + (argumentTypes == null ? "none" : argumentTypes.size()));
        }
    }
}
