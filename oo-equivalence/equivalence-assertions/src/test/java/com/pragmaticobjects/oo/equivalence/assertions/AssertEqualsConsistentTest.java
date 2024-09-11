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

/**
 *
 * @author skapral
 */
public class AssertEqualsConsistentTest extends TestsSuite {
    public AssertEqualsConsistentTest() {
        super(
            new TestCase(
                "pass scenario",
                new AssertAssertionPasses(
                    new AssertEqualsConsistent(
                        new TestObject(true),
                        new TestObject(true)
                    )
                )
            ),
            new TestCase(
                "fail scenario",
                new AssertAssertionFails(
                    new AssertEqualsConsistent(
                        new TestObject(false),
                        new TestObject(true)
                    )
                )
            )
        );
    }
    
    private static final class TestObject {
        private final boolean consistentEquality;
        private int i = 0;

        public TestObject(boolean consistentEquality) {
            this.consistentEquality = consistentEquality;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final TestObject other = (TestObject) obj;
            if (this.i != other.i) {
                return false;
            }
            if(!consistentEquality) {
                i++;
            }
            return true;
        }        
    }
}
