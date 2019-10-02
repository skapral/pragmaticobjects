/*-
 * ===========================================================================
 * equivalence-codegen
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
package com.pragmaticobjects.oo.equivalence.codegen.matchers;

import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;
import com.pragmaticobjects.oo.equivalence.base.EObject;

/**
 *
 * @author skapral
 */
public class ShouldImplementEObjectMethodsTest extends TestsSuite {
    public ShouldImplementEObjectMethodsTest() {
        super(
            new TestCase(
                "mismatch simple object",
                new AssertThatTypeDoesNotMatch(
                    Type1.class,
                    new ShouldImplementEObjectMethods()
                )
            ),
            new TestCase(
                "match eobject direct subtypes",
                new AssertThatTypeMatches(
                    Type2.class,
                    new ShouldImplementEObjectMethods()
                )
            ),
            new TestCase(
                "mismatch eobject indirect subtypes",
                new AssertThatTypeDoesNotMatch(
                    Type3.class,
                    new ShouldImplementEObjectMethods()
                )
            ),
            new TestCase(
                "mismatch abstract class",
                new AssertThatTypeDoesNotMatch(
                    Type4.class,
                    new ShouldImplementEObjectMethods()
                )
            ),
            new TestCase(
                "mismatch abstract eobject subtype",
                new AssertThatTypeDoesNotMatch(
                    Type5.class,
                    new ShouldImplementEObjectMethods()
                )
            ),
            new TestCase(
                "mismatch subtype from abstract eobject",
                new AssertThatTypeMatches(
                    Type6.class,
                    new ShouldImplementEObjectMethods()
                )
            )
        );
    }
    
    private static class Type1 {
        private final Object obj;

        public Type1(Object obj) {
            this.obj = obj;
        }
    }
    
    private static class Type2 extends EObject {
        private final Object obj;

        public Type2(Object obj) {
            this.obj = obj;
        }

        @Override
        protected final Object[] attributes() {
            return new Object[] {obj};
        }

        @Override
        protected final int hashSeed() {
            return 42;
        }

        @Override
        protected final Class<? extends EObject> baseType() {
            return Type2.class;
        }
    }
    
    private static class Type3 extends Type2 {
        public Type3(Object obj) {
            super(obj);
        }
    }
    
    private static abstract class Type4 {
    }
    
    private static abstract class Type5 extends EObject {
    }
    
    private static class Type6 extends Type5 {
        @Override
        protected final Object[] attributes() {
            return new Object[] {};
        }

        @Override
        protected final int hashSeed() {
            return 42;
        }

        @Override
        protected final Class<? extends EObject> baseType() {
            return Type6.class;
        }
    }
}

