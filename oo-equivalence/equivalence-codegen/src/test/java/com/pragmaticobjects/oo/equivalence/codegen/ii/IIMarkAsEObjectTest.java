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
package com.pragmaticobjects.oo.equivalence.codegen.ii;

import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;
import com.pragmaticobjects.oo.equivalence.base.EObjectContract;
import net.bytebuddy.matcher.ElementMatchers;

/**
 *
 * @author skapral
 */
public class IIMarkAsEObjectTest extends TestsSuite {
    public IIMarkAsEObjectTest() {
        super(
            new TestCase(
                "mark on simple class",
                new AssertClassAfterInstrumentation(
                    new IIMarkAsEObject(),
                    Type1.class,
                    ElementMatchers.isSubTypeOf(EObjectContract.class)
                )
            ),
            new TestCase(
                "mark on abstract class",
                new AssertClassAfterInstrumentation(
                    new IIMarkAsEObject(),
                    Type2.class,
                    ElementMatchers.isSubTypeOf(EObjectContract.class)
                )
            ),
            new TestCase(
                "mark a class with generics",
                new AssertClassAfterInstrumentation(
                    new IIMarkAsEObject(),
                    Type3.class,
                    ElementMatchers.isSubTypeOf(EObjectContract.class)
                )
            )
        );
    }
    
    private static class Type1 {
        private final int x;

        public Type1(int x) {
            this.x = x;
        }

        public int getX() {
            return x;
        }
    }
    
    private static abstract class Type2 {
        protected final int x;

        public Type2(int x) {
            this.x = x;
        }
        
        public int getX() {
            return x;
        }
    }
    
    private static class Type3<T> {
        private final T x;

        public Type3(T x) {
            this.x = x;
        }

        public T getX() {
            return x;
        }
    }
}
