/*-
 * ===========================================================================
 * guidelines-archunit
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2026 Kapralov Sergey
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
package com.pragmaticobjects.oo.guidelines.archunit.archunit.assertions;

import com.pragmaticobjects.oo.tests.Assertion;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Base assertion that bridges between the oo-tests {@link Assertion} abstraction and an
 * ArchUnit {@link ArchRule}. Subclasses configure the rule in their constructor and this class
 * executes it against a provided set of {@link JavaClasses}.
 *
 * @author Kapralov Sergey
 */
public class AssertThatClassesFollowProvidedArchunitGuidelines implements Assertion {
    private final ArchRule archunitRule;
    private final JavaClasses javaClasses;

    /**
     * Ctor.
     *
     * @param archunitRule the ArchUnit rule to evaluate
     * @param javaClasses  the classes to evaluate the rule against
     */
    public AssertThatClassesFollowProvidedArchunitGuidelines(ArchRule archunitRule, JavaClasses javaClasses) {
        this.archunitRule = archunitRule;
        this.javaClasses = javaClasses;
    }

    @Override
    public final void check() throws Exception {
        archunitRule.allowEmptyShould(true).check(javaClasses);
    }
}
