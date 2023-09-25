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

import com.pragmaticobjects.oo.equivalence.base.EObject;
import com.pragmaticobjects.oo.equivalence.base.EquivalenceLogic;
import com.pragmaticobjects.oo.equivalence.codegen.ii.bb.Implementation;
import com.pragmaticobjects.oo.equivalence.codegen.matchers.MatchEqualsTriadGenerationCandidate;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class IIImplementEObjectEquals implements InstrumentationIteration {
    private static final Logger log = LoggerFactory.getLogger(IIImplementEObjectEquals.class);
    private static final Method EQLOGIC_EQUALS;
    private static final ElementMatcher<TypeDescription> GENERATION_CANDIDATE = new MatchEqualsTriadGenerationCandidate();
    static {
        try {
            EQLOGIC_EQUALS = EquivalenceLogic.class.getDeclaredMethod(
                "equals",
                EObject.class,
                Object.class
            );
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, TypeDescription typeDescription) {
        log.debug("equals() " + typeDescription.getActualName());
        if(!GENERATION_CANDIDATE.matches(typeDescription)) {
            log.debug("Skipping for non-base class");
            return builder;
        }
        final int modifiers = Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL;
        final StackManipulation impl = new StackManipulation.Compound(
            MethodVariableAccess.REFERENCE.loadFrom(0),
            MethodVariableAccess.REFERENCE.loadFrom(1),
            MethodInvocation.invoke(new MethodDescription.ForLoadedMethod(EQLOGIC_EQUALS)),
            MethodReturn.INTEGER
        );
        if(!typeDescription.getDeclaredMethods()
            .filter(ElementMatchers.named("equals"))
            .filter(ElementMatchers.takesArguments(1))
            .isEmpty()
        ) {
            if(typeDescription.isRecord()) {
                log.debug("defining equals for record");
                return builder
                    .method(ElementMatchers.named("equals"))
                    .intercept(new Implementation(impl))
                    .annotateMethod(new GeneratedMark(), new OverrideMark());
            } else {
                // If for some reason the method is already defined, we fail
                throw new RuntimeException("Defining equals() for this class is unexpected");
            }
        } else {
            log.debug("defining equals for record 2");
            return builder
                .defineMethod("equals", boolean.class, modifiers)
                .withParameters(Object.class)
                .intercept(new Implementation(impl))
                .annotateMethod(new GeneratedMark());
        }
    }
}
