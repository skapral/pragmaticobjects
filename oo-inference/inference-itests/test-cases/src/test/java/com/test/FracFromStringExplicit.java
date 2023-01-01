package com.test;

import com.pragmaticobjects.oo.inference.api.Inference;

public class FracFromStringExplicit extends FractionInferred {
    public FracFromStringExplicit(String str) {
        super(
            new FracFromStringExplicitInference(str)
        );
    }
}
