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
package com.pragmaticobjects.oo.equivalence.codegen.ii.bb;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.jar.asm.MethodVisitor;

/**
 *
 * @author skapral
 */
public class Implementation implements net.bytebuddy.implementation.Implementation {
    private final StackManipulation sm;

    /**
     * Ctor.
     *
     * @param sm {@link Implementation}'s {@link StackManipulation}.
     */
    public Implementation(StackManipulation sm) {
        this.sm = sm;
    }

    @Override
    public final ByteCodeAppender appender(Implementation.Target implementationTarget) {
        return new ByteCodeAppender(sm);
    }

    @Override
    public final InstrumentedType prepare(InstrumentedType instrumentedType) {
        return instrumentedType;
    }

    /**
     * {@link ByteCodeAppender} for {@link BtGenerateMethod}
     *
     * @author Kapralov Sergey
     */
    private static class ByteCodeAppender implements net.bytebuddy.implementation.bytecode.ByteCodeAppender {
        private final StackManipulation sm;

        /**
         * Ctor.
         *
         * @param sm {@link ByteCodeAppender}'s {@link StackManipulation}.
         */
        public ByteCodeAppender(StackManipulation sm) {
            this.sm = sm;
        }

        @Override
        public final ByteCodeAppender.Size apply(MethodVisitor mv, net.bytebuddy.implementation.Implementation.Context ctx, MethodDescription method) {
            StackManipulation.Size size = sm.apply(mv, ctx);
            return new ByteCodeAppender.Size(size.getMaximalSize(), method.getStackSize());
        }
    }
}
