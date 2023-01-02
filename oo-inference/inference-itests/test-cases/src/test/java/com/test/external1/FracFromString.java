package com.test.external1;

import com.pragmaticobjects.oo.inference.api.Inference;
import com.external.FractionInferred;
import com.external.Fraction;

public class FracFromString extends FractionInferred {
    public FracFromString(String str) {
        super(
            new FracFromStringInference(str)
        );
    }
}
