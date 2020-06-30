package com.dnastack.wdl4j.lib.typing;

import com.dnastack.wdl4j.lib.LanguageLevel;
import lombok.NonNull;

public class FloatType extends Type {

    private static final FloatType INSTANCE = new FloatType();

    public static FloatType getType() {
        return INSTANCE;
    }

    private FloatType() {

    }

    @Override
    public boolean isCoercibleTo(CoercionOptions options, @NonNull Type toType) {
        if (toType instanceof IntType && options != null && options.getLanguageLevel()
                                                                   .equals(LanguageLevel.WDL_DRAFT_2)) {
            return true;
        } else if (toType instanceof StringType) {
            return true;
        } else {
            return super.isCoercibleTo(options, toType);
        }
    }

    @Override
    public String getTypeName() {
        return "Float";
    }
}
