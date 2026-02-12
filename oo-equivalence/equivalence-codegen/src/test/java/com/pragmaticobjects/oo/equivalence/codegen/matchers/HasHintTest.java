/*-
 * ===========================================================================
 * equivalence-codegen
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2026 Kapralov Sergey
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
import com.tests.hint.HasHint;
import com.tests.hint.Type1;
import com.tests.hint.Type2;
import com.tests.hint.Type3;
import com.tests.hint.markedpackage1.Type4;
import com.tests.hint.markedpackage1.Type5;

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
                    new HasHint(true)
                )
            ),
            new TestCase(
                "mismatch hinted-false type",
                new AssertThatTypeDoesNotMatch(
                    Type2.class,
                    new HasHint(true)
                )
            ),
            new TestCase(
                "mismatch hinted type",
                new AssertThatTypeDoesNotMatch(
                    Type1.class,
                    new HasHint(false)
                )
            ),
            new TestCase(
                "mismatch type without true hint",
                new AssertThatTypeDoesNotMatch(
                    Type3.class,
                    new HasHint(true)
                )
            ),
            new TestCase(
                "mismatch type without false hint",
                new AssertThatTypeDoesNotMatch(
                    Type3.class,
                    new HasHint(false)
                )
            ),
            new TestCase(
                "package-level hint defines defaults for a whole package",
                new AssertThatTypeDoesNotMatch(
                    Type4.class,
                    new HasHint(true)
                )
            ),
                new TestCase(
                "class-level hint overrides package-level hint",
                new AssertThatTypeMatches(
                    Type5.class,
                    new HasHint(true)
                )
            )
        );
    }
}
