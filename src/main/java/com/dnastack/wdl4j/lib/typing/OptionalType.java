package com.dnastack.wdl4j.lib.typing;

import com.dnastack.wdl4j.lib.LanguageLevel;
import com.dnastack.wdl4j.lib.Namespace;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OptionalType extends Type {

    @NonNull
    private final Type innerType;

    public static OptionalType getType(Type innerType) {
        return new OptionalType(innerType);
    }

    @Override
    public boolean isCoercibleTo(CoercionOptions options, @NonNull Type toType) {
        if (options.getLanguageLevel() != null && options.getLanguageLevel().equals(LanguageLevel.WDL_DRAFT_2)) {
            return innerType.isCoercibleTo(options, toType);
        } else {
            if (toType instanceof OptionalType) {
                return innerType.isCoercibleTo(options, ((OptionalType) toType).innerType);
            } else {
                return false;
            }
        }
    }

    @Override
    public void typecheck(Namespace namespace) throws NamespaceException {
        innerType.typecheck(namespace);
    }

    @Override
    public String getTypeName() {
        return innerType.getTypeName() + "?";
    }
}
