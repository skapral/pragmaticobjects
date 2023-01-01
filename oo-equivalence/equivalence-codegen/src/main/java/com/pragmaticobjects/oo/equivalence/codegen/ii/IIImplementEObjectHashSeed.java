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
package com.pragmaticobjects.oo.equivalence.codegen.ii;

import com.pragmaticobjects.oo.equivalence.codegen.ii.bb.Implementation;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author skapral
 */
public class IIImplementEObjectHashSeed implements InstrumentationIteration {
    private static final Logger log = LoggerFactory.getLogger(IIImplementEObjectHashSeed.class);
    private final int seedValue;

    public IIImplementEObjectHashSeed(int seedValue) {
        this.seedValue = seedValue;
    }

    public IIImplementEObjectHashSeed() {
        this(
            92821 // taken from here https://stackoverflow.com/a/2816747/3223300
        );
    }

    @Override
    public final DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, TypeDescription typeDescription) {
        log.debug("hashSeed() " + typeDescription.getActualName());
        if(!typeDescription.getDeclaredMethods()
                .filter(ElementMatchers.named("hashSeed"))
                .isEmpty()) {
            // If for some reason the method is already defined, we skip instrumentation for it
            log.debug("Skipping " + typeDescription);
            return builder;
        }
        int modifiers = Opcodes.ACC_PROTECTED;
        if(!typeDescription.isAbstract()) {
            modifiers = modifiers | Opcodes.ACC_FINAL;
        }
        StackManipulation baseTypeImpl = new StackManipulation.Compound(
            IntegerConstant.forValue(seedValue),
            MethodReturn.INTEGER
        );
        return builder
                .defineMethod("hashSeed", int.class, modifiers)
                .intercept(new Implementation(baseTypeImpl))
                .annotateMethod(new GeneratedMark());
    }
}
