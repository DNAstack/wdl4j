package com.dnastack.wdl4j.lib.typing;

import lombok.NonNull;

public class FloatType extends Type {

    private static final FloatType INSTANCE = new FloatType();

    public static FloatType getType() {
        return INSTANCE;
    }

    private FloatType() {

    }

    @Override
    public boolean isCoercibleTo(@NonNull Type toType) {
        if (toType instanceof StringType){
            return true;
        } else  {
            return super.isCoercibleTo(toType);
        }
    }

    @Override
    public String getTypeName() {
        return "Float";
    }
}
