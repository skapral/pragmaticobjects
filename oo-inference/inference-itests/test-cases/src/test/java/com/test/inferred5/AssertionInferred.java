package com.test.inferred5;

import com.pragmaticobjects.oo.inference.api.Inference;

public class AssertionInferred implements Assertion {
    private final Inference<Assertion> inference;

    public AssertionInferred(Inference<Assertion> inference) {
        this.inference = inference;
    }

    @Override
    public final void check() throws Exception {
        inference.inferredInstance().check();
    }

}
