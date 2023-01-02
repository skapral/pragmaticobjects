package com.test.alias3;

import com.pragmaticobjects.oo.inference.api.Inference;
import com.pragmaticobjects.oo.inference.api.MemoizedInference;
import com.pragmaticobjects.oo.memoized.core.Memory;

public class FracFromStringMemoized extends FractionInferred {
    public FracFromStringMemoized(String str, Memory memory) {
        super(
            new MemoizedInference(
                new FracFromStringInferenceMemoized(str),
                memory
            )
        );
    }
}
