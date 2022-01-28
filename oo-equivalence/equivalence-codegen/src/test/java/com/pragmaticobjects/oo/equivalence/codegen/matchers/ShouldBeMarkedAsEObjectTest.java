/*-
 * ===========================================================================
 * equivalence-codegen
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2022 Kapralov Sergey
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
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author skapral
 */
public class ShouldBeMarkedAsEObjectTest extends TestsSuite {
    public ShouldBeMarkedAsEObjectTest() {
        super(
            new TestCase(
                "match hinted abstract object",
                new AssertThatTypeMatches(
                    Type1.class,
                    new ShouldBeMarkedAsEObject(EObjectHint.class, EObjectHint::enabled)
                )
            ),
            new TestCase(
                "mismatch hinted abstract subtype from abstract object",
                new AssertThatTypeDoesNotMatch(
                    Type2.class,
                    new ShouldBeMarkedAsEObject(EObjectHint.class, EObjectHint::enabled)
                )
            ),
            new TestCase(
                "mismatch non-hinted abstract class",
                new AssertThatTypeDoesNotMatch(
                    Type3.class,
                    new ShouldBeMarkedAsEObject(EObjectHint.class, EObjectHint::enabled)
                )
            ),
            new TestCase(
                "mismatch subtype from non-hinted abstract class",
                new AssertThatTypeDoesNotMatch(
                    Type4.class,
                    new ShouldBeMarkedAsEObject(EObjectHint.class, EObjectHint::enabled)
                )
            ),
            new TestCase(
                "match simple class",
                new AssertThatTypeMatches(
                    Type5.class,
                    new ShouldBeMarkedAsEObject(EObjectHint.class, EObjectHint::enabled)
                )
            )
        );
    }
    
    
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    private static @interface EObjectHint {
        boolean enabled() default true;
    }
    
    private static abstract @EObjectHint class Type1 {
    }
    
    private static abstract @EObjectHint class Type2 extends Type1 {
    }
    
    private static abstract class Type3 {
    }
    
    private static class Type4 extends Type3 {
    }
    
    private static class Type5 {
    }
}
