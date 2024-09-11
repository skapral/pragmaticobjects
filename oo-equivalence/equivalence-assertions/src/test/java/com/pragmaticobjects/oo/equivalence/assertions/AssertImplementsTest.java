/*-
 * ===========================================================================
 * equivalence-assertions
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
package com.pragmaticobjects.oo.equivalence.assertions;

public class AssertImplementsTest extends TestsSuite {
    public AssertImplementsTest() {
        super(
            new TestCase(
                "direct implementor",
                new AssertCombined(
                    new AssertAssertionPasses(
                        new AssertImplements(
                            Ao.class,
                            A.class
                        )
                    ),
                    new AssertAssertionFails(
                        new AssertImplements(
                            Ao.class,
                            B.class
                        )
                    )
                )
            ),
            new TestCase(
                "indirect implementor",
                new AssertCombined(
                    new AssertAssertionFails(
                        new AssertImplements(
                            Bo.class,
                            A.class
                        )
                    ),
                    new AssertAssertionPasses(
                        new AssertImplements(
                            Bo.class,
                            A.class,
                            true
                        )
                    )
                )
            )
        );
    }
}

interface A {
}

class Ao implements A {
}

interface B extends A {
}

class Bo implements B {
}
