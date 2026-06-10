/*-
 * ===========================================================================
 * guidelines-selftest
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
package com.pragmaticobjects.oo.guidelines.archunit.selftest;

import com.pragmaticobjects.oo.guidelines.archunit.archunit.exclusions.ConstructorCallsExclusion;
import com.pragmaticobjects.oo.guidelines.archunit.archunit.testsuite.GuidelinesTests;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaCodeUnit;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.domain.properties.HasModifiers;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import io.vavr.collection.List;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatchers;

import static com.tngtech.archunit.base.DescribedPredicate.and;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.*;

/**
 * Self-test suite that applies guidelines to oo-equivalence classes.
 */
public class GuidelinesForOOEquivalenceTests extends GuidelinesTests {
    private static final JavaClasses CLASSES_TO_TEST = new ClassFileImporter(
    ).importPackages("com.pragmaticobjects.oo.equivalence..");

    public GuidelinesForOOEquivalenceTests() {
        super(
            CLASSES_TO_TEST,
            and(
                resideInAPackage(
                    "com.pragmaticobjects.oo.equivalence.."
                )
            ),
            List.empty(),
            List.empty(),
            List.empty(),
            List.of(
                new ConstructorCallsExclusion(
                    // Its allowed to instantiate type descriptions inside secondary constructors,
                    // since they are stateless
                    implement(TypeDescription.class),
                    JavaCodeUnit.Predicates.constructor().forSubtype()
                ),
                new ConstructorCallsExclusion(
                    // It's allowed to use ElementMatchers utility class, which instantiates stateless element matchers
                    type(ElementMatchers.class),
                    HasModifiers.Predicates.modifier(JavaModifier.STATIC).forSubtype()
                )
            )
        );
    }
}
