/*-
 * ===========================================================================
 * meta-freemarker-test
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
package com.pragmaticobjects.oo.meta.freemarker;

import com.pragmaticobjects.oo.tests.AssertAssertionFails;
import com.pragmaticobjects.oo.tests.AssertAssertionPasses;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

/**
 *
 * @author skapral
 */
public class AssertFreemarkerModelValueTest extends TestsSuite {
    public AssertFreemarkerModelValueTest() {
        super(
            new TestCase(
                "positive test",
                new AssertAssertionPasses(
                    new AssertFreemarkerModelValue(
                        new FAMSimple(HashMap.of("A", 42)),
                        "A", 42
                    )
                )
            ),
            new TestCase(
                "negative test",
                new AssertAssertionFails(
                    new AssertFreemarkerModelValue(
                        new FAMSimple(HashMap.of("A", 42)),
                        "A", 123
                    )
                )
            )
        );
    }
}

class FAMSimple implements FreemarkerArtifactModel {
    private final Map<String, Object> contents;

    public FAMSimple(Map<String, Object> contents) {
        this.contents = contents;
    }

    @Override
    public final <T> T get(String item) {
        return (T) contents.get(item).get();
    }
}
