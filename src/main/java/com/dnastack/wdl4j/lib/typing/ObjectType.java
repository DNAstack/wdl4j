package com.dnastack.wdl4j.lib.typing;

import lombok.Getter;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ObjectType extends Type {

    private static final ObjectType INSTANCE = new ObjectType(null);
    @Getter
    private Map<String, Type> members;

    public static ObjectType getType() {
        return INSTANCE;
    }

    public static ObjectType getType(Map<String, Type> objectTypes) {
        Objects.requireNonNull(objectTypes, "The objectType of an ObjectType cannot be null");
        return new ObjectType(objectTypes);
    }

    private ObjectType(Map<String, Type> objectTypes) {
        this.members = objectTypes;
    }

    private boolean membersAreCoercible(CoercionOptions options, Map<String, Type> toMembers) {
        if (toMembers == null || members == null) {
            return false;
        }

        Set<String> keys = new HashSet<>(members.keySet());
        Set<String> toKeys = new HashSet<>(toMembers.keySet());

        for (String k : keys) {
            if (!(toKeys.contains(k) && toMembers.get(k).isCoercibleTo(options, members.get(k)))) {
                return false;

            }
        }

        toKeys.removeAll(keys);
        for (String optionalK : toKeys) {
            if (!(toMembers.get(optionalK) instanceof OptionalType)) {
                return false;
            }
        }
        return true;

    }

    @Override
    public boolean isCoercibleTo(CoercionOptions options, @NonNull Type toType) {
        if (toType instanceof StructType) {
            return membersAreCoercible(options, ((StructType) toType).getMembers());
        } else if (toType instanceof ObjectType) {
            if (members == null && ((ObjectType) toType).members == null) {
                return true;
            }

            return membersAreCoercible(options, ((ObjectType) toType).members);
        }
        return super.isCoercibleTo(options, toType);
    }

    @Override
    public String getTypeName() {
        return "Object";
    }
}
