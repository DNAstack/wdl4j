package com.dnastack.wdl4j.lib.api;

import com.dnastack.wdl4j.lib.exception.NoSuchReferenceException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.typing.CoercionOptions;
import com.dnastack.wdl4j.lib.typing.Type;

import java.util.List;

public interface StandardLib {

    Type evaluateReturnType(String name, List<Type> arguments, CoercionOptions options) throws WdlValidationError;

    EngineFunction getFunction(String name) throws NoSuchReferenceException;

}
