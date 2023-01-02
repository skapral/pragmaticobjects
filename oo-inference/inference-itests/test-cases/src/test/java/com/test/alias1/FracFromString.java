package com.test.alias1;


public class FracFromString extends FractionInferred {
    public FracFromString(String str) {
        super(
            new FracFromStringInference(str)
        );
    }
}
