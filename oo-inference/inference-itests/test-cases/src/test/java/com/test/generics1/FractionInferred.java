package com.test.generics1;

import com.pragmaticobjects.oo.inference.api.Inference;


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

