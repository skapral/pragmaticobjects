package com.pragmaticobjects.oo.meta.model;

import java.util.Collection;

public class GenericInferred implements Generic {
    private final Inference<Generic> inference;

    public GenericInferred(Inference<Generic> inference) {
        this.inference = inference;
    }

    @Override
    public final String asString() {
        return inference.inferredInstance().asString();
    }

    @Override
    public final Collection<Type> getImports() {
        return inference.inferredInstance().getImports();
    }
}
