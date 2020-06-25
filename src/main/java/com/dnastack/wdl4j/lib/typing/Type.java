package com.dnastack.wdl4j.lib.typing;

import com.dnastack.wdl4j.lib.api.WdlElement;
import lombok.Getter;
import lombok.NonNull;

public abstract class Type implements WdlElement {

    public abstract String getTypeName();


    public boolean isCoercibleTo(@NonNull Type toType) {
        if (equals(toType) || toType instanceof AnyType) {
            return true;
        } else if (toType instanceof OptionalType){
            OptionalType optionalToType = (OptionalType) toType;
            return this.isCoercibleTo(optionalToType.getInnerType());
        }
        return false;
    }

    @Getter
    public int id = -1;

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
