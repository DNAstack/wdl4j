package com.dnastack.wdl4j.lib.typing;

import com.dnastack.wdl4j.lib.Namespace;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import lombok.*;

import java.util.Map;
import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StructType extends Type {

    @NonNull
    private final String name;

    @Setter
    private Map<String, Type> members;

    public static StructType getType(String name) {
        Objects.requireNonNull(name, "The name of a struct type cannot be null");
        return new StructType(name, null);
    }

    public static StructType getType(String name, Map<String, Type> members) {
        Objects.requireNonNull(name, "The name of a struct type cannot be null");
        return new StructType(name, members);
    }

    @Override
    public void typecheck(Namespace namespace) throws NamespaceException {
        //Make sure that the struct is in the current namespace
        namespace.getStruct(name);
    }

    @Override
    public boolean isCoercibleTo(CoercionOptions options, @NonNull Type toType) {
        if (toType instanceof StructType) {
            StructType structToType = (StructType) toType;
            if (structToType.name.equals(name)) {
                return true;
            } else if (structToType.getMembers() != null && members != null) {
                return structToType.members.equals(members);
            } else {
                return false;
            }
        }
        return super.isCoercibleTo(options, toType);
    }

    @Override
    public String getTypeName() {
        return name;
    }
}
