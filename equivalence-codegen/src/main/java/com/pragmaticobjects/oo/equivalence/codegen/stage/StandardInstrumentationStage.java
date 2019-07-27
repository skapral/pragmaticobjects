/*-
 * ===========================================================================
 * equivalence-maven-plugin
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
import com.pragmaticobjects.oo.equivalence.codegen.plugin.ConditionalPlugin;
import com.pragmaticobjects.oo.equivalence.codegen.plugin.ImplementEObjectAttributesPlugin;
import com.pragmaticobjects.oo.equivalence.codegen.plugin.MarkAsEObjectPlugin;
import com.pragmaticobjects.oo.equivalence.codegen.plugin.VerbosePlugin;
import com.pragmaticobjects.oo.equivalence.base.EObject;
import com.pragmaticobjects.oo.equivalence.codegen.matchers.ConjunctionMatcher;
import com.pragmaticobjects.oo.equivalence.codegen.matchers.MatchAttributesStandForIdentity;
import com.pragmaticobjects.oo.equivalence.codegen.matchers.MatchSuperClass;
import com.pragmaticobjects.oo.equivalence.codegen.plugin.ImplementEObjectBaseTypePlugin;
import com.pragmaticobjects.oo.equivalence.codegen.plugin.ImplementEObjectHashSeedPlugin;
import com.pragmaticobjects.oo.equivalence.codegen.plugin.SequentialPlugin;
import net.bytebuddy.description.type.TypeDescription;
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
            new ByteBuddyStage(
                new ConditionalPlugin(
                    new ConjunctionMatcher<TypeDescription>(
                        new MatchSuperClass(
                            ElementMatchers.is(Object.class)
                        ),
                        new MatchAttributesStandForIdentity()
                    ),
                    new VerbosePlugin(
                        new MarkAsEObjectPlugin()
                    )
                )
            ),
            new ByteBuddyStage(
                new ConditionalPlugin(
                    new ConjunctionMatcher<TypeDescription>(
                        new MatchSuperClass(
                            ElementMatchers.is(EObject.class)
                        )
                    ),
                    new SequentialPlugin(
                        new VerbosePlugin(
                            new ImplementEObjectAttributesPlugin()
                        ),
                        new VerbosePlugin(
                            new ImplementEObjectBaseTypePlugin()
                        ),
                        new VerbosePlugin(
                            new ImplementEObjectHashSeedPlugin()
                        )
                    )
                )
            )
        );
    }
}
