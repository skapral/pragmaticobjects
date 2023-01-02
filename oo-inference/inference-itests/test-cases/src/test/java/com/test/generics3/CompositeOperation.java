package com.test.generics3;

import java.util.function.Function;
import io.vavr.collection.List;
import java.util.UUID;

public class CompositeOperation extends OperationInferred {
    public CompositeOperation(Function<List<UUID>,Integer> digitsFunction) {
        super(
            new CompositeOperationInference(digitsFunction)
        );
    }
}
