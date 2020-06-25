package com.dnastack.wdl4j.lib.stdlib;

import com.dnastack.wdl4j.lib.api.EngineFunction;
import com.dnastack.wdl4j.lib.exception.ArityException;
import com.dnastack.wdl4j.lib.typing.FileType;
import com.dnastack.wdl4j.lib.typing.Type;

import java.util.List;

public class Stderr implements EngineFunction {

    @Override
    public Type evaluateReturnType(List<Type> argumentTypes) {
        return FileType.getType();
    }

    @Override
    public void checkArity(List<Type> argumentTypes) throws ArityException {
        if (argumentTypes != null && argumentTypes.size() > 0){
            throw new ArityException("Invalid number of arguments for function stderr. Expecting none but got " + argumentTypes.size());
        }
    }
}
