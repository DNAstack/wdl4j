package com.dnastack.wdl4j.lib.typing;

import lombok.NonNull;

public class FileType extends Type {

    private static final FileType INSTANCE = new FileType();

    public static FileType getType() {
        return INSTANCE;
    }

    private FileType() {

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
        return "File";
    }
}
