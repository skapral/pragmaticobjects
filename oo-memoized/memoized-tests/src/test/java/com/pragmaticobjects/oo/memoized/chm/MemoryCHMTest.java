/*-
 * ===========================================================================
 * memoized-assertions
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2024 Kapralov Sergey
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


import com.pragmaticobjects.oo.memoized.assertions.AssertCallTimes;
import com.pragmaticobjects.oo.memoized.assertions.AssertDisposal;
import com.pragmaticobjects.oo.memoized.core.MemoizedCallable;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import io.vavr.collection.List;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class TestCallable implements MemoizedCallable<Integer> {
    private final int id;

    public TestCallable(int id) {
        this.id = id;
    }

    @Override
    public final Integer call() {
        return id;
    }
}


class MemoryCHMTest extends TestsSuite {
    public MemoryCHMTest() {
        super(
            new TestCase(
                "Check at most once call",
                new AssertCallTimes(
                    new MemoryCHM(
                        new ConcurrentHashMap<>()
                    ),
                    10,
                    1
                )
            ),
            new TestCase(
                "Check disposal of existing value",
                new AssertDisposal(
                    new MemoryCHM(
                        new ConcurrentHashMap<>()
                    ),
                    List.of(
                        new TestCallable(1),
                        new TestCallable(2),
                        new TestCallable(3)
                    ),
                    new TestCallable(1),
                    Optional.of(1)
                )
            ),
            new TestCase(
                "Check disposal of inexisting value",
                new AssertDisposal(
                    new MemoryCHM(
                        new ConcurrentHashMap<>()
                    ),
                    List.of(
                        new TestCallable(1),
                        new TestCallable(2),
                        new TestCallable(3)
                    ),
                    new TestCallable(4),
                    Optional.empty()
                )
            )
        );
    }
}
