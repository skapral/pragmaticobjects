/*-
 * ===========================================================================
 * equivalence-codegen
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2023 Kapralov Sergey
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
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatchers;

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
            // direct non-abstract subtype of Object, attributes of which are all final (stand for identity)
            new ConjunctionMatcher<>(
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
            ),
            // or explicitly hinted class if abstract
            new ConjunctionMatcher<>(
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
    
    public ShouldBeMarkedAsEObject() {
        this(EObjectHint.class, EObjectHint::enabled);
    }
}
