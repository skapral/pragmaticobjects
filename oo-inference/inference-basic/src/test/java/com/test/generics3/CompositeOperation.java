package com.test.generics3;

import com.pragmaticobjects.oo.inference.api.Inference;
import io.vavr.collection.List;
import java.util.function.Function;

public class CompositeOperation extends OperationInferred {
    public CompositeOperation(List<Integer> digits, Function<List<Integer>,Integer> digitsFunction) {
        super(
            new CompositeOperationInference(digits, digitsFunction)
        );
    }
}
