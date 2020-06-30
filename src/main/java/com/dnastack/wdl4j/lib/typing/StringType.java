package com.dnastack.wdl4j.lib.typing;

import lombok.NonNull;

public class StringType extends Type {

    private static final StringType INSTANCE = new StringType();

    public static StringType getType() {
        return INSTANCE;
    }

    private StringType() {

    }

    @Override
    public boolean isCoercibleTo(CoercionOptions options, @NonNull Type toType) {
        if (toType instanceof FileType || toType instanceof IntType || toType instanceof FloatType) {
            return true;
        }
        return super.isCoercibleTo(options, toType);
    }

    @Override
    public String getTypeName() {
        return "String";
    }
}
