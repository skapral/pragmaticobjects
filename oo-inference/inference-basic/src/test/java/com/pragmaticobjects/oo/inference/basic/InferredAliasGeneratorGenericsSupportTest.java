/*-
 * ===========================================================================
 * inference-basic
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
package com.pragmaticobjects.oo.inference.basic;

import assertions.AssertAnnotationProcessorGeneratesFiles;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import io.vavr.collection.List;

import static assertions.AssertAnnotationProcessorGeneratesFiles.SourceFile;

public class InferredAliasGeneratorGenericsSupportTest extends TestsSuite {
    public InferredAliasGeneratorGenericsSupportTest() {
        super(
            new TestCase(
                "Plain generic",
                new AssertAnnotationProcessorGeneratesFiles(
                    new InferredAliasGenerator(),
                    List.of(
                        new SourceFile("com/test/generics1/Fraction.java"),
                        new SourceFile("com/test/generics1/FracFromListInference.java")
                    ),
                    List.of(
                        new SourceFile("com/test/generics1/FracFromList.java")
                    )
                )
            ),
            new TestCase(
                "Wildcard generic",
                new AssertAnnotationProcessorGeneratesFiles(
                    new InferredAliasGenerator(),
                    List.of(
                        new SourceFile("com/test/generics2/Fraction.java"),
                        new SourceFile("com/test/generics2/FractionInferred.java"),
                        new SourceFile("com/test/generics2/FracFromListInferenceWithWildcardGenerics.java")
                    ),
                    List.of(
                        new SourceFile("com/test/generics2/FracFromListWithWildcardGenerics.java")
                    )
                )
            ),
            new TestCase(
                "Complex generic",
                new AssertAnnotationProcessorGeneratesFiles(
                    new InferredAliasGenerator(),
                    List.of(
                        new SourceFile("com/test/generics3/Operation.java"),
                        new SourceFile("com/test/generics3/OperationInferred.java"),
                        new SourceFile("com/test/generics3/CompositeOperationInference.java")
                    ),
                    List.of(
                        new SourceFile("com/test/generics3/CompositeOperation.java")
                    )
                )
            )
        );
    }
}
