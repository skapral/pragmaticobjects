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

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;

import static com.pragmaticobjects.oo.guidelines.archunit.archunit.Items.*;
import static com.tngtech.archunit.base.DescribedPredicate.and;
import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.JavaAccess.Predicates.origin;
import static com.tngtech.archunit.core.domain.JavaAccess.Predicates.target;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.ANONYMOUS_CLASSES;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.ENUMS;
import static com.tngtech.archunit.core.domain.JavaCodeUnit.Predicates.method;
import static com.tngtech.archunit.core.domain.properties.HasModifiers.Predicates.modifier;
import static com.tngtech.archunit.core.domain.properties.HasOwner.Predicates.With.owner;
import static com.tngtech.archunit.lang.conditions.ArchConditions.callConstructorWhere;
import static com.tngtech.archunit.lang.conditions.ArchPredicates.are;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Assertion that enforces the guideline that non-static methods of concrete classes must not
 * call constructors of other concrete classes (to avoid hidden side effects and coupling).
 *
 * @author Kapralov Sergey
 */
public class AssertThatMethodsOfConcreteClassesDontCallConstructorsOfOtherConcretics extends AssertThatClassesFollowProvidedArchunitGuidelines {
    private static DescribedPredicate<JavaClass> concretics(DescribedPredicate<JavaClass> concretics) {
        return and(
            concretics,
            not(ENUMS),
            // Since inferences make class compositions in runtime, we consider them exception out of the rule
            not(INFERENCES),
            not(ANONYMOUS_CLASSES),
            not(ARCHITECTURAL_EXCLUSION)
        ).as("concretics");
    }

    private static ArchCondition<JavaClass> callConstructorsOfConcreticsFromMethods(DescribedPredicate<JavaClass> concretics) {
        return callConstructorWhere(
            and(
                // Method belongs to concretics
                target(
                    owner(
                        concretics
                    )
                ),
                origin(
                    // Method is not static and is not exclusion out of the rules
                    and(
                        method(),
                        not(
                            modifier(JavaModifier.STATIC)
                        ),
                        not(
                            ARCHITECTURAL_EXCLUSION
                        )
                    )
                )
            )
        );
    }

    /**
     * Ctor.
     *
     * @param javaClasses classes to check
     * @param concretics  predicate identifying the concrete classes to enforce the rule on
     */
    public AssertThatMethodsOfConcreteClassesDontCallConstructorsOfOtherConcretics(
        JavaClasses javaClasses,
        DescribedPredicate<JavaClass> concretics
    ) {
        super(
            noClasses(
            ).that(
                are(
                    concretics(concretics)
                )
            ).should(
                callConstructorsOfConcreticsFromMethods(concretics)
            ),
            javaClasses
        );
    }
}
