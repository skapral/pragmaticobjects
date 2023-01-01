package com.test;

import com.pragmaticobjects.oo.inference.api.Inference;
import com.pragmaticobjects.oo.inference.api.Infers;

public @Infers(value = "FracFromStringMemoized", memoized = true)
class FracFromStringInferenceMemoized implements Inference<Fraction> {
    private final String str;

    public FracFromStringInferenceMemoized(String str) {
        this.str = str;
    }

    @Override
    public final Fraction inferredInstance() {
        return new FracFixed(
            Integer.parseInt(str.split("/")[0]),
            Integer.parseInt(str.split("/")[1])
        );
    }
}
