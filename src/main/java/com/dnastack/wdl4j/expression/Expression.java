package com.dnastack.wdl4j.expression;

import com.dnastack.wdl4j.Namespace;
import com.dnastack.wdl4j.api.WdlElement;
import com.dnastack.wdl4j.exception.WdlValidationError;
import com.dnastack.wdl4j.typing.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public abstract class Expression implements WdlElement {

    @NonNull
    private int id;

    public abstract Type typeCheck(WdlElement target, Namespace namespace) throws WdlValidationError;


}

