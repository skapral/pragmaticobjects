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

import com.pragmaticobjects.oo.equivalence.assertions.AssertCombined;
import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;

/**
 * Matches classes, that declare additional fields and/or methods
 * @author skapral
 */
public class ClassDeclaresAdditionalFieldsAndMethodsTest extends TestsSuite {
    public ClassDeclaresAdditionalFieldsAndMethodsTest() {
        super(
            new TestCase(
                "mismatch base class without any methods",
                new AssertThatTypeDoesNotMatch(
                    Base.class,
                    new ClassDeclaresAdditionalFieldsAndMethods()
                )
            ),
            new TestCase(
                "mismatch base inheritor",
                new AssertThatTypeDoesNotMatch(
                    Base2Negative.class,
                    new ClassDeclaresAdditionalFieldsAndMethods()
                )
            ),
            new TestCase(
                "match inheritors with declared fields or methods",
                new AssertCombined(
                    new AssertThatTypeMatches(
                        Base2Positive_ExtraFields.class,
                        new ClassDeclaresAdditionalFieldsAndMethods()
                    ),
                    new AssertThatTypeMatches(
                        Base2Positive_ExtraMethods.class,
                        new ClassDeclaresAdditionalFieldsAndMethods()
                    )
                )
            )
        );
    }
    
    public static class Base {
    }
    
    public static class Base2Negative extends Base {
        public Base2Negative() {
        }
    }
    
    public static class Base2Positive_ExtraFields extends Base {
        private final int z;
        
        public Base2Positive_ExtraFields(int z) {
            super();
            this.z = z;
        }
    }
    
    public static class Base2Positive_ExtraMethods extends Base {
        public final void additionalMethod() {}
    }
}
