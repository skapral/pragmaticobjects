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

import com.pragmaticobjects.oo.guidelines.archunit.archunit.conditions.BePrimaryConstructor;
import com.pragmaticobjects.oo.guidelines.archunit.archunit.conditions.BeSecondaryConstructor;
import com.pragmaticobjects.oo.guidelines.archunit.archunit.exclusions.ConstructorCallsExclusion;
import com.pragmaticobjects.oo.guidelines.archunit.archunit.exclusions.FieldAccessExclusion;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import io.vavr.collection.List;

import static com.pragmaticobjects.oo.guidelines.archunit.archunit.Items.ARCHITECTURAL_EXCLUSION;
import static com.tngtech.archunit.base.DescribedPredicate.and;
import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.constructors;

public class AssertThatAllConstructorsOfConcreticsAreEitherPrimaryOrSecondary extends AssertThatClassesFollowProvidedArchunitGuidelines {
    public AssertThatAllConstructorsOfConcreticsAreEitherPrimaryOrSecondary(
        JavaClasses javaClasses,
        DescribedPredicate<JavaClass> concretics,
        List<FieldAccessExclusion> fieldAccessExclusionsAllowedInPrimaryConstructor,
        List<ConstructorCallsExclusion> callsAllowedInPrimaryConstructor,
        List<FieldAccessExclusion> fieldAccessExclusionsAllowedInSecondaryConstructor,
        List<ConstructorCallsExclusion> callsAllowedInSecondaryConstructor
    ) {
        super(
            constructors().that().areDeclaredInClassesThat(
                    and(
                        concretics,
                        not(ENUMS),
                        not(RECORDS),
                        not(ANONYMOUS_CLASSES),
                        not(LOCAL_CLASSES),
                        not(ARCHITECTURAL_EXCLUSION)
                    )
                )
                .should(
                    new BePrimaryConstructor(
                        fieldAccessExclusionsAllowedInPrimaryConstructor,
                        callsAllowedInPrimaryConstructor
                    )
                )
                .orShould(
                    new BeSecondaryConstructor(
                        concretics,
                        fieldAccessExclusionsAllowedInSecondaryConstructor,
                        callsAllowedInSecondaryConstructor
                    )
                ),
            javaClasses
        );
    }

    public AssertThatAllConstructorsOfConcreticsAreEitherPrimaryOrSecondary(
        JavaClasses javaClasses,
        DescribedPredicate<JavaClass> concretics
    ) {
        this(
            javaClasses,
            concretics,
            List.empty(),
            List.empty(),
            List.empty(),
            List.empty()
        );
    }
}
