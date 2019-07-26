/*-
 * ===========================================================================
 * equivalence-maven-plugin
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
package com.pragmaticobjects.oo.equivalence.codegen.cn;

import com.pragmaticobjects.oo.equivalence.assertions.Assertion;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import org.assertj.core.api.Assertions;

/**
 * Assertion that passes if {@link ClassNames} contain certain class names
 *
 * @author Kapralov Sergey
 */
public class AssertClassNamesContainCertainNames implements Assertion {
    private final ClassNames classNames;
    private final Set<String> namesToCheck;

    /**
     * Ctor.
     *
     * @param classNames {@link ClassNames} under test
     * @param namesToCheck expected names
     */
    public AssertClassNamesContainCertainNames(ClassNames classNames, Set<String> namesToCheck) {
        this.classNames = classNames;
        this.namesToCheck = namesToCheck;
    }

    /**
     * Ctor.
     *
     * @param classNames {@link ClassNames} under test
     * @param namesToCheck expected names
     */
    public AssertClassNamesContainCertainNames(ClassNames classNames, String... namesToCheck) {
        this(classNames, HashSet.of(namesToCheck));
    }

    @Override
    public final void check() throws Exception {
        List<String> extractedNames = classNames.classNames();
        Assertions.assertThat(namesToCheck).containsAll(extractedNames);
    }
}
