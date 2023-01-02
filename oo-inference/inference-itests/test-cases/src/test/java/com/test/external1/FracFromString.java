package com.test.external1;

import com.external.FractionInferred;

public class FracFromString extends FractionInferred {
    public FracFromString(String str) {
        super(
            new FracFromStringInference(str)
        );
    }
}
