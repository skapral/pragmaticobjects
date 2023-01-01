/*-
 * ===========================================================================
 * equivalence-base
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2023 Kapralov Sergey
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
package com.pragmaticobjects.oo.equivalence.base;

import com.pragmaticobjects.oo.equivalence.assertions.AssertCombined;
import com.pragmaticobjects.oo.equivalence.assertions.AssertTwoObjectsEquality;
import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;
import com.pragmaticobjects.oo.equivalence.base.testobjects.ETuple;
import com.pragmaticobjects.oo.equivalence.base.testobjects.ETuple24;
import com.pragmaticobjects.oo.equivalence.base.testobjects.ETuple42;
import com.pragmaticobjects.oo.equivalence.base.testobjects.ETuple42_2;
import com.pragmaticobjects.oo.equivalence.base.testobjects.ETuple_2;

/**
 *
 * @author skapral
 */
public class EObjectSubtypesTest extends TestsSuite {
    public EObjectSubtypesTest() {
        super(
            new TestCase(
                "subtype vs equivalent base type",
                new AssertCombined(
                    new AssertTwoObjectsEquality(
                        new ETuple42(),
                        new ETuple(42),
                        true
                    )
                )
            ),
            new TestCase(
                "subtype vs non-equivalent base type",
                new AssertCombined(
                    new AssertTwoObjectsEquality(
                        new ETuple42(),
                        new ETuple(24),
                        false
                    )
                )
            ),
            new TestCase(
                "two equivalent subtypes",
                new AssertCombined(
                    new AssertTwoObjectsEquality(
                        new ETuple42(),
                        new ETuple42_2(),
                        true
                    )
                )
            ),
            new TestCase(
                "two non-equivalent subtypes",
                new AssertCombined(
                    new AssertTwoObjectsEquality(
                        new ETuple42(),
                        new ETuple24(),
                        false
                    )
                )
            ),
            new TestCase(
                "two non-subtypes",
                new AssertCombined(
                    new AssertTwoObjectsEquality(
                        new ETuple(42),
                        new ETuple_2(42),
                        false
                    )
                )
            )
        );
    }
}
