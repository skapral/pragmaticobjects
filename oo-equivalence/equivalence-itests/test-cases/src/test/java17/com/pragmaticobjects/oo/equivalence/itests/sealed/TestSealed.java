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
package com.pragmaticobjects.oo.equivalence.itests.sealed;

import com.pragmaticobjects.oo.equivalence.assertions.AssertCombined;
import com.pragmaticobjects.oo.equivalence.assertions.AssertEqualsResult;
import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;

public class TestSealed extends TestsSuite {
    public TestSealed() {
        super(
            new TestCase(
                "Sealed interface can have equivalence-compliant implementations",
                new AssertCombined(
                    new AssertEqualsResult(
                        new Just<>(1),
                        new Just<>(1),
                        true
                    ),
                    new AssertEqualsResult(
                        new Just<>(1),
                        new Just<>(2),
                        false
                    ),
                    new AssertEqualsResult(
                        new Just<>(42),
                        new Just42(),
                        true
                    ),
                    new AssertEqualsResult(
                        new Nothingness<>(),
                        new Nothingness<>(),
                        true
                    ),
                    new AssertEqualsResult(
                        new Nothingness<>(),
                        new Just<>(1),
                        false
                    )
                )
            )
        );
    }
}

