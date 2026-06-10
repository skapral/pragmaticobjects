/*-
 * ===========================================================================
 * equivalence-codegen
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
package com.pragmaticobjects.oo.equivalence.codegen.stage;

import com.pragmaticobjects.oo.equivalence.base.EObject;
import com.pragmaticobjects.oo.equivalence.base.EObjectHint;
import com.pragmaticobjects.oo.equivalence.codegen.banner.BnnrFromResource;
import com.pragmaticobjects.oo.equivalence.codegen.ii.*;
import com.pragmaticobjects.oo.equivalence.codegen.matchers.*;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * Standard instrumentation scenario used by the code generation agent.
 *
 * <p>This stage is the default {@link SequenceStage} for EObject instrumentation. It composes
 * validation, transformation, and verification stages into the production pipeline that prepares
 * user classes for participation in the {@link EObject} equality model.</p>
 *
 * <p>The scenario is intentionally declarative: this class does not perform bytecode manipulation
 * itself, but wires smaller stages, matchers, and instrumentation instructions in the order in
 * which they must be executed. The order is part of the contract:</p>
 *
 * <ol>
 *     <li>print the startup banner and current instrumentation statistics;</li>
 *     <li>run pre-checks that reject unsupported class hierarchies, for example inherited classes
 *         annotated with {@link EObjectHint};</li>
 *     <li>mark eligible classes as {@link EObject} implementations;</li>
 *     <li>generate the EObject infrastructure: identity attributes, base type, hash seed,
 *         {@code equals(Object)}, {@code hashCode()}, and {@code toString()};</li>
 *     <li>run post-checks that enforce the EObject model: non-abstract methods must be final,
 *         identity fields must be final, and EObject aliases must not declare additional fields or
 *         methods.</li>
 * </ol>
 *
 * <p>Instances of this class are stateless. Creating a new instance creates a ready-to-run
 * standard instrumentation pipeline with all required stages already configured.</p>
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
            // Pre-checks
            new ByteBuddyValidationStage(
                "It's prohibited to place EObjectHint on inherited classes",
                new MatchInheritedClass(),
                ElementMatchers.not(
                    ElementMatchers.isAnnotatedWith(EObjectHint.class)
                )
            ),
            // Instrumentation
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
                        ),
                        new IIVerbose(
                            new IIImplementEObjectEquals()
                        ),
                        new IIVerbose(
                            new IIImplementEObjectHashCode()
                        ),
                        new IIVerbose(
                            new IIImplementEObjectToString()
                        )
                    )
                )
            ),
            // Post-Checks
            new ByteBuddyValidationStage(
                "All methods of EObject must be final if not abstract",
                ElementMatchers.isSubTypeOf(EObject.class),
                new VerboseMatcher<>(
                    new AllMethodsAreFinal()
                )
            ),
            new ByteBuddyValidationStage(
                "All fields of EObject must be final",
                ElementMatchers.isSubTypeOf(EObject.class),
                new VerboseMatcher<>(
                    new AttributesStandForIdentity()
                )
            ),
            new ByteBuddyValidationStage(
                "EObject aliases must not introduce new fields or methods",
                new MatchEObjectAlias(),
                new VerboseMatcher<>(
                    ElementMatchers.not(
                        new ClassDeclaresAdditionalFieldsAndMethods()
                    )
                )
            )
        );
    }
}
