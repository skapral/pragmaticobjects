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

import com.pragmaticobjects.oo.equivalence.base.EObject;
import com.pragmaticobjects.oo.equivalence.base.EObjectHint;
import java.lang.annotation.Annotation;
import java.util.function.Function;

import com.pragmaticobjects.oo.equivalence.base.EquivalenceCompliant;
import io.vavr.collection.List;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatchers;

class DirectNonAbstractSubtypeOfObjectAttributesOfWhichAreAllFinal extends ConjunctionMatcher<TypeDescription> {
    public <Hint extends Annotation> DirectNonAbstractSubtypeOfObjectAttributesOfWhichAreAllFinal(
        Class<Hint> eobjectHint,
        Function<Hint, Boolean> hintStatus
    ) {
        super(
            List.of(
                ElementMatchers.not(
                    // No sense in marking the objects that already handle equivalence
                    ElementMatchers.isSubTypeOf(EquivalenceCompliant.class)
                ),
                ElementMatchers.not(
                    ElementMatchers.isAbstract()
                ),
                new MatchSuperClass(
                    ElementMatchers.is(Object.class)
                ),
                new AttributesStandForIdentity(),
                // unless it is explicitly hinted as not a candidate
                ElementMatchers.not(
                    new HasHint<>(eobjectHint, hintStatus, false)
                )
            )
        );
    }
}

class ExplicitlyHintedAbstractClass extends ConjunctionMatcher<TypeDescription> {
    public <Hint extends Annotation> ExplicitlyHintedAbstractClass(
        Class<Hint> eobjectHint,
        Function<Hint, Boolean> hintStatus
    ) {
        super(
            List.of(
                ElementMatchers.isAbstract(),
                new ConjunctionMatcher<>(
                    new MatchSuperClass(
                        ElementMatchers.is(Object.class)
                    ),
                    new HasHint<>(eobjectHint, hintStatus, true)
                )
            )
        );
    }
}

/**
 * Matches classes, that should be marked as EObjects. 
 * Semantically, "marking as EObject" means confirming that equivalence logic is applicable for the type.
 * Technically, "marking as EObject" means making the class implement {@link EObject} and its methods, if necessary.
 * 
 * Later, all {@link EObject} implementors will become candidates for instrumentation
 * 
 * @see ShouldImplementEObjectMethods
 * @author skapral
 */
public class ShouldBeMarkedAsEObject extends DisjunctionMatcher<TypeDescription> {
    public <Hint extends Annotation> ShouldBeMarkedAsEObject(Class<Hint> eobjectHint, Function<Hint, Boolean> hintStatus) {
        super(
            new DirectNonAbstractSubtypeOfObjectAttributesOfWhichAreAllFinal(eobjectHint, hintStatus),
            new ExplicitlyHintedAbstractClass(eobjectHint, hintStatus)
        );
    }
    
    public ShouldBeMarkedAsEObject() {
        this(EObjectHint.class, EObjectHint::enabled);
    }
}
