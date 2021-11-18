package com.test.generics3;

import com.pragmaticobjects.oo.inference.api.Inference;
import com.pragmaticobjects.oo.inference.api.Infers;
import io.vavr.collection.List;

import java.util.function.Function;

@Infers("CompositeOperation")
public class CompositeOperationInference implements Inference<Operation> {
    private final List<Integer> digits;
    private final Function<List<Integer>, Integer> digitsFunction;

    public CompositeOperationInference(List<Integer> digits, Function<List<Integer>, Integer> digitsFunction) {
        this.digits = digits;
        this.digitsFunction = digitsFunction;
    }

    @Override
    public final Operation inferredInstance() {
        return () -> digitsFunction.apply(digits);
    }
}
