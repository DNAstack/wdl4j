package com.dnastack.wdl4j.lib.typing;

import com.dnastack.wdl4j.lib.Namespace;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import com.dnastack.wdl4j.lib.exception.TypeCoercionException;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public abstract class Type implements WdlElement {

    @Getter
    public int id = -1;

    public abstract String getTypeName();

    public void assertIsCoercibleTo(CoercionOptions options, @NonNull Type toType) throws TypeCoercionException {
        if (!isCoercibleTo(options, toType)) {
            throw new TypeCoercionException("Could not coerce " + getTypeName() + " to target type " + toType.getTypeName());
        }
    }

    public void assertIsCoercibleToSome(CoercionOptions options, @NonNull Type... toType) throws TypeCoercionException {
        List<String> typeNames = new ArrayList<>();
        for (int i = 0; i < toType.length; i++) {
            if (isCoercibleTo(options, toType[i])) {
                return;
            }
            typeNames.add(toType[i].getTypeName());
        }

        throw new TypeCoercionException("Could not coerce " + getTypeName() + " to any of the target types [" + String.join(
                ",",
                typeNames) + "] target type " + getTypeName());

    }

    public boolean isCoercibleTo(CoercionOptions options, @NonNull Type toType) {
        if (equals(toType) || toType instanceof AnyType) {
            return true;
        } else if (toType instanceof OptionalType) {
            OptionalType optionalToType = (OptionalType) toType;
            return this.isCoercibleTo(options, optionalToType.getInnerType());
        }
        return false;
    }

    public void typecheck(Namespace namespace) throws NamespaceException {
        // in general there is nothing that needs to be done
        return;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o == null) {
            return false;
        } else if (o instanceof Type) {
            Type oType = (Type) o;
            return oType.getTypeName().equals(getTypeName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getTypeName().hashCode();
    }

}
