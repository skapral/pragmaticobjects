package com.external;

import com.pragmaticobjects.oo.inference.api.Inference;

public class FracInferred implements Fraction {
    private final Inference<Fraction> inference;

    public FracInferred(Inference<Fraction> inference) {
        this.inference = inference;
    }

    @Override
    public final int numerator() {
        return inference.inferredInstance().numerator();
    }

    @Override
    public final int denumenator() {
        return inference.inferredInstance().denumenator();
    }
}
