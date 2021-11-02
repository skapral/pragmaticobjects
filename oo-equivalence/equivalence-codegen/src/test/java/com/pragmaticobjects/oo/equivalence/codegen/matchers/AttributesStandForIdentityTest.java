/*-
 * ===========================================================================
 * equivalence-codegen
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
package com.pragmaticobjects.oo.equivalence.codegen.matchers;

import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;

/**
 *
 * @author skapral
 */
public class AttributesStandForIdentityTest extends TestsSuite {
    public AttributesStandForIdentityTest() {
        super(
            new TestCase(
                "match class with private final attributes",
                new AssertThatTypeMatches(
                    Type1.class,
                    new AttributesStandForIdentity()
                )
            ),
            new TestCase(
                "mismatch abstract class with private final attributes",
                new AssertThatTypeDoesNotMatch(
                    Type2.class,
                    new AttributesStandForIdentity()
                )
            ),
            new TestCase(
                "mismatch class with non-final attributes",
                new AssertThatTypeDoesNotMatch(
                    Type3.class,
                    new AttributesStandForIdentity()
                )
            ),
            new TestCase(
                "match abstract class with protected final attributes",
                new AssertThatTypeMatches(
                    Type4.class,
                    new AttributesStandForIdentity()
                )
            )
        );
    }
    
    
    private static class Type1 {
        private final Object id;

        public Type1(Object id) {
            this.id = id;
        }
    }

    private static abstract class Type2 {
        private final Object id;

        public Type2(Object id) {
            this.id = id;
        }
    }

    private static class Type3 {
        private final Object id;
        private Object state;

        public Type3(Object id) {
            this.id = id;
        }
    }
    
    private static abstract class Type4 {
        protected final Object id;

        public Type4(Object id) {
            this.id = id;
        }
    }
}
