package com.pragmaticobjects.oo.meta.model;

import java.util.Collection;

public class GenericWildcard implements Generic {
    private final GenericBoundary boundaryType;
    private final Generic boundary;

    public GenericWildcard(GenericBoundary boundaryType, Generic boundary) {
        this.boundaryType = boundaryType;
        this.boundary = boundary;
    }

    @Override
    public final String asString() {
        switch (boundaryType) {
            case EXTENDS: return "? extends " + boundary.asString();
            case SUPER: return "? super " + boundary.asString();
            default: throw new RuntimeException("Unexpected boundary type faced: " + boundaryType);
        }
    }

    @Override
    public final Collection<Type> getImports() {
        return boundary.getImports();
    }
}
