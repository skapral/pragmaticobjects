/*-
 * ===========================================================================
 * inference-itests.test-cases
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
package com.pragmaticobjects.oo.inference.itests;

import com.pragmaticobjects.oo.inference.basic.InferredImplementationGenerator;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import io.vavr.collection.List;
import com.pragmaticobjects.oo.meta.anno.procesor.AssertAnnotationProcessorGeneratesFiles;

import static com.pragmaticobjects.oo.meta.anno.procesor.AssertAnnotationProcessorGeneratesFiles.SourceFile;

/**
 *
 * @author skapral
 */
public class InferredImplementationGeneratorTest extends TestsSuite {
    public InferredImplementationGeneratorTest() {
        super(
            new TestCase(
                "Fraction inference",
                new AssertAnnotationProcessorGeneratesFiles(
                    new InferredImplementationGenerator(),
                    List.of(
                        new SourceFile("com/test/inferred1/package-info.java"),
                        new SourceFile("com/test/inferred1/Fraction.java")
                    ),
                    List.of(
                        new SourceFile("com/test/inferred1/FractionInferred.java")
                    )
                )
            ),
            new TestCase(
                "Fraction inference in package different than interface",
                new AssertAnnotationProcessorGeneratesFiles(
                    new InferredImplementationGenerator(),
                    List.of(
                        new SourceFile("com/test/inferred2/package-info.java"),
                        new SourceFile("com/test/inferred1/Fraction.java")
                    ),
                    List.of(
                        new SourceFile("com/test/inferred2/FractionInferred.java")
                    )
                )
            ),
            new TestCase(
                "Fraction inference with void method",
                new AssertAnnotationProcessorGeneratesFiles(
                    new InferredImplementationGenerator(),
                    List.of(
                        new SourceFile("com/test/inferred3/package-info.java"),
                        new SourceFile("com/test/inferred3/Operation.java")
                    ),
                    List.of(
                        new SourceFile("com/test/inferred3/OperationInferred.java")
                    )
                )
            ),
            new TestCase(
                "Generator ignores inner classes when generating inference",
                new AssertAnnotationProcessorGeneratesFiles(
                    new InferredImplementationGenerator(),
                    List.of(
                        new SourceFile("com/test/inferred4/package-info.java"),
                        new SourceFile("com/test/inferred4/Operation.java")
                    ),
                    List.of(
                        new SourceFile("com/test/inferred4/OperationInferred.java")
                    )
                )
            ),
            new TestCase(
                "Generator takes into account 'throws' in method signatures",
                new AssertAnnotationProcessorGeneratesFiles(
                    new InferredImplementationGenerator(),
                    List.of(
                        new SourceFile("com/test/inferred5/Assertion.java"),
                        new SourceFile("com/test/inferred5/package-info.java")
                    ),
                    List.of(
                        new SourceFile("com/test/inferred5/AssertionInferred.java")
                    )
                )
            )
        );
    }
}
