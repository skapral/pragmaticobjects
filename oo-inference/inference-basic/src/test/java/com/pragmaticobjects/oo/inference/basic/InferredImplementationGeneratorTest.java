/*-
 * ===========================================================================
 * inference-basic
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
package com.pragmaticobjects.oo.inference.basic;

import assertions.AssertAnnotationProcessorGeneratesFiles;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import io.vavr.collection.List;

import static assertions.AssertAnnotationProcessorGeneratesFiles.*;

/**
 *
 * @author skapral
 */
public class InferredImplementationGeneratorTest extends TestsSuite {
    public InferredImplementationGeneratorTest() {
        super(
            new TestCase(
                "Fraction inference alias",
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
                "Fraction inference alias in package different than interface",
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
                "Fraction inference alias with void method",
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
            )
        );
    }
}
