package com.test;

import com.pragmaticobjects.oo.inference.api.Inference;
import com.pragmaticobjects.oo.inference.api.Infers;

import java.util.List;

public @Infers(value = "FracFromList", memoized = true)
class FracFromListInference implements Inference<Fraction> {
    private final List<Integer> list;

    public FracFromListInference(List<Integer> list) {
        this.list = list;
    }

    @Override
    public final Fraction inferredInstance() {
        return new FracFixed(
            list.get(0),
            list.get(1)
        );
    }
}
