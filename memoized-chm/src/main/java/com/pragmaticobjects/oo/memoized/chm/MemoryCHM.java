/*-
 * ===========================================================================
 * memoized-chm
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
package com.pragmaticobjects.oo.memoized.chm;

import com.pragmaticobjects.oo.memoized.core.CalcDefault;
import com.pragmaticobjects.oo.memoized.core.Memory;
import com.pragmaticobjects.oo.memoized.core.Calculation;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Memory region, backed by {@link ConcurrentHashMap} instance
 * 
 * @author skapral
 */
public class MemoryCHM implements Memory {
    private static final ConcurrentHashMap<Object, Calculation> DEFAULT = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Object, Calculation> memoizedObjects;

    /**
     * Ctor.
     * 
     * @param memoizedObjects hash map for memoized calculations
     */
    public MemoryCHM(ConcurrentHashMap<Object, Calculation> memoizedObjects) {
        this.memoizedObjects = memoizedObjects;
    }

    /**
     * Ctor.
     */
    public MemoryCHM() {
        this(DEFAULT);
    }

    @Override
    public final <S, T> Calculation<T> memoizedCalculation(S that, Object key, Supplier<T> methodBody) {
        return memoizedObjects.computeIfAbsent(
            Arrays.asList(that, key),
            mref -> new CalcDefault(that, key, methodBody)
        );
    }

    @Override
    public final void clean() {
        memoizedObjects.clear();
    }
}
