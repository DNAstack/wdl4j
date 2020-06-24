package com.dnastack.wdl4j.api;

import com.dnastack.wdl4j.exception.ArityException;
import com.dnastack.wdl4j.exception.WdlValidationError;
import com.dnastack.wdl4j.typing.Type;

import java.util.List;

public interface EngineFunction {

    Type evaluateReturnType(List<Type> argumentTypes) throws WdlValidationError;

    void checkArity(List<Type> argumentTypes) throws ArityException;
}
