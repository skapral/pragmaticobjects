package com.test.external1;

import com.pragmaticobjects.oo.inference.api.Inference;
import com.test.Fraction;
import com.external.FractionInferred;

public class FracFromString extends FractionInferred {
    public FracFromString(String str) {
        super(
            new FracFromStringInference(str)
        );
    }
}
