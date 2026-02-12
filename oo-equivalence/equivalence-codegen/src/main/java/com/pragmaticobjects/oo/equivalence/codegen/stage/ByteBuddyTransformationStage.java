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

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import com.pragmaticobjects.oo.equivalence.codegen.ii.InstrumentationIteration;

/**
 * Bytebuddy-powered stage
 *
 * @author Kapralov Sergey
 */
public class ByteBuddyTransformationStage extends ByteBuddyStage {
    /**
     * Ctor.
     *
     * @param task Task.
     */
    public ByteBuddyTransformationStage(final InstrumentationIteration task) {
        super(
            (td, cfl, workingDirectory, errors) -> {
                try {
                    final DynamicType.Builder<?> builder = new ByteBuddy().redefine(td, cfl);
                    final DynamicType.Unloaded<?> unloaded = task.apply(builder, td).make();
                    unloaded.saveIn(workingDirectory.toFile());
                } catch(Exception ex) {
                    throw new RuntimeException(
                        String.format("Exception while transforming class %s with %s", td, task.getClass().getName()),
                        ex
                    );
                }
            }
        );
    }
}
