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
package com.pragmaticobjects.oo.equivalence.codegen.plugin;

import com.pragmaticobjects.oo.equivalence.base.EObject;
import io.vavr.API;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import io.vavr.collection.List;
import java.lang.reflect.Method;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.collection.ArrayFactory;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatchers;



/**
 *
 * @author skapral
 */
public class ImplementEObjectAttributesPlugin implements Plugin {
    @Override
    public DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, TypeDescription typeDescription) {
        if(!typeDescription.isAssignableTo(EObject.class)) {
            return builder;
        }
        
        StackManipulation attributesImpl = new StackManipulation.Compound(
            List.ofAll(typeDescription.getDeclaredFields())
                .map(field -> loadAttribute(field))
                .transform(list -> ArrayFactory.forType(TypeDescription.Generic.OBJECT).withValues(list.asJava())),
            MethodReturn.REFERENCE
        );
        return builder
                .method(ElementMatchers.named("attributes")).intercept(new Implementation(attributesImpl));
    }
    
    private StackManipulation loadAttribute(FieldDescription field) {
        StackManipulation loadThis = MethodVariableAccess.REFERENCE.loadFrom(0);
        StackManipulation readAttr = FieldAccess.forField(field).read();
        if(field.getType().isPrimitive()) {
            readAttr = new StackManipulation.Compound(
                readAttr,
                new Box(field.getType().asErasure())
            );
        }
        return new StackManipulation.Compound(loadThis, readAttr);
    }
}



/**
 * {@link Implementation} for {@link BtGenerateMethod}
 *
 * @author Kapralov Sergey
 */
class Implementation implements net.bytebuddy.implementation.Implementation {
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


final class Box implements StackManipulation {
    private static final Method BOOLEAN_VALUEOF;
    private static final Method BYTE_VALUEOF;
    private static final Method CHAR_VALUEOF;
    private static final Method SHORT_VALUEOF;
    private static final Method INT_VALUEOF;
    private static final Method LONG_VALUEOF;
    private static final Method FLOAT_VALUEOF;
    private static final Method DOUBLE_VALUEOF;

    static {
        try {
            BOOLEAN_VALUEOF = Boolean.class.getMethod("valueOf", boolean.class);
            BYTE_VALUEOF = Byte.class.getMethod("valueOf", byte.class);
            CHAR_VALUEOF = Double.class.getMethod("valueOf", double.class);
            SHORT_VALUEOF = Short.class.getMethod("valueOf", short.class);
            INT_VALUEOF = Integer.class.getMethod("valueOf", int.class);
            LONG_VALUEOF = Long.class.getMethod("valueOf", long.class);
            FLOAT_VALUEOF = Float.class.getMethod("valueOf", float.class);
            DOUBLE_VALUEOF = Double.class.getMethod("valueOf", double.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private final TypeDescription type;

    /**
     * Ctor.
     * 
     * @param type a type to box
     */
    public Box(TypeDescription type) {
        this.type = type;
    }

    @Override
    public final Size apply(MethodVisitor arg0, net.bytebuddy.implementation.Implementation.Context arg1) {
        StackManipulation sm = API.Match(type).<StackManipulation>of(
            Case(
                $(t -> t.represents(boolean.class)),
                MethodInvocation.invoke(new MethodDescription.ForLoadedMethod(BOOLEAN_VALUEOF))
            ),
            Case(
                $(t -> t.represents(byte.class)),
                MethodInvocation.invoke(new MethodDescription.ForLoadedMethod(BYTE_VALUEOF))
            ),
            Case(
                $(t -> t.represents(char.class)),
                MethodInvocation.invoke(new MethodDescription.ForLoadedMethod(CHAR_VALUEOF))
            ),
            Case(
                $(t -> t.represents(short.class)),
                MethodInvocation.invoke(new MethodDescription.ForLoadedMethod(SHORT_VALUEOF))
            ),
            Case(
                $(t -> t.represents(int.class)),
                MethodInvocation.invoke(new MethodDescription.ForLoadedMethod(INT_VALUEOF))
            ),
            Case(
                $(t -> t.represents(long.class)),
                MethodInvocation.invoke(new MethodDescription.ForLoadedMethod(LONG_VALUEOF))
            ),
            Case(
                $(t -> t.represents(float.class)),
                MethodInvocation.invoke(new MethodDescription.ForLoadedMethod(FLOAT_VALUEOF))
            ),
            Case(
                $(t -> t.represents(double.class)),
                MethodInvocation.invoke(new MethodDescription.ForLoadedMethod(DOUBLE_VALUEOF))
            ),
            Case(
                $(),
                () -> {
                    throw new RuntimeException(
                        String.format("Attempt to box non-primitive type %s", type.getName())
                    );
                }
            )
        );
        return sm.apply(arg0, arg1);
    }

    public final boolean isValid() {
        return true;
    }
}
