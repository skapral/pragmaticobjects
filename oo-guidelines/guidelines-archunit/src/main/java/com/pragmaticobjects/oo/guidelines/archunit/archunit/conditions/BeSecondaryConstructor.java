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
package com.pragmaticobjects.oo.guidelines.archunit.archunit.conditions;

import com.pragmaticobjects.oo.guidelines.archunit.archunit.exclusions.ConstructorCallsExclusion;
import com.pragmaticobjects.oo.guidelines.archunit.archunit.exclusions.FieldAccessExclusion;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMember;
import com.tngtech.archunit.core.domain.JavaModifier;
import io.vavr.collection.List;

import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Predicate;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.type;
import static com.tngtech.archunit.core.domain.JavaCodeUnit.Predicates.constructor;
import static com.tngtech.archunit.core.domain.properties.HasModifiers.Predicates.modifier;

/**
 * ArchUnit condition that recognizes secondary constructors.
 *
 * <p>A secondary constructor is expected to delegate object creation without
 * performing direct state mutation. During the deep constructor analysis this
 * condition therefore rejects arbitrary field accesses and calls, allowing only
 * operations that are considered safe for argument preparation or delegation.</p>
 *
 * <p>The default field-access exclusions allow reading static constants and enum
 * members. The default call exclusions allow calls to constructors of configured
 * concretics, Vavr collection APIs, stateless {@code java.time} factory methods,
 * selected side-effect-free JDK helpers, and {@link StringBuilder} usage that is
 * typically produced by the compiler for string concatenation.</p>
 *
 * <p>The {@code concretics} predicate defines which concrete classes may be
 * instantiated from a secondary constructor. Use the constructor with additional
 * exclusions to permit project-specific safe accesses or calls while preserving
 * the default secondary-constructor restrictions.</p>
 */
public class BeSecondaryConstructor extends DeepConstructorAnalysisCondition {
    /**
     * Ctor. Uses default exclusions.
     *
     * @param concretics predicate identifying concrete classes whose constructors are allowed targets
     */
    public BeSecondaryConstructor(
        DescribedPredicate<JavaClass> concretics
    ) {
        this(
            concretics,
            List.empty(),
            List.empty()
        );
    }

    /**
     * Ctor.
     *
     * @param concretics                        predicate identifying concrete classes whose constructors are allowed targets
     * @param additionalFieldAccessExclusions   extra field-access patterns allowed in a secondary constructor
     * @param additionalCtorCallsExclusions     extra constructor-call patterns allowed in a secondary constructor
     */
    public BeSecondaryConstructor(
        DescribedPredicate<JavaClass> concretics,
        List<FieldAccessExclusion> additionalFieldAccessExclusions,
        List<ConstructorCallsExclusion> additionalCtorCallsExclusions
    ) {
        super(
            "secondary",
            List.of(
                new FieldAccessExclusion(
                    // Access to static constants is legit
                    DescribedPredicate.and(
                        modifier(JavaModifier.STATIC).forSubtype(),
                        modifier(JavaModifier.FINAL).forSubtype()
                    )
                ),
                new FieldAccessExclusion(
                    // Access to enums is legit
                    JavaMember.Predicates.declaredIn(
                        JavaClass.Predicates.ENUMS
                    ).forSubtype()
                )
            ).appendAll(additionalFieldAccessExclusions),
            List.of(
                new ConstructorCallsExclusion(
                    // Constructors of other concretics are allowed
                    concretics,
                    constructor().forSubtype()
                ),
                new ConstructorCallsExclusion(
                    // Vavr collections are allowed, because they are side-effect-free
                    resideInAPackage("io.vavr.."),
                    alwaysTrue()
                ),
                new ConstructorCallsExclusion(
                    // Java.time (JSR 310) object builders are allowed
                    // We allow them in secondary constructors because they are stateless
                    resideInAPackage("java.time.."),
                    modifier(JavaModifier.STATIC).forSubtype()
                ),
                new ConstructorCallsExclusion(
                    // Static methods of the classes below are considered legit,
                    // since they don't bring any side effects
                    DescribedPredicate.or(
                        type(Predicate.class),
                        type(Objects.class),
                        type(Integer.class),
                        type(Boolean.class),
                        type(Byte.class),
                        type(Short.class),
                        type(Long.class),
                        type(Character.class),
                        type(Float.class),
                        type(Double.class),
                        type(Paths.class),
                        type(Class.class)
                    ),
                    modifier(JavaModifier.STATIC).forSubtype()
                ),
                new ConstructorCallsExclusion(
                    // Since StringBuilder is often used by compiler for concatenating strings,
                    // and concatenating strings is considered as a rather safe side-effect-free operation
                    // we make an exclusion for it
                    type(StringBuilder.class),
                    alwaysTrue()
                )
            ).appendAll(additionalCtorCallsExclusions)
        );
    }
}
