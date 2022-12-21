/*-
 * ===========================================================================
 * equivalence-codegen
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2022 Kapralov Sergey
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

import com.pragmaticobjects.oo.equivalence.base.EquivalenceHint;
import com.pragmaticobjects.oo.equivalence.base.NaturallyEquivalent;
import com.pragmaticobjects.oo.equivalence.codegen.ii.bb.Box;
import com.pragmaticobjects.oo.equivalence.codegen.ii.bb.Implementation;
import com.pragmaticobjects.oo.equivalence.codegen.sets.Attributes;
import com.pragmaticobjects.oo.equivalence.codegen.sets.AttributesFromTypeDescription;
import com.pragmaticobjects.oo.equivalence.codegen.sets.AttributesNonStatic;

import java.lang.reflect.Constructor;
import java.util.function.Function;

import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.bytecode.Duplication;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.TypeCreation;
import net.bytebuddy.implementation.bytecode.collection.ArrayFactory;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.matcher.ElementMatchers;


/**
 * @author skapral
 */
public class IIImplementEObjectAttributes implements InstrumentationIteration {
    private static final Constructor NATURALLYEQ_CONSTR;

    static {
        try {
            NATURALLYEQ_CONSTR = NaturallyEquivalent.class.getConstructor(Object.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private final Function<TypeDescription, Attributes> attributesFn;

    public IIImplementEObjectAttributes(Function<TypeDescription, Attributes> attributesFn) {
        this.attributesFn = attributesFn;
    }

    public IIImplementEObjectAttributes() {
        this(
            td -> new AttributesNonStatic(
                new AttributesFromTypeDescription(td)
            )
        );
    }

    @Override
    public final DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, TypeDescription typeDescription) {
        if (!typeDescription.getDeclaredMethods()
            .filter(ElementMatchers.named("attributes"))
            .isEmpty()) {
            return builder;
        }
        StackManipulation attributesImpl = new StackManipulation.Compound(
            attributesFn.apply(typeDescription).asList()
                .map(field -> loadAttribute(field))
                .transform(list -> ArrayFactory.forType(TypeDescription.Generic.OBJECT).withValues(list.asJava())),
            MethodReturn.REFERENCE
        );
        return builder
            .defineMethod("attributes", Object[].class, Opcodes.ACC_PROTECTED | Opcodes.ACC_FINAL)
            .intercept(new Implementation(attributesImpl))
            .annotateMethod(new GeneratedMark());
    }

    private StackManipulation loadAttribute(FieldDescription field) {
        StackManipulation loadThis = MethodVariableAccess.REFERENCE.loadFrom(0);
        StackManipulation readAttr = FieldAccess.forField(field).read();
        if (field.getType().isPrimitive()) {
            readAttr = new StackManipulation.Compound(
                readAttr,
                new Box(field.getType().asErasure())
            );
        }
        AnnotationDescription.Loadable<EquivalenceHint> equivalenceHintLoadable = field.getDeclaredAnnotations().ofType(EquivalenceHint.class);
        if (equivalenceHintLoadable != null) {
            EquivalenceHint eqHint = equivalenceHintLoadable.load();
            if (eqHint.enabled()) {
                return new StackManipulation.Compound(
                    TypeCreation.of(
                        TypeDescription.ForLoadedType.of(NaturallyEquivalent.class)
                    ),
                    Duplication.of(
                        TypeDescription.ForLoadedType.of(NaturallyEquivalent.class)
                    ),
                    loadThis,
                    readAttr,
                    MethodInvocation.invoke(
                        new MethodDescription.ForLoadedConstructor(
                            NATURALLYEQ_CONSTR
                        )
                    )
                );
            }
        }
        return new StackManipulation.Compound(loadThis, readAttr);
    }
}
