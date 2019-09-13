/*-
 * ===========================================================================
 * data-core
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 Kapralov Sergey
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
package com.pragmaticobjects.oo.meta.src;

import com.pragmaticobjects.oo.tests.AssertAssertionFails;
import com.pragmaticobjects.oo.tests.AssertAssertionPasses;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;

/**
 * Tests for {@link AssertSourceFileGenerated}.
 * 
 * @author skapral
 */
public class AssertSourceFileGeneratedTest extends TestsSuite {    
    /**
     * Ctor.
     */
    public AssertSourceFileGeneratedTest() {
        super(
            new TestCase(
                "assertion fails on wrong test contents",
                new AssertAssumingTemporaryDirectory(tmpDir -> 
                    new AssertAssertionFails(
                        new AssertSourceFileGenerated(
                            new SrcFileStatic(
                                tmpDir.resolve("srcfile"),
                                "testcontents"
                            ),
                            tmpDir.resolve("srcfile"),
                            "wrongtestcontents"
                        )
                    )
                )
            ),
            new TestCase(
                "assertion fails on wrong test file path",
                new AssertAssumingTemporaryDirectory(tmpDir ->
                    new AssertAssertionFails(
                        new AssertSourceFileGenerated(
                            new SrcFileStatic(
                                tmpDir.resolve("srcfile"),
                                "testcontents"
                            ),
                            tmpDir.resolve("wrongfile"),
                            "testcontents"
                        )
                    )
                )
            ),
            new TestCase(
                "assertion passes",
                new AssertAssumingTemporaryDirectory(tmpDir ->
                    new AssertAssertionPasses(
                        new AssertSourceFileGenerated(
                            new SrcFileStatic(
                                tmpDir.resolve("srcfile"),
                                "testcontents"
                            ),
                            tmpDir.resolve("srcfile"),
                            "testcontents"
                        )
                    )
                )
            )
        );
    }
}
