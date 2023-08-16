/*-
 * ===========================================================================
 * equivalence-codegen
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
package com.pragmaticobjects.oo.equivalence.codegen.matchers;

import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;
import com.pragmaticobjects.oo.equivalence.base.EObject;
import com.pragmaticobjects.oo.equivalence.base.EquivalenceLogic;

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
                    SimpleType.class,
                    new ShouldImplementEObjectMethods()
                )
            ),
            new TestCase(
                "match eobject direct subtypes",
                new AssertThatTypeMatches(
                    EObjectDirectSubtype.class,
                    new ShouldImplementEObjectMethods()
                )
            ),
            new TestCase(
                "mismatch eobject indirect subtypes",
                new AssertThatTypeDoesNotMatch(
                    EObjectIndirectSubtype.class,
                    new ShouldImplementEObjectMethods()
                )
            ),
            new TestCase(
                "mismatch abstract class",
                new AssertThatTypeDoesNotMatch(
                    AbstractType.class,
                    new ShouldImplementEObjectMethods()
                )
            ),
            new TestCase(
                "match abstract eobject subtype",
                new AssertThatTypeMatches(
                    AbstractEObjectSubtype.class,
                    new ShouldImplementEObjectMethods()
                )
            ),
            new TestCase(
                "match subtype from abstract eobject",
                new AssertThatTypeMatches(
                    AbstractEObjectSubtypeImpl.class,
                    new ShouldImplementEObjectMethods()
                )
            )
        );
    }
    
    private static class SimpleType { 
        private final Object obj;

        public SimpleType(Object obj) {
            this.obj = obj;
        }
    }
    
    private static class EObjectDirectSubtype implements EObject {
        private final Object obj;

        public EObjectDirectSubtype(Object obj) {
            this.obj = obj;
        }

        @Override
        public final Object[] attributes() {
            return new Object[] {obj};
        }

        @Override
        public final int hashSeed() {
            return 42;
        }

        @Override
        public final Class<?> baseType() {
            return EObjectDirectSubtype.class;
        }

        @Override
        public final boolean equals(Object obj) {
            return EquivalenceLogic.equals(this, obj);
        }

        @Override
        public final int hashCode() {
            return EquivalenceLogic.hashCode(this);
        }

        @Override
        public final String toString() {
            return EquivalenceLogic.toString(this);
        }
    }
    
    private static class EObjectIndirectSubtype extends EObjectDirectSubtype {
        public EObjectIndirectSubtype(Object obj) {
            super(obj);
        }
    }
    
    private static abstract class AbstractType {
    }
    
    private static abstract class AbstractEObjectSubtype implements EObject {
    }
    
    private static class AbstractEObjectSubtypeImpl extends AbstractEObjectSubtype {
        @Override
        public final Object[] attributes() {
            return new Object[] {};
        }

        @Override
        public final int hashSeed() {
            return 42;
        }

        @Override
        public final Class<?> baseType() {
            return AbstractEObjectSubtypeImpl.class;
        }

        @Override
        public final boolean equals(Object obj) {
            return EquivalenceLogic.equals(this, obj);
        }

        @Override
        public final int hashCode() {
            return EquivalenceLogic.hashCode(this);
        }

        @Override
        public final String toString() {
            return EquivalenceLogic.toString(this);
        }
    }
}

