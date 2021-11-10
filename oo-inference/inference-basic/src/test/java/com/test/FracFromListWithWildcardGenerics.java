package com.test;

import com.pragmaticobjects.oo.inference.api.Inference;
import com.pragmaticobjects.oo.inference.api.MemoizedInference;
import com.pragmaticobjects.oo.memoized.core.Memory;
import java.util.List;

public class FracFromListWithWildcardGenerics extends FractionInferred {
    public FracFromListWithWildcardGenerics(List<? extends Integer> list, Memory memory) {
        super(
            new MemoizedInference(
                new FracFromListInferenceWithWildcardGenerics(list),
                memory
            )
        );
    }
}
