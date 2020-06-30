package com.dnastack.wdl4j.lib.typing;

import lombok.NonNull;

public class AnyType extends Type {

    private static final AnyType INSTANCE = new AnyType();

    public static AnyType getType() {
        return INSTANCE;
    }

    private AnyType() {

    }

    @Override
    public boolean isCoercibleTo(CoercionOptions options, @NonNull Type toType) {
        return true;
    }

    @Override
    public String getTypeName() {
        return "Any";
    }
}
