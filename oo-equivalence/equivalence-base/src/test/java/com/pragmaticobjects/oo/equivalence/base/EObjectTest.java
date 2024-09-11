/*-
 * ===========================================================================
 * equivalence-base
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
package com.pragmaticobjects.oo.equivalence.base;

import com.pragmaticobjects.oo.equivalence.assertions.*;
import com.pragmaticobjects.oo.equivalence.base.testobjects.ETuple;

import java.time.LocalDate;

/**
 * @author skapral
 */
public class EObjectTest extends TestsSuite {
    private static final ETuple CONSTANT = new ETuple();
    private static final Object CONSTANT_OBJ = new Object();
    private static final EObject[] CONSTANT_ARRAY = new EObject[]{};

    public EObjectTest() {
        super(
            new TestCase(
                "Primitive equality",
                new AssertTwoObjectsEquality(
                    new ETuple((byte) 4, (short) 8, 15, 16l, 23f, 42d, true),
                    new ETuple((byte) 4, (short) 8, 15, 16l, 23f, 42d, true),
                    true
                )
            ),
            new TestCase(
                "Primitive unequality",
                new AssertTwoObjectsEquality(
                    new ETuple((byte) 4, (short) 8, 15, 16l, 23f, 42d, true),
                    new ETuple(4, 8, 15, 16, 23, 42, true),
                    false
                )
            ),
            new TestCase(
                "Composition equality",
                new AssertTwoObjectsEquality(
                    new ETuple(new ETuple(new ETuple(42))),
                    new ETuple(new ETuple(new ETuple(42))),
                    true
                )
            ),
            new TestCase(
                "Composition unequality",
                new AssertTwoObjectsEquality(
                    new ETuple(new ETuple(new ETuple(42))),
                    new ETuple(new ETuple(42)),
                    false
                )
            ),
            new TestCase(
                "Attributes count unequality",
                new AssertTwoObjectsEquality(
                    new ETuple(new ETuple(42, 42)),
                    new ETuple(new ETuple(42)),
                    false
                )
            ),
            new TestCase(
                "Comparing with null",
                new AssertTwoObjectsEquality(
                    new ETuple(),
                    null,
                    false
                )
            ),
            new TestCase(
                "Comparing with non-EObject",
                new AssertTwoObjectsEquality(
                    new ETuple(),
                    new Object(),
                    false
                )
            ),
            new TestCase(
                "Identity with nulls",
                new AssertCombined(
                    new AssertTwoObjectsEquality(
                        new ETuple((Object) null),
                        new ETuple((Object) null),
                        true
                    ),
                    new AssertTwoObjectsEquality(
                        new ETuple((Object) null),
                        new ETuple(42),
                        false
                    )
                )
            ),
            new TestCase(
                "Non-EObject identity",
                new AssertCombined(
                    new AssertTwoObjectsEquality(
                        new ETuple(new Object()),
                        new ETuple(new Object()),
                        false
                    ),
                    new AssertTwoObjectsEquality(
                        new ETuple(CONSTANT_OBJ),
                        new ETuple(CONSTANT_OBJ),
                        true
                    )
                )
            ),
            new TestCase(
                "enforced equivalence",
                new AssertCombined(
                    new AssertTwoObjectsEquality(
                        new ETuple(
                            LocalDate.of(2022, 12, 1)
                        ),
                        new ETuple(
                            LocalDate.of(2022, 12, 1)
                        ),
                        false
                    ),
                    new AssertTwoObjectsEquality(
                        new ETuple(
                            new NaturallyEquivalent(
                                LocalDate.of(2022, 12, 1)
                            )
                        ),
                        new ETuple(
                            new NaturallyEquivalent(
                                LocalDate.of(2022, 12, 1)
                            )
                        ),
                        true
                    )
                )
            ),
            new TestCase(
                "Arrays identity",
                new AssertCombined(
                    new AssertTwoObjectsEquality(
                        new ETuple((Object) new EObject[]{}),
                        new ETuple((Object) new EObject[]{}),
                        false
                    ),
                    new AssertTwoObjectsEquality(
                        new ETuple((Object) CONSTANT_ARRAY),
                        new ETuple((Object) CONSTANT_ARRAY),
                        true
                    )
                )
            ),
            new TestCase(
                "Equal by reference",
                new AssertTwoObjectsEquality(
                    CONSTANT,
                    CONSTANT,
                    true
                )
            ),
            new TestCase(
                "HashCodes",
                new AssertCombined(
                    new AssertObjectHashCode(new ETuple(42), 152399067),
                    new AssertObjectHashCode(new ETuple(new ETuple(42)), 304798092),
                    new AssertObjectHashCode(new ETuple(new Object()), 1216276036),
                    new AssertObjectHashCode(new ETuple((Object) null), 152399025)
                )
            ),
            new TestCase(
                "toString",
                new AssertCombined(
                    new AssertObjectToString(new ETuple(42), "ETuple(42)"),
                    new AssertObjectToStringMatchesRegex(new ETuple(new Object()), "ETuple\\(java\\.lang\\.Object\\#\\p{Digit}+\\)"),
                    new AssertObjectToString(new ETuple(TestEnum.B), "ETuple(B)")
                )
            )
        );
    }
}
