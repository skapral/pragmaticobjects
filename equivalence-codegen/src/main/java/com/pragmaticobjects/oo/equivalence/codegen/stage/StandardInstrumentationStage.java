/*-
 * ===========================================================================
 * equivalence-codegen
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 Kapralov Sergey
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
package com.pragmaticobjects.oo.equivalence.codegen.stage;

import com.pragmaticobjects.oo.equivalence.codegen.banner.BnnrFromResource;
import com.pragmaticobjects.oo.equivalence.codegen.ii.IIConditional;
import com.pragmaticobjects.oo.equivalence.codegen.ii.IIImplementEObjectAttributes;
import com.pragmaticobjects.oo.equivalence.codegen.ii.IIMarkAsEObject;
import com.pragmaticobjects.oo.equivalence.codegen.ii.IIVerbose;
import com.pragmaticobjects.oo.equivalence.base.EObject;
import com.pragmaticobjects.oo.equivalence.codegen.matchers.MatchSuperClass;
import com.pragmaticobjects.oo.equivalence.codegen.ii.IIImplementEObjectBaseType;
import com.pragmaticobjects.oo.equivalence.codegen.ii.IIImplementEObjectHashSeed;
import com.pragmaticobjects.oo.equivalence.codegen.ii.IISequential;
import com.pragmaticobjects.oo.equivalence.codegen.matchers.AllFieldsArePrivateFinal;
import com.pragmaticobjects.oo.equivalence.codegen.matchers.AllMethodsAreFinal;
import com.pragmaticobjects.oo.equivalence.codegen.matchers.ShouldBeMarkedAsEObject;
import com.pragmaticobjects.oo.equivalence.codegen.matchers.ShouldImplementEObjectMethods;
import com.pragmaticobjects.oo.equivalence.codegen.matchers.VerboseMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * Standard instrumentation scenario
 *
 * @author Kapralov Sergey
 */
public class StandardInstrumentationStage extends SequenceStage {
    /**
     * Ctor.
     */
    public StandardInstrumentationStage() {
        super(new ShowBannerStage(
                new BnnrFromResource(
                    "banner"
                )
            ),
            new ShowStatsStage(),
            new ByteBuddyTransformationStage(
                new IIConditional(
                    new ShouldBeMarkedAsEObject(),
                    new IIVerbose(
                        new IIMarkAsEObject()
                    )
                )
            ),
            new ByteBuddyTransformationStage(
                new IIConditional(
                    new ShouldImplementEObjectMethods(),
                    new IISequential(
                        new IIVerbose(
                            new IIImplementEObjectAttributes()
                        ),
                        new IIVerbose(
                            new IIImplementEObjectBaseType()
                        ),
                        new IIVerbose(
                            new IIImplementEObjectHashSeed()
                        )
                    )
                )
            ),
            // Post-Checks
            new ByteBuddyValidationStage(
                "All methods of EObject must be final if not abstract",
                new MatchSuperClass(
                    ElementMatchers.is(EObject.class)
                ),
                new VerboseMatcher<>(
                    new AllMethodsAreFinal()
                )
            ),
            new ByteBuddyValidationStage(
                "All fields of EObject must be final",
                new MatchSuperClass(
                    ElementMatchers.is(EObject.class)
                ),
                new VerboseMatcher<>(
                    new AllFieldsArePrivateFinal()
                )
            )
        );
    }
}
