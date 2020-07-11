package com.pragmaticobjects.oo.meta.model;

import java.util.Collection;
import java.util.Collections;

public class GenericType implements Generic {
    private final Type type;

    public GenericType(Type type) {
        this.type = type;
    }

    @Override
    public final String asString() {
        return type.name();
    }

    @Override
    public final Collection<Type> getImports() {
        return Collections.singleton(type);
    }
}
