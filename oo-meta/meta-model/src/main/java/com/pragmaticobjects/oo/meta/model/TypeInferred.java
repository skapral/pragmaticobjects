/*-
 * ===========================================================================
 * meta-model
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2020 Kapralov Sergey
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * ============================================================================
 */
package com.pragmaticobjects.oo.meta.model;

import java.util.Collection;

/**
 *
 * @author skapral
 */
public class TypeInferred implements Type {
    private final Inference<Type> inference;

    public TypeInferred(Inference<Type> inference) {
        this.inference = inference;
    }

    @Override
    public final boolean isPrimitive() {
        return inference.inferredInstance().isPrimitive();
    }

    @Override
    public final String getFullName() {
        return inference.inferredInstance().getFullName();
    }

    @Override
    public final String packageName() {
        return inference.inferredInstance().packageName();
    }

    @Override
    public final String name() {
        return inference.inferredInstance().name();
    }

    @Override
    public final Generic generic() {
        return inference.inferredInstance().generic();
    }

    @Override
    public final String declaration() {
        return inference.inferredInstance().declaration();
    }

    @Override
    public final Collection<Type> getImports() {
        return inference.inferredInstance().getImports();
    }
}
