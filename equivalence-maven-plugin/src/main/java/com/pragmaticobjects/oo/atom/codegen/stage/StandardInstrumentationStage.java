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
package com.pragmaticobjects.oo.atom.codegen.stage;

import com.pragmaticobjects.oo.atom.banner.BnnrFromResource;
import com.pragmaticobjects.oo.atom.codegen.bytebuddy.plugin.ConditionalPlugin;
import com.pragmaticobjects.oo.atom.codegen.bytebuddy.plugin.ImplementEObjectAttributesPlugin;
import com.pragmaticobjects.oo.atom.codegen.bytebuddy.plugin.MarkAsEObjectPlugin;
import com.pragmaticobjects.oo.atom.codegen.bytebuddy.plugin.VerbosePlugin;
import com.pragmaticobjects.oo.equivalence.base.EObject;

/**
 * Standard instrumentation scenario
 *
 * @author Kapralov Sergey
 */
public class StandardInstrumentationStage extends SequenceStage {
    /**
     * Ctor.
     *
     * @param stubbedInstrumentation generates stubbed implementations of
     * certain calls. Useful for testing.
     */
    public StandardInstrumentationStage(boolean stubbedInstrumentation) {
        super(
            new ShowBannerStage(
                new BnnrFromResource(
                    "banner"
                )
            ),
            new ShowStatsStage(),
            new ByteBuddyStage(
                new ConditionalPlugin(
                    type -> !type.isInterface() && type.getSuperClass().represents(Object.class),
                    new VerbosePlugin(
                            new MarkAsEObjectPlugin()
                    )
                )
            ),
            new ByteBuddyStage(
                new ConditionalPlugin(
                    type -> !type.isInterface() && type.getSuperClass().represents(EObject.class),
                    new VerbosePlugin(
                        new ImplementEObjectAttributesPlugin()
                    )
                )
            )
        );
    }

    /**
     * Default ctor.
     */
    public StandardInstrumentationStage() {
        this(false);
    }
}
