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
import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import io.vavr.collection.List;

/**
 * Base ArchUnit condition that analyses a constructor's field accesses and method calls,
 * reporting violations for any that are not covered by the provided exclusion lists.
 *
 * @author Kapralov Sergey
 */
public class DeepConstructorAnalysisCondition extends ArchCondition<JavaConstructor> {
    private final String constructorType;
    private final List<FieldAccessExclusion> fieldAccessExclusions;
    private final List<ConstructorCallsExclusion> ctorCallExclusion;

    /**
     * Ctor.
     *
     * @param constructorType    human-readable name of the constructor type (e.g. "primary")
     * @param fieldAccessExclusions  patterns of field accesses that are permitted
     * @param ctorCallExclusion      patterns of constructor/method calls that are permitted
     */
    public DeepConstructorAnalysisCondition(String constructorType, List<FieldAccessExclusion> fieldAccessExclusions, List<ConstructorCallsExclusion> ctorCallExclusion) {
        super(constructorType + " constructor");
        this.constructorType = constructorType;
        this.fieldAccessExclusions = fieldAccessExclusions;
        this.ctorCallExclusion = ctorCallExclusion;
    }

    @Override
    public final void check(JavaConstructor constructor, ConditionEvents events) {
        java.util.Set<JavaFieldAccess> accesses = constructor.getFieldAccesses();
        accesses
            .stream()
            .forEach((JavaFieldAccess access) -> {
                if (access.isDeclaredInLambda()) {
                    // any fields access inside lambdas is actually legit,
                    // since they don't bring any side effects
                    // into constructor execution
                    return;
                }
                AccessTarget.FieldAccessTarget target = access.getTarget();
                JavaClass owner = target.getOwner();
                for (FieldAccessExclusion exclusion : fieldAccessExclusions) {
                    Boolean excluded = access.getTarget().resolveMember()
                        .map(exclusion.field::test)
                        .orElse(true);
                    if (excluded) {
                        return;
                    }
                }
                events.add(
                    SimpleConditionEvent.violated(
                        constructor,
                        constructor + "is not " + constructorType + " since accesses field " + target  + "(" + owner + ") at line " + access.getLineNumber() + "\n"
                    )
                );
            });
        java.util.Set<JavaCall<?>> calls = constructor.getCallsFromSelf();
        calls.stream()
            .forEach((JavaCall<?> call) -> {
                if (call.isDeclaredInLambda()) {
                    // any calls inside lambdas are actually legit,
                    // since they don't bring any side effects
                    // into constructor execution
                    return;
                }
                AccessTarget.CodeUnitCallTarget target = call.getTarget();
                JavaClass owner = target.getOwner();
                JavaCodeUnit javaCodeUnit = target
                    .resolveMember()
                    .orElseThrow(
                        () -> new RuntimeException("Unrecognised member")
                    );
                for (ConstructorCallsExclusion exclusion : ctorCallExclusion) {
                    if (exclusion.target.test(javaCodeUnit) && exclusion.owner.test(owner)) {
                        return;
                    }
                }
                events.add(
                    SimpleConditionEvent.violated(
                        constructor,
                        constructor + " is not " + constructorType + " since calls " + target  + "(" + owner + ") at line " + call.getLineNumber() + "\n"
                    )
                );
            });
    }
}

