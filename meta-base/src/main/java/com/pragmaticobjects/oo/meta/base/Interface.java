/*-
 * ===========================================================================
 * project-name
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
package com.pragmaticobjects.oo.meta.base;

import com.pragmaticobjects.oo.meta.base.ifaces.Method;
import com.pragmaticobjects.oo.meta.base.ifaces.Type;
import com.pragmaticobjects.oo.meta.src.JavaPoetDefinition;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import io.vavr.collection.List;
import io.vavr.control.Option;
import javax.lang.model.element.Modifier;

/**
 *
 * @author skapral
 */
public class Interface implements JavaPoetDefinition {
    private final String name;
    private final List<Type> superinterfaces;
    private final List<Method> methods;

    public Interface(String name, List<Type> superinterfaces, List<Method> methods) {
        this.name = name;
        this.superinterfaces = superinterfaces;
        this.methods = methods;
    }
    
    @Override
    public final TypeSpec javaPoetSpec() {
        TypeSpec.Builder ifaceBuilder = TypeSpec.interfaceBuilder(name);
        ifaceBuilder.addSuperinterfaces(superinterfaces.map(Type::type));
        ifaceBuilder.addMethods(
            methods.map(m -> {
                MethodSpec.Builder builder = MethodSpec.methodBuilder(m.name());
                builder.returns(m.returns());
                builder.addParameters(
                    m.args().map(a -> ParameterSpec.builder(a.type(), a.name()).build())
                );
                Option<CodeBlock> body = m.body();
                if(body.isEmpty()) {
                    builder.addModifiers(Modifier.ABSTRACT);
                } else {
                    throw new RuntimeException("Non abstract methods are not allowed in interfaces");
                }
                return builder.build();
            })
        );
        return ifaceBuilder.build();
    }
}
