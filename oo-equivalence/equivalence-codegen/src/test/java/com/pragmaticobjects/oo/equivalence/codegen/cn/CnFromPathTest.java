/*-
 * ===========================================================================
 * equivalence-codegen
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2021 Kapralov Sergey
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
package com.pragmaticobjects.oo.equivalence.codegen.cn;

import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;
import java.nio.file.Paths;


/**
 *
 * @author skapral
 */
public class CnFromPathTest extends TestsSuite {
    public CnFromPathTest() {
        super(
            new TestCase(
                "Class names resolved from a package containing '.class' substring",
                new AssertSimulatingClasspath(
                    path -> new AssertClassNamesContainCertainNames(
                        new CnFromPath(path),
                        "test.classes.A",
                        "test.classes.B"
                    ),
                    Paths.get("test/classes/A.class"),
                    Paths.get("test/classes/B.class")
                )
            ),
            new TestCase(
                "doesn't fail if the directory is absent",
                new AssertZeroClassNames(
                    new CnFromPath(Paths.get("/some-unexisting-path"))
                )
            ),
            new TestCase(
                "ignores module-info.class",
                new AssertSimulatingClasspath(
                    path -> new AssertZeroClassNames(
                        new CnFromPath(path)
                    ),
                    Paths.get("module-info.class")
                )
            )
        );
    }
}
