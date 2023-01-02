package com.test.alias4;

import com.pragmaticobjects.oo.inference.api.Inference;
import com.pragmaticobjects.oo.inference.api.MemoizedInference;
import com.pragmaticobjects.oo.memoized.core.Memory;
import java.util.Random;

public class RandomFrac extends FractionInferred {
    public RandomFrac(Random random, Memory memory) {
        super(
            new MemoizedInference(
                new RandomFracInference(random),
                memory
            )
        );
    }
}
