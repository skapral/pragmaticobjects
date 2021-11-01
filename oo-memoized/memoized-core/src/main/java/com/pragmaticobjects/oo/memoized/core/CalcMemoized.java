/*-
 * ===========================================================================
 * memoized-core
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 Kapralov Sergey
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
package com.pragmaticobjects.oo.memoized.core;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Calculation, calculated more more then one time. Result is memoized.
 * 
 * @author skapral
 * @param <T> calculation result's type
 */
public class CalcMemoized<T> implements Calculation<T> {
    private final AtomicReference<T> memoizeReference;
    private final Calculation<T> calculation;

    /**
     * Ctor.
     * @param memoizeReference reference for memoization
     * @param calculation calculation to memoize
     */
    public CalcMemoized(AtomicReference<T> memoizeReference, Calculation<T> calculation) {
        this.memoizeReference = memoizeReference;
        this.calculation = calculation;
    }
    
    /**
     * Ctor.
     * @param calculation calculation to memoize
     */
    public CalcMemoized(Calculation<T> calculation) {
        this(new AtomicReference<>(), calculation);
    }

    @Override
    public final T calculate() {
        synchronized(memoizeReference) {
            if(memoizeReference.get() == null) {
                T result = calculation.calculate();
                memoizeReference.set(result);
            }
        }
        return memoizeReference.get();
    }
}
