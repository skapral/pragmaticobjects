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
package com.pragmaticobjects.oo.guidelines.archunit.archunit.testsuite;

import com.pragmaticobjects.oo.equivalence.base.EquivalenceCompliant;
import com.pragmaticobjects.oo.guidelines.archunit.archunit.assertions.*;
import com.pragmaticobjects.oo.guidelines.archunit.archunit.exclusions.ConstructorCallsExclusion;
import com.pragmaticobjects.oo.guidelines.archunit.archunit.exclusions.FieldAccessExclusion;
import com.pragmaticobjects.oo.tests.AssertIgnore;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import io.vavr.collection.List;

public class GuidelinesTests extends TestsSuite {
    public static DescribedPredicate<JavaClass> DEFAULT_CONCRETICS = JavaClass.Predicates.implement(EquivalenceCompliant.class);

    public GuidelinesTests(
        JavaClasses javaClasses,
        DescribedPredicate<JavaClass> concretics,
        List<FieldAccessExclusion> fieldAccessExclusionsAllowedInPrimaryConstructor,
        List<ConstructorCallsExclusion> callsAllowedInPrimaryConstructor,
        List<FieldAccessExclusion> fieldAccessExclusionsAllowedInSecondaryConstructor,
        List<ConstructorCallsExclusion> callsAllowedInSecondaryConstructor
    ) {
        super(
            new TestCase(
                "concrete classes are not final",
                new AssertThatConcreteClassesAreNotFinal(javaClasses, concretics)
            ),
            new TestCase(
                "non-abstract methods of concrete classes are final",
                new AssertThatAllNonAbstractMethodsOfConcreteClassesAreFinal(javaClasses, concretics)
            ),
            new TestCase(
                "concrete classes declare only final fields",
                new AssertThatConcreteClassesDeclareOnlyFinalFields(javaClasses, concretics)
            ),
            new TestCase(
                "methods of concrete classes don't call constructors of other concrete classes",
                new AssertIgnore(
                    // TODO: Controversial rule. Deside something later
                    new AssertThatMethodsOfConcreteClassesDontCallConstructorsOfOtherConcretics(javaClasses, concretics)
                )
            ),
            new TestCase(
                "constructors of concrete classes are primary or secondary",
                new AssertThatAllConstructorsOfConcreticsAreEitherPrimaryOrSecondary(
                    javaClasses,
                    concretics,
                    fieldAccessExclusionsAllowedInPrimaryConstructor,
                    callsAllowedInPrimaryConstructor,
                    fieldAccessExclusionsAllowedInSecondaryConstructor,
                    callsAllowedInSecondaryConstructor
                )
            )
        );
    }

    public GuidelinesTests(
        JavaClasses javaClasses
    ) {
        this(
            javaClasses,
            DEFAULT_CONCRETICS,
            List.empty(),
            List.empty(),
            List.empty(),
            List.empty()
        );
    }
}
