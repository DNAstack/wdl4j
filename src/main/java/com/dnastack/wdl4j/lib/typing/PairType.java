package com.dnastack.wdl4j.lib.typing;

import com.dnastack.wdl4j.lib.Namespace;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PairType extends Type {

    @NonNull
    private final Type leftType;
    @NonNull
    private final Type rightType;

    public static PairType getType(Type leftType, Type rightType) {
        Objects.requireNonNull(leftType, "The leftType of a pair cannot be null");
        Objects.requireNonNull(rightType, "The rightType of a pair cannot be null");
        return new PairType(leftType, rightType);

    }

    @Override
    public void typecheck(Namespace namespace) throws NamespaceException {
        leftType.typecheck(namespace);
        rightType.typecheck(namespace);
    }

    @Override
    public boolean isCoercibleTo(CoercionOptions options, @NonNull Type toType) {
        if (toType instanceof PairType) {
            return rightType.isCoercibleTo(options, ((PairType) toType).rightType) && leftType.isCoercibleTo(options,
                                                                                                             ((PairType) toType).leftType);
        } else {
            return super.isCoercibleTo(options, toType);
        }
    }

    @Override
    public String getTypeName() {
        return "Pair[" + leftType.getTypeName() + "," + rightType.getTypeName() + "]";
    }
}
