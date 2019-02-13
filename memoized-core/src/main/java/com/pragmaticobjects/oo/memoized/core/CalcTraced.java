/*
 * The MIT License
 *
 * Copyright 2019 skapral.
 *
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
 */
package com.pragmaticobjects.oo.memoized.core;

import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Traced calculation. Emit trace record with calculation's key when called.
 * 
 * @author skapral
 * @param <S> Object type
 * @param <T> Result type
 */
public class CalcTraced<S, T> implements Calculation<T> {
    private static final Logger LOG = LoggerFactory.getLogger(CalcTraced.class);
    private final S that;
    private final Function<S, T> method;
    private final Calculation<T> delegate;

    /**
     * Ctor.
     * @param that Object
     * @param method Object's method
     * @param delegate Calculation
     */
    public CalcTraced(S that, Function<S, T> method, Calculation<T> delegate) {
        this.that = that;
        this.method = method;
        this.delegate = delegate;
    }

    @Override
    public final T calculate() {
        if(LOG.isTraceEnabled()) {
            LOG.trace("New calculation: {} {}", that.toString(), System.identityHashCode(method));
        }
        return delegate.calculate();
    }
}
