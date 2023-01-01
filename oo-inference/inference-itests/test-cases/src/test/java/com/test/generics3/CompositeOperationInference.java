package com.test.generics3;

import com.pragmaticobjects.oo.inference.api.Inference;
import com.pragmaticobjects.oo.inference.api.Infers;
import io.vavr.collection.List;

import java.util.UUID;
import java.util.function.Function;

@Infers("CompositeOperation")
public class CompositeOperationInference implements Inference<Operation> {
    private final Function<List<UUID>, Integer> digitsFunction;

    public CompositeOperationInference(Function<List<UUID>, Integer> digitsFunction) {
        this.digitsFunction = digitsFunction;
    }

    @Override
    public final Operation inferredInstance() {
        return () -> digitsFunction.apply(List.of(UUID.randomUUID()));
    }
}
