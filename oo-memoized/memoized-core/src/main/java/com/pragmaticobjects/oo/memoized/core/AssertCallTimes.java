/*-
 * ===========================================================================
 * memoized-core
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2021 Kapralov Sergey
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
import org.assertj.core.api.Assertions;

import java.util.concurrent.atomic.AtomicInteger;

// NOTE: equals, hashCode and toString are implicitly generated
// by equivalence-maven-plugin
class TestCallable implements MemoizedCallable {
    private final AtomicInteger counter;

    public TestCallable(AtomicInteger counter) {
        this.counter = counter;
    }

    @Override
    public final Object call() {
        counter.incrementAndGet();
        return null;
    }
}

public class AssertCallTimes implements Assertion {
    private final Memory memory;
    private final int callNums;
    private final int expectedNumCalls;

    public AssertCallTimes(Memory memory, int callNums, int expectedNumCalls) {
        this.memory = memory;
        this.callNums = callNums;
        this.expectedNumCalls = expectedNumCalls;
    }

    @Override
    public final void check() throws Exception {
        AtomicInteger counter = new AtomicInteger();
        for(int i = 0; i < callNums; i++) {
            memory.memoized(new TestCallable(counter));
        }
        Assertions.assertThat(counter).hasValue(expectedNumCalls);
    }
}
