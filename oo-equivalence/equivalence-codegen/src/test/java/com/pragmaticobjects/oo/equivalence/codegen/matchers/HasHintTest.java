/*-
 * ===========================================================================
 * equivalence-codegen
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2020 Kapralov Sergey
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
public class HasHintTest extends TestsSuite {
    public HasHintTest() {
        super(
            new TestCase(
                "match hinted type",
                new AssertThatTypeMatches(
                    Type1.class,
                    new _HasEObjectHint(true)
                )
            ),
            new TestCase(
                "mismatch hinted-false type",
                new AssertThatTypeDoesNotMatch(
                    Type2.class,
                    new _HasEObjectHint(true)
                )
            ),
            new TestCase(
                "mismatch hinted type",
                new AssertThatTypeDoesNotMatch(
                    Type1.class,
                    new _HasEObjectHint(false)
                )
            ),
            new TestCase(
                "mismatch type without true hint",
                new AssertThatTypeDoesNotMatch(
                    Type3.class,
                    new _HasEObjectHint(true)
                )
            ),
            new TestCase(
                "mismatch type without false hint",
                new AssertThatTypeDoesNotMatch(
                    Type3.class,
                    new _HasEObjectHint(false)
                )
            )  
        );
    }
    
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    private static @interface EObjectHint { boolean enabled() default true; }
    private static class _HasEObjectHint extends HasHint<EObjectHint> {
        public _HasEObjectHint(boolean enabled) {
            super(EObjectHint.class, EObjectHint::enabled, enabled);
        }
    }
    
    private static @EObjectHint class Type1 {}
    private static @EObjectHint(enabled = false) class Type2 {}
    private static class Type3 {}
}
