package com.test.inferred2;

import com.pragmaticobjects.oo.inference.api.Inference;
import com.test.inferred1.Fraction;

public class FractionInferred implements Fraction {
    private final Inference<Fraction> inference;

    public FractionInferred(Inference<Fraction> inference) {
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
