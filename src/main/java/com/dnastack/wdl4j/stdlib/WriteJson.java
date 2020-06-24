package com.dnastack.wdl4j.stdlib;

import com.dnastack.wdl4j.api.EngineFunction;
import com.dnastack.wdl4j.exception.ArityException;
import com.dnastack.wdl4j.exception.WdlValidationError;
import com.dnastack.wdl4j.typing.FileType;
import com.dnastack.wdl4j.typing.Type;

import java.util.List;

public class WriteJson implements EngineFunction {

    @Override
    public Type evaluateReturnType(List<Type> argumentTypes) throws WdlValidationError {
        return FileType.getType();
    }

    @Override
    public void checkArity(List<Type> argumentTypes) throws ArityException {
        if (argumentTypes == null || argumentTypes.size() != 1){
            throw new ArityException("Invalid number of arguments for function write_json. Expecting 1 but got " + (argumentTypes == null ? "none" : argumentTypes.size()));
        }
    }
}
