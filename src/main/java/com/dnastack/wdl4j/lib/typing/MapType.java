package com.dnastack.wdl4j.lib.typing;

import com.dnastack.wdl4j.lib.Namespace;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MapType extends Type {

    @NonNull
    private final Type keyType;
    @NonNull
    private final Type valueType;

    private final List<String> literalNames;

    public static MapType getType(Type keyType, Type valueType) {
        Objects.requireNonNull(keyType, "keyType of a MapType cannot be null");
        Objects.requireNonNull(valueType, "valueType of a MapType cannot be null");
        return new MapType(keyType, valueType, null);
    }

    public static MapType getType(Type keyType, Type valueType, List<String> literalNames) {
        Objects.requireNonNull(keyType, "keyType of a MapType cannot be null");
        Objects.requireNonNull(valueType, "valueType of a MapType cannot be null");
        return new MapType(keyType, valueType, literalNames);
    }

    @Override
    public void typecheck(Namespace namespace) throws NamespaceException {
        keyType.typecheck(namespace);
        valueType.typecheck(namespace);
    }

    @Override
    public boolean isCoercibleTo(CoercionOptions options, @NonNull Type toType) {
        if (toType instanceof MapType) {
            MapType mapToType = (MapType) toType;
            return keyType.isCoercibleTo(options, mapToType) && valueType.isCoercibleTo(options, valueType);
        } else if (toType instanceof StructType && literalNames != null && !literalNames.isEmpty()) {
            StructType structToType = (StructType) toType;
            Map<String, Type> members = structToType.getMembers();
            if (members == null || members.isEmpty()) {
                return false;
            } else if (!members.keySet().containsAll(literalNames)) {
                return false;
            }

            Set<String> toKeys = new HashSet<>(members.keySet());

            for (String k : literalNames) {
                if (!(toKeys.contains(k) && members.get(k).isCoercibleTo(options, valueType))) {
                    return false;
                }
            }

            toKeys.removeAll(literalNames);
            for (String optionalK : toKeys) {
                if (!(members.get(optionalK) instanceof OptionalType)) {
                    return false;
                }
            }
            return true;

        }

        return super.isCoercibleTo(options, toType);
    }

    @Override
    public String getTypeName() {
        return "Map[" + keyType.getTypeName() + "," + valueType.getTypeName() + "]";
    }

}
