package com.dnastack.wdl4j.lib.api;

import com.dnastack.wdl4j.lib.exception.ArityException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.typing.CoercionOptions;
import com.dnastack.wdl4j.lib.typing.Type;

import java.util.List;

public interface EngineFunction {

    Type evaluateReturnType(List<Type> argumentTypes, CoercionOptions options) throws WdlValidationError;

    void checkArity(List<Type> argumentTypes) throws ArityException;
}
