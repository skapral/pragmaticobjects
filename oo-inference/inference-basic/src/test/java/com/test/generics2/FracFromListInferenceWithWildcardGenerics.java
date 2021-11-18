package com.test.generics2;

import com.pragmaticobjects.oo.inference.api.Inference;
import com.pragmaticobjects.oo.inference.api.Infers;


import java.util.List;

public @Infers(value = "FracFromListWithWildcardGenerics", memoized = true)
class FracFromListInferenceWithWildcardGenerics implements Inference<Fraction> {
    private final List<? extends Integer> list;

    public FracFromListInferenceWithWildcardGenerics(List<? extends Integer> list) {
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
