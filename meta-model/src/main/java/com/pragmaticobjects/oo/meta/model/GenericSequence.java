package com.pragmaticobjects.oo.meta.model;

import io.vavr.collection.List;

import java.util.Collection;
import java.util.stream.Collectors;

public class GenericSequence implements Generic {
    private final List<Generic> generics;

    public GenericSequence(List<Generic> generics) {
        this.generics = generics;
    }

    @Override
    public final String asString() {
        return generics
                .map(Generic::asString)
                .collect(Collectors.joining(","));
    }

    @Override
    public final Collection<Type> getImports() {
        return generics.flatMap(generic -> generic.getImports()).distinct().asJava();
    }
}
