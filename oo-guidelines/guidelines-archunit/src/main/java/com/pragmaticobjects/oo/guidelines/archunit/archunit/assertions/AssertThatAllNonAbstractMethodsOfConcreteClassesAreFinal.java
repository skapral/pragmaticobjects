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

import static com.pragmaticobjects.oo.guidelines.archunit.archunit.Items.ARCHITECTURAL_EXCLUSION;
import static com.pragmaticobjects.oo.guidelines.archunit.archunit.Items.DECLARED_METHODS;
import static com.tngtech.archunit.base.DescribedPredicate.and;
import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.ENUMS;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.INTERFACES;
import static com.tngtech.archunit.core.domain.JavaModifier.ABSTRACT;
import static com.tngtech.archunit.core.domain.properties.HasModifiers.Predicates.modifier;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

/**
 * Assertion that enforces the guideline that all non-abstract, non-static, non-private methods
 * of concrete classes must be declared {@code final}.
 *
 * @author Kapralov Sergey
 */
public class AssertThatAllNonAbstractMethodsOfConcreteClassesAreFinal extends AssertThatClassesFollowProvidedArchunitGuidelines {
    /**
     * Ctor.
     *
     * @param javaClasses classes to check
     * @param concretics  predicate identifying the concrete classes to enforce the rule on
     */
    public AssertThatAllNonAbstractMethodsOfConcreteClassesAreFinal(
        JavaClasses javaClasses,
        DescribedPredicate<JavaClass> concretics
    ) {
        super(
            methods().that(
                    and(
                        DECLARED_METHODS,
                        not(
                            ARCHITECTURAL_EXCLUSION
                        ),
                        not(
                            modifier(ABSTRACT)
                        )
                    )
                ).and()
                .areDeclaredInClassesThat(
                    and(
                        concretics,
                        not(ENUMS),
                        not(
                            // Since anonymous classes are typically not extended, it is allowed to
                            // leave its methods non-final
                            JavaClass.Predicates.ANONYMOUS_CLASSES
                        ),
                        not(INTERFACES),
                        not(ARCHITECTURAL_EXCLUSION)
                    )
                )
                .and()
                .areNotPrivate()
                .and()
                .areNotStatic()
                .should()
                .beFinal(),
            javaClasses
        );
    }
}
