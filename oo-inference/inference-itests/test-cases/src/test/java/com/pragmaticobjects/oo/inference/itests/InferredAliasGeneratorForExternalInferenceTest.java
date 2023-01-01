/*-
 * ===========================================================================
 * inference-itests.test-cases
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
package com.pragmaticobjects.oo.inference.itests;

import com.pragmaticobjects.oo.inference.basic.InferredAliasGenerator;
import com.pragmaticobjects.oo.meta.anno.procesor.AssertAnnotationProcessorGeneratesFiles;
import com.pragmaticobjects.oo.tests.AssertIgnore;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import io.vavr.collection.List;

/**
 *
 * @author skapral
 */
public class InferredAliasGeneratorForExternalInferenceTest extends TestsSuite {
    public InferredAliasGeneratorForExternalInferenceTest() {
        super(
            new TestCase(
                "alias for an inference, located in external module",
                new AssertIgnore( // TODO: implement a fix
                    new AssertAnnotationProcessorGeneratesFiles(
                        new InferredAliasGenerator(),
                        List.of(
                            new AssertAnnotationProcessorGeneratesFiles.SourceFile("com/test/external1/FracFromStringInference.java")
                        ),
                        List.of(
                            new AssertAnnotationProcessorGeneratesFiles.SourceFile("com/test/external1/FracFromString.java")
                        )
                    )
                )
            )
        );
    }
}
