package com.test.generics1;

import com.pragmaticobjects.oo.inference.api.MemoizedInference;
import com.pragmaticobjects.oo.memoized.core.Memory;
import java.util.List;

public class FracFromList extends FractionInferred {
    public FracFromList(List<Integer> list, Memory memory) {
        super(
            new MemoizedInference(
                new FracFromListInference(list),
                memory
            )
        );
    }
}
