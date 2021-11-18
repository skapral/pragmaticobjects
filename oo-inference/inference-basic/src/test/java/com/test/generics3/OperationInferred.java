package com.test.generics3;

import com.pragmaticobjects.oo.inference.api.Inference;

public class OperationInferred implements Operation {
    private final Inference<Operation> inference;

    public OperationInferred(Inference<Operation> inference) {
        this.inference = inference;
    }

    @Override
    public final void execute() {
        inference.inferredInstance().execute();
    }
}
