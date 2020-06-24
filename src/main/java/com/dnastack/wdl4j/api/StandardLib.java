package com.dnastack.wdl4j.api;

import com.dnastack.wdl4j.exception.NoSuchReferenceException;
import com.dnastack.wdl4j.exception.WdlValidationError;
import com.dnastack.wdl4j.typing.Type;

import java.util.List;

public interface StandardLib {

    Type evaluateReturnType(String name, List<Type> arguments) throws WdlValidationError;

    EngineFunction getFunction(String name) throws NoSuchReferenceException;

}
