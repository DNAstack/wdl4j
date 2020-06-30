package com.dnastack.wdl4j.lib.typing;

import lombok.NonNull;

public class BooleanType extends Type {

    private static final BooleanType INSTANCE = new BooleanType();

    public static BooleanType getType() {
        return INSTANCE;
    }

    private BooleanType() {
    }

    @Override
    public boolean isCoercibleTo(CoercionOptions options, @NonNull Type toType) {
        if (toType instanceof StringType) {
            return true;
        } else {
            return super.isCoercibleTo(options, toType);
        }
    }

    @Override
    public String getTypeName() {
        return "Boolean";
    }
}
