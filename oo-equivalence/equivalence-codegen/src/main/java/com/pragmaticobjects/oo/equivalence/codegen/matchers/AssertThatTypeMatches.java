/*-
 * ===========================================================================
 * equivalence-codegen
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
package com.pragmaticobjects.oo.equivalence.codegen.matchers;

import com.pragmaticobjects.oo.equivalence.assertions.Assertion;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Assertion which passes if the {@link ElementMatcher} under the test matches
 * the provided {@link TypeDescription}
 * 
 * @author skapral
 */
public class AssertThatTypeMatches implements Assertion {
    private final TypeDescription typeDescription;
    private final ElementMatcher<TypeDescription> matcher;

    /**
     * Ctor.
     *
     * @param typeDescription Type description
     * @param matcher Matcher
     */
    public AssertThatTypeMatches(TypeDescription typeDescription, ElementMatcher<TypeDescription> matcher) {
        this.typeDescription = typeDescription;
        this.matcher = matcher;
    }

    public AssertThatTypeMatches(Class<?> clazz, ElementMatcher<TypeDescription> matcher) {
        this(
            new TypeDescription.ForLoadedType(clazz),
            matcher
        );
    }
    
    @Override
    public final void check() throws Exception {
        assertThat(matcher.matches(typeDescription)).isTrue();
    }
}
