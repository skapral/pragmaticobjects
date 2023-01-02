package com.test.alias4;

import com.pragmaticobjects.oo.inference.api.Inference;
import com.pragmaticobjects.oo.inference.api.Infers;

import java.util.Random;

public @Infers(value = "RandomFrac", memoized = true) class RandomFracInference implements Inference<Fraction> {
    private final Random random;

    public RandomFracInference(Random random) {
        this.random = random;
    }

    @Override
    public final Fraction inferredInstance() {
        return new FracFixed(
            random.nextInt(),
            random.nextInt()
        );
    }
}
