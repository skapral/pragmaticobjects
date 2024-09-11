/*-
 * ===========================================================================
 * equivalence-itests.test-cases
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
package com.pragmaticobjects.oo.equivalence.itests.records;

import com.pragmaticobjects.oo.equivalence.assertions.*;
import com.pragmaticobjects.oo.equivalence.itests.classes.Counter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TestRecords extends TestsSuite {
    private static final Map<String, Object> CONSTANT = new HashMap<>();

    public TestRecords() {
        super(
            new TestCase(
                "Test record with non-equivalent fields",
                new AssertCombined(
                    new AssertEqualsResult(
                        new TRecord<>(CONSTANT),
                        new TRecord<>(CONSTANT),
                        true
                    ),
                    new AssertEqualsResult(
                        new TRecord<>(new HashMap<>()),
                        new TRecord<>(new HashMap<>()),
                        false
                    )
                )
            ),
            new TestCase(
                "Test record with equivalent fields",
                new AssertCombined(
                    new AssertEqualsResult(
                        new TRecord<>(new Counter("A")),
                        new TRecord<>(new Counter("A")),
                        true
                    ),
                    new AssertEqualsResult(
                        new TRecord<>(new Counter("A")),
                        new TRecord<>(new Counter("B")),
                        false
                    )
                )
            ),
            new TestCase(
                "record with hinted atributtes",
                new AssertTwoObjectsEquality(
                    new TimeRange(
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2030, 1, 1)
                    ),
                    new TimeRange(
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2030, 1, 1)
                    ),
                    true
                )
            )
        );
    }
}
