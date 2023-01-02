package com.test.external1;

import com.pragmaticobjects.oo.inference.api.Inference;
import com.pragmaticobjects.oo.inference.api.Infers;
import com.external.Fraction;
import com.external.FracFixed;
import com.external.FractionInferred;

public @Infers(value = "FracFromString", using = FractionInferred.class)
class FracFromStringInference implements Inference<Fraction> {
    private final String str;

    public FracFromStringInference(String str) {
        this.str = str;
    }

    @Override
    public final Fraction inferredInstance() {
        return new FracFixed(
            Integer.parseInt(str.split("/")[0]),
            Integer.parseInt(str.split("/")[1])
        );
    }
}
