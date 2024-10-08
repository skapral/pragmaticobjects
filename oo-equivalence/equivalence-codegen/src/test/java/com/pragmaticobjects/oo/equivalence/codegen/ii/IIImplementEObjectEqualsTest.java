/*-
 * ===========================================================================
 * equivalence-codegen
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
package com.pragmaticobjects.oo.equivalence.codegen.ii;

import com.pragmaticobjects.oo.equivalence.assertions.Test;
import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;
import io.vavr.collection.List;
import net.bytebuddy.matcher.ElementMatchers;


class BaseSetForIIImplementEObjectEquals extends TestsSuite {
    public static final List<TestCase> BASE_SET = List.of(
        new TestCase(
            "declares the method",
            new AssertClassAfterInstrumentation(
                new IIImplementEObjectEquals(),
                Foo.class,
                ElementMatchers.declaresMethod(
                    ElementMatchers.hasMethodName("equals")
                )
            )
        ),
        new TestCase(
            "declaration is skept for non-base classes",
            new AssertClassAfterInstrumentation(
                new IIImplementEObjectEquals(),
                Foo2.class,
                ElementMatchers.not(
                    ElementMatchers.declaresMethod(
                        ElementMatchers.hasMethodName("equals")
                    )
                )
            )
        )
    );

    public BaseSetForIIImplementEObjectEquals(Test... tests) {
        super(
            List.of(tests).prependAll(BASE_SET)
        );
    }
}


/**
 *
 * @author skapral
 */
public class IIImplementEObjectEqualsTest extends BaseSetForIIImplementEObjectEquals {
}
