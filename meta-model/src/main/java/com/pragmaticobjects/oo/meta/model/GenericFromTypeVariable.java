package com.pragmaticobjects.oo.meta.model;

import javax.lang.model.type.TypeVariable;
import java.util.Collection;
import java.util.Collections;

public class GenericFromTypeVariable implements Generic {
    private final TypeVariable variable;

    public GenericFromTypeVariable(TypeVariable variable) {
        this.variable = variable;
    }

    @Override
    public final String asString() {
        return variable.toString();

    }

    @Override
    public final Collection<Type> getImports() {
        return Collections.emptyList();
    }
}
