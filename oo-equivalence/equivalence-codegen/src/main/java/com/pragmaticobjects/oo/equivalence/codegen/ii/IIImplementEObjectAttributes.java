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
import com.pragmaticobjects.oo.equivalence.base.EquivalenceHint;
import com.pragmaticobjects.oo.equivalence.base.NaturallyEquivalent;
import com.pragmaticobjects.oo.equivalence.codegen.ii.bb.Box;
import com.pragmaticobjects.oo.equivalence.codegen.ii.bb.Implementation;
import com.pragmaticobjects.oo.equivalence.codegen.sets.Attributes;
import com.pragmaticobjects.oo.equivalence.codegen.sets.AttributesFromTypeDescription;
import com.pragmaticobjects.oo.equivalence.codegen.sets.AttributesNonStatic;
import java.lang.annotation.Annotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;

import java.util.function.Function;
import net.bytebuddy.asm.AsmVisitorWrapper;

import net.bytebuddy.description.annotation.AnnotationDescription;

import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.bytecode.Addition;
import net.bytebuddy.implementation.bytecode.Duplication;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.TypeCreation;
import net.bytebuddy.implementation.bytecode.collection.ArrayFactory;
import net.bytebuddy.implementation.bytecode.collection.ArrayLength;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.ClassWriter;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;


import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IIImplementEObjectAttributes implements InstrumentationIteration {
    private static final Logger log = LoggerFactory.getLogger(IIImplementEObjectAttributes.class);
    private static final Constructor NATURALLYEQ_CONSTR;
    private static final Method ARRAYS_COPYOF;
    private static final Method SYSTEM_ARRAYCOPY;

    static {
        try {
            NATURALLYEQ_CONSTR = NaturallyEquivalent.class.getConstructor(Object.class);
            ARRAYS_COPYOF = Arrays.class.getDeclaredMethod("copyOf", Object[].class, int.class);
            SYSTEM_ARRAYCOPY = System.class.getDeclaredMethod("arraycopy", Object.class, int.class, Object.class, int.class, int.class);
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
        log.debug("attributes() " + typeDescription.getActualName());
        if (!typeDescription.getDeclaredMethods()
            .filter(ElementMatchers.named("attributes"))
            .isEmpty()) {
            // If for some reason the method is already defined, we skip instrumentation for it
            log.debug("Skipping " + typeDescription);
            return builder;
        }
        
        final boolean doOverride, isAbstract;
        final int modifiers;
        final Annotation[] annotations;
        {
            TypeDescription superclass = typeDescription.getSuperClass().asErasure();
            doOverride = superclass.isAssignableTo(EObject.class) && !superclass.represents(EObject.class);
            isAbstract = typeDescription.isAbstract();
        }
        if(isAbstract) {
            modifiers = Opcodes.ACC_PROTECTED;
        } else {
            modifiers = Opcodes.ACC_PROTECTED | Opcodes.ACC_FINAL;
        }
        if(doOverride) {
            annotations = new Annotation[] {
                new GeneratedMark(),
                new OverrideMark()
            };
        } else {
            annotations = new Annotation[] {
                new GeneratedMark()
            };
        }
        
        log.debug("isAbstract = {}", isAbstract);
        log.debug("doOverride = {}", doOverride);
        log.debug("modifiers = {}", Modifier.toString(modifiers));
        final StackManipulation impl = doOverride ?
                new StackManipulation.Compound(
                    loadSuperAttributes(typeDescription),
                    MethodVariableAccess.REFERENCE.storeAt(1),
                    loadAttributes(typeDescription),
                    MethodVariableAccess.REFERENCE.storeAt(2),
                    mergeArrays(1, 2, 3),
                    MethodVariableAccess.REFERENCE.loadFrom(3),
                    MethodReturn.REFERENCE
                ) :
                new StackManipulation.Compound(
                    loadAttributes(typeDescription),
                    MethodReturn.REFERENCE
                );

        
        
        return builder
            .defineMethod("attributes", Object[].class, modifiers)
            .intercept(new Implementation(impl))
            .annotateMethod(annotations)
            .visit(new AsmVisitorWrapper() {
                @Override
                public final int mergeWriter(int arg0) {
                    return arg0 | ClassWriter.COMPUTE_FRAMES;
                }

                @Override
                public final int mergeReader(int arg0) {
                    return arg0;
                }

                @Override
                public ClassVisitor wrap(TypeDescription arg0, ClassVisitor arg1, net.bytebuddy.implementation.Implementation.Context arg2, TypePool arg3, FieldList<FieldDescription.InDefinedShape> arg4, MethodList<?> arg5, int arg6, int arg7) {
                    return arg1;
                }
            });
    }
    
    
    private StackManipulation loadSuperAttributes(TypeDescription td) {
        TypeDescription superclass = td.getSuperClass().asErasure();
        MethodDescription superMethod = new MethodDescription.Latent(
            superclass,
            "attributes",
            Modifier.PROTECTED | Modifier.FINAL,
            Collections.emptyList(),
            (new TypeDescription.ForLoadedType(Object[].class)).asGenericType(),
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            null,
            null
        );
        log.debug("{}", superMethod.getReceiverType());
        return new StackManipulation.Compound(
            MethodVariableAccess.REFERENCE.loadFrom(0),
            MethodInvocation.invoke(superMethod).special(superclass)
        );
    }
    
    private StackManipulation loadAttributes(TypeDescription td) {
        return attributesFn.apply(td).asList()
                .map(field -> loadAttribute(field))
                .transform(list -> ArrayFactory.forType(TypeDescription.Generic.OBJECT).withValues(list.asJava()));
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
    
    /**
     * Assuming two object arrays located on certain frames, merges them into one
     * and places the result on frame, by doing:
     * 
     * <code>
     * Object[] sum = Arrays.copyOf(arr1, arr1.length + arr2.length);
     * System.arraycopy(arr2, 0, sum, arr1.length, arr2.length);
     * </code>
     * 
     * @param indexSuper
     * @param indexCurrent
     * @param indexSum
     * @return 
     */
    private StackManipulation mergeArrays(int indexSuper, int indexCurrent, int indexSum) {
        StackManipulation loadSuper = MethodVariableAccess.REFERENCE.loadFrom(indexSuper);
        StackManipulation loadCurrent = MethodVariableAccess.REFERENCE.loadFrom(indexCurrent);
        StackManipulation storeSum = MethodVariableAccess.REFERENCE.storeAt(indexSum);
        StackManipulation loadSum = MethodVariableAccess.REFERENCE.loadFrom(indexSum);
        StackManipulation result = new StackManipulation.Compound(
            // Loads first array on stack
            loadSuper,
            // Places it's length on top
            loadSuper,
            ArrayLength.INSTANCE,
            // Places second's array length on top
            loadCurrent,
            ArrayLength.INSTANCE,
            // Get lengths sum
            Addition.INTEGER,
            // Invoke Arrays.copyOf
            MethodInvocation.invoke(
                new MethodDescription.ForLoadedMethod(ARRAYS_COPYOF)
            ),
            // Store the copy
            storeSum,
            // Doing System.arraycopy(<curreant>, 0, <sum>, <super>.length, <current>.length);
            // Load arraycopy's first arg
            loadCurrent,
            // second arg
            IntegerConstant.ZERO,
            // third
            loadSum,
            // fourth
            loadSuper,
            ArrayLength.INSTANCE,
            // fifth
            loadCurrent,
            ArrayLength.INSTANCE,
            // invocation
            MethodInvocation.invoke(
                new MethodDescription.ForLoadedMethod(SYSTEM_ARRAYCOPY)
            )
        );
        return result;
    }
}
