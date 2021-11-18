package com.test.generics3;

import com.pragmaticobjects.oo.inference.api.Inference;
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
