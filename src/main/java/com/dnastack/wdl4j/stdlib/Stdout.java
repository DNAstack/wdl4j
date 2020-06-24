package com.dnastack.wdl4j.stdlib;

import com.dnastack.wdl4j.api.EngineFunction;
import com.dnastack.wdl4j.exception.ArityException;
import com.dnastack.wdl4j.typing.FileType;
import com.dnastack.wdl4j.typing.Type;

import java.util.List;

public class Stdout implements EngineFunction {

    @Override
    public Type evaluateReturnType(List<Type> argumentTypes) {
        return FileType.getType();
    }

    @Override
    public void checkArity(List<Type> argumentTypes) throws ArityException {
        if (argumentTypes != null && argumentTypes.size() > 0){
            throw new ArityException("Invalid number of arguments for function stdout. Expecting none but got " + argumentTypes.size());
        }
    }
}
