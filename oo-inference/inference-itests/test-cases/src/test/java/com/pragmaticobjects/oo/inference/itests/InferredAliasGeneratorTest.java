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

import com.pragmaticobjects.oo.inference.basic.InferredAliasGenerator;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import io.vavr.collection.List;
import com.pragmaticobjects.oo.meta.anno.procesor.AssertAnnotationProcessorGeneratesFiles;

import static com.pragmaticobjects.oo.meta.anno.procesor.AssertAnnotationProcessorGeneratesFiles.SourceFile;

/**
 *
 * @author skapral
 */
public class InferredAliasGeneratorTest extends TestsSuite {
    public InferredAliasGeneratorTest() {
        super(
            new TestCase(
                "Fraction inference alias",
                new AssertAnnotationProcessorGeneratesFiles(
                    new InferredAliasGenerator(),
                    List.of(
                        new SourceFile("com/test/alias1/Fraction.java"),
                        new SourceFile("com/test/alias1/FracFromStringInference.java")
                    ),
                    List.of(
                        new SourceFile("com/test/alias1/FracFromString.java")
                    )
                )
            ),
            new TestCase(
                "Fraction inference alias with explicit base class",
                new AssertAnnotationProcessorGeneratesFiles(
                    new InferredAliasGenerator(),
                    List.of(
                        new SourceFile("com/test/alias2/Fraction.java"),
                        new SourceFile("com/test/alias2/FracFromStringExplicitInference.java"),
                        new SourceFile("com/test/alias2/FractionInferred.java")
                    ),
                    List.of(
                        new SourceFile("com/test/alias2/FracFromStringExplicit.java")
                    )
                )
            ),
            new TestCase(
                "Fraction memoized alias",
                new AssertAnnotationProcessorGeneratesFiles(
                    new InferredAliasGenerator(),
                    List.of(
                        new SourceFile("com/test/alias3/Fraction.java"),
                        new SourceFile("com/test/alias3/FracFromStringInferenceMemoized.java")
                    ),
                    List.of(
                        new SourceFile("com/test/alias3/FracFromStringMemoized.java")
                    )
                )
            ),
            new TestCase(
                "Fraction alias with attribute that requires importing",
                new AssertAnnotationProcessorGeneratesFiles(
                    new InferredAliasGenerator(),
                    List.of(
                        new SourceFile("com/test/alias4/Fraction.java"),
                        new SourceFile("com/test/alias4/RandomFracInference.java")
                    ),
                    List.of(
                        new SourceFile("com/test/alias4/RandomFrac.java")
                    )
                )
            )
        );
    }
}
