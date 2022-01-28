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
package com.pragmaticobjects.oo.equivalence.codegen.cfls;

import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;
import java.nio.file.Paths;

/**
 * Tests suite for {@link CflsFromPath}
 *
 * @author Kapralov Sergey
 */
class CflsFromPathTest extends TestsSuite {
    private static final String TEST_CLASS_PATH = System.getProperty("user.dir") + "/target/test-classes";

    /**
     * Ctor.
     */
    public CflsFromPathTest() {
        super(
            new TestCase(
                "doesn't fail if the path is absent",
                new AssertClassFileLocatorSourceDoesntResolveAClassName(
                    new CflsFromPath(
                        Paths.get("/some_unexisting_path")
                    ),
                    "SomeUnexistingClass"
                )
            ),
            new TestCase(
                "successfully resolves a class from path",
                new AssertClassFileLocatorSourceResolvesAClassName(
                    new CflsFromPath(
                        Paths.get(TEST_CLASS_PATH)
                    ),
                    Foo.class.getName()
                )
            ),
            new TestCase(
                "doesn't resolve absent class from path",
                new AssertClassFileLocatorSourceDoesntResolveAClassName(
                    new CflsFromPath(
                        Paths.get(TEST_CLASS_PATH)
                    ),
                    "SomeUnexistingClass"
                )
            )
        );
    }

    //CHECKSTYLE:OFF
    private static final class Foo {}
}
