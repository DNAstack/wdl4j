package com.dnastack.wdl4j.lib.expression;

import com.dnastack.wdl4j.lib.Declaration;
import com.dnastack.wdl4j.lib.Namespace;
import com.dnastack.wdl4j.lib.api.NamedElement;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.exception.IllegalReferenceException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.typing.Type;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class VariableReference extends Expression implements NamedElement {

    String name;

    public VariableReference(@NonNull String name, @NonNull int id) {
        super(id);
        this.name = name;
    }

    @Override
    public Type typeCheck(WdlElement target, Namespace namespace) throws WdlValidationError {
        Declaration declaration = namespace.getDeclaration(name);
        if (target.getId() < declaration.getId()) {
            throw new IllegalReferenceException("Variable \"" + declaration.getName() + "\" referenced prior to being defined");
        }
        return declaration.getDeclType();

    }
}
