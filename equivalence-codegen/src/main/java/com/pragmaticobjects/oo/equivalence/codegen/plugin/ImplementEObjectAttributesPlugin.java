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

import com.pragmaticobjects.oo.equivalence.codegen.plugin.bb.Box;
import com.pragmaticobjects.oo.equivalence.codegen.plugin.bb.Implementation;
import io.vavr.collection.List;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.collection.ArrayFactory;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.Opcodes;



/**
 *
 * @author skapral
 */
public class ImplementEObjectAttributesPlugin implements Plugin {
    @Override
    public final DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, TypeDescription typeDescription) {
        StackManipulation attributesImpl = new StackManipulation.Compound(
            List.ofAll(typeDescription.getDeclaredFields())
                .map(field -> loadAttribute(field))
                .transform(list -> ArrayFactory.forType(TypeDescription.Generic.OBJECT).withValues(list.asJava())),
            MethodReturn.REFERENCE
        );
        return builder
                .defineMethod("attributes", Object[].class, Opcodes.ACC_PROTECTED | Opcodes.ACC_FINAL)
                .intercept(new Implementation(attributesImpl));
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
