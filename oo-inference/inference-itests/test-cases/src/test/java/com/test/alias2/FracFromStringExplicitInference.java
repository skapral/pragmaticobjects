package com.test.alias2;

import com.pragmaticobjects.oo.inference.api.Inference;
import com.pragmaticobjects.oo.inference.api.Infers;

public @Infers(value = "FracFromStringExplicit", using = FractionInferred.class)
class FracFromStringExplicitInference implements Inference<Fraction> {
    private final String str;

    public FracFromStringExplicitInference(String str) {
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
