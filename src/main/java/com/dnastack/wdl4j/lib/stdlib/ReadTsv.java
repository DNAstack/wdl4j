package com.dnastack.wdl4j.lib.stdlib;

import com.dnastack.wdl4j.lib.api.EngineFunction;
import com.dnastack.wdl4j.lib.exception.ArityException;
import com.dnastack.wdl4j.lib.exception.TypeCoercionException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.typing.ArrayType;
import com.dnastack.wdl4j.lib.typing.FileType;
import com.dnastack.wdl4j.lib.typing.StringType;
import com.dnastack.wdl4j.lib.typing.Type;

import java.util.List;

public class ReadTsv implements EngineFunction {

    @Override
    public Type evaluateReturnType(List<Type> argumentTypes) throws WdlValidationError {
        Type argumentType = argumentTypes.get(0);
        if (!argumentType.isCoercibleTo(FileType.getType()) && !argumentType.isCoercibleTo(StringType.getType())) {
            throw new TypeCoercionException(
                    "Illegal argument type for read_tsv function, expecting one of [String,File] but got " + argumentType
                            .getTypeName());
        }
        return ArrayType.getType(ArrayType.getType(StringType.getType(), false), false);
    }

    @Override
    public void checkArity(List<Type> argumentTypes) throws ArityException {
        if (argumentTypes == null || argumentTypes.size() != 1) {
            throw new ArityException("Invalid number of arguments for function read_tsv. Expecting 1 but got " + (
                    argumentTypes == null
                    ? "none"
                    : argumentTypes.size()));
        }
    }
}