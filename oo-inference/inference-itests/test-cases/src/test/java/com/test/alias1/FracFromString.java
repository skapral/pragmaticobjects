package com.test.alias1;

import com.pragmaticobjects.oo.inference.api.Inference;

public class FracFromString extends FractionInferred {
    public FracFromString(String str) {
        super(
            new FracFromStringInference(str)
        );
    }
}
