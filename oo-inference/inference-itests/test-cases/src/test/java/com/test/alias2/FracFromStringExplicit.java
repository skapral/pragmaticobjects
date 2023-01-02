package com.test.alias2;


public class FracFromStringExplicit extends FractionInferred {
    public FracFromStringExplicit(String str) {
        super(
            new FracFromStringExplicitInference(str)
        );
    }
}
