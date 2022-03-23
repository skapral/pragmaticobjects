/*-
 * ===========================================================================
 * memoized-core
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2022 Kapralov Sergey
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

import com.pragmaticobjects.oo.tests.Assertion;
import io.vavr.collection.List;
import org.assertj.core.api.Assertions;

import java.util.Optional;

public class AssertDisposal implements Assertion {
    private final Memory memoryUnderTest;
    private final List<MemoizedCallable<?>> preparationCallables;
    private final MemoizedCallable<?> callableToDispose;
    private final Optional<?> expectedDisposedValue;

    public AssertDisposal(Memory memoryUnderTest, List<MemoizedCallable<?>> preparationCallables, MemoizedCallable<?> callableToDispose, Optional<?> expectedDisposedValue) {
        this.memoryUnderTest = memoryUnderTest;
        this.preparationCallables = preparationCallables;
        this.callableToDispose = callableToDispose;
        this.expectedDisposedValue = expectedDisposedValue;
    }

    @Override
    public final void check() throws Exception {
        preparationCallables.forEach(memoryUnderTest::memoized);
        Optional<?> disposedValue = memoryUnderTest.dispose(callableToDispose);
        Assertions.assertThat(disposedValue).isEqualTo(expectedDisposedValue);
    }
}
