/*-
 * ===========================================================================
 * meta-anno-procesor
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
package com.pragmaticobjects.oo.meta.anno.procesor;

import com.pragmaticobjects.oo.tests.AssertAssertionFails;
import com.pragmaticobjects.oo.tests.AssertAssertionPasses;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import io.vavr.collection.List;
import java.nio.file.Paths;

import static com.pragmaticobjects.oo.meta.anno.procesor.AssertAnnotationProcessorGeneratesFiles.SourceFile;
import com.test.assertions.DummyProcessor;

/**
 *
 * @author skapral
 */
public class AssertAnnotationProcessorGeneratesFilesTest extends TestsSuite {
    public AssertAnnotationProcessorGeneratesFilesTest() {
        super(
            new TestCase(
                "DummyProcessor on annotated file",
                new AssertAssertionPasses(
                    new AssertAnnotationProcessorGeneratesFiles(
                        new DummyProcessor(),
                        List.of(
                            new SourceFile("com/test/assertions/Dummy.java"),
                            new SourceFile("com/test/assertions/AnnotatedType1.java")
                        ),
                        List.of(
                            new SourceFile("com/test/assertions/A")
                        )
                    )
                )
            ),
            new TestCase(
                "DummyProcessor on annotated file - file is missing",
                new AssertAssertionFails(
                    new AssertAnnotationProcessorGeneratesFiles(
                        new DummyProcessor(),
                        List.of(
                            new SourceFile("com/test/assertions/Dummy.java"),
                            new SourceFile("com/test/assertions/AnnotatedType1.java")
                        ),
                        List.of(
                            new SourceFile("com/test/assertions/B")
                        )
                    )
                )
            ),
            new TestCase(
                "DummyProcessor on annotated file - compilation error",
                new AssertAssertionFails(
                    new AssertAnnotationProcessorGeneratesFiles(
                        new DummyProcessor(),
                        List.of(
                            new SourceFile("com/test/assertions/Dummy.java"),
                            new SourceFile("com/test/assertions/BrokenSourceFile", Paths.get("com/test/assertions/BrokenSourceFile.java"))
                        ),
                        List.empty()
                    )
                )
            )
        );
    }
}
