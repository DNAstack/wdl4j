package com.dnastack.wdl4j.lib.typing;

import com.dnastack.wdl4j.lib.Namespace;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArrayType extends Type {

    private final static Set<ArrayType> INSTANCES = new HashSet<>();
    @NonNull
    private final Type innerType;
    @NonNull
    private final Boolean nonEmpty;

    public static ArrayType getType(Type innerType, boolean nonEmpty) {
        return new ArrayType(innerType, nonEmpty);
    }

    public static ArrayType getType(Type innerType) {
        return new ArrayType(innerType, false);
    }

    @Override
    public void typecheck(Namespace namespace) throws NamespaceException {
        innerType.typecheck(namespace);
    }

    @Override
    public boolean isCoercibleTo(CoercionOptions options, @NonNull Type toType) {
        if (toType instanceof ArrayType) {
            ArrayType arrayToType = (ArrayType) toType;
            return (!arrayToType.nonEmpty || nonEmpty) && innerType.isCoercibleTo(options, arrayToType.innerType);
        } else if (toType instanceof StringType) {
            return innerType == null || innerType.isCoercibleTo(options, StringType.getType());
        }
        return super.isCoercibleTo(options, toType);
    }

    @Override
    public String getTypeName() {
        return "Array[" + innerType.getTypeName() + "]" + (nonEmpty ? "+" : "");
    }
}