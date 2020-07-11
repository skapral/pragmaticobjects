package com.pragmaticobjects.oo.meta.model;

import java.util.Collection;
import java.util.Collections;

public class GenericEmpty implements Generic {
    @Override
    public final String asString() {
        return "";
    }

    @Override
    public final Collection<Type> getImports() {
        return Collections.emptyList();
    }
}
