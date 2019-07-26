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

import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.OpenedClassReader;

/**
 *
 * @author skapral
 */
public class MarkAsEObjectPlugin implements Plugin {

    @Override
    public final DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, TypeDescription typeDescription) {
        return builder.visit(
            new AsmVisitorWrapper() {
                @Override
                public int mergeWriter(int arg0) {
                    return arg0;
                }

                @Override
                public int mergeReader(int arg0) {
                    return arg0;
                }

                @Override
                public ClassVisitor wrap(TypeDescription instrumentedType,
                                         ClassVisitor classVisitor,
                                         net.bytebuddy.implementation.Implementation.Context implementationContext,
                                         TypePool typePool,
                                         FieldList<FieldDescription.InDefinedShape> fields,
                                         MethodList<?> methods,
                                         int writerFlags,
                                         int readerFlags) {
                    return new ClassVisitor(OpenedClassReader.ASM_API, classVisitor) {
                        private boolean wasMarked = false;
                        
                        @Override
                        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                            if("java/lang/Object".equals(superName)) {
                                superName = "com/pragmaticobjects/oo/equivalence/base/EObject";
                                wasMarked = true;
                            }
                            super.visit(version, access, name, signature, superName, interfaces);
                        }

                        @Override
                        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                            if(wasMarked && "<init>".equals(name)) {
                                return new MethodVisitor(OpenedClassReader.ASM_API, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                                    @Override
                                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                                        if("java/lang/Object".equals(owner)) {
                                            owner = "com/pragmaticobjects/oo/equivalence/base/EObject";
                                        }
                                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                                    }
                                };
                            }
                            return super.visitMethod(access, name, descriptor, signature, exceptions);
                        }
                    };
                }
            }
        );
    }
}
