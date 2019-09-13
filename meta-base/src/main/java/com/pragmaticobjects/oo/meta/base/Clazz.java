/*-
 * ===========================================================================
 * meta-base
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
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;
import io.vavr.collection.List;
import com.pragmaticobjects.oo.meta.base.ifaces.IdentityAttribute;
import com.pragmaticobjects.oo.meta.base.ifaces.StateAttribute;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import io.vavr.control.Option;
import java.util.Optional;
import javax.lang.model.element.Modifier;

/**
 *
 * @author skapral
 */
public class Clazz implements JavaPoetDefinition {
    private final String name;
    private final Option<Type> superclass;
    private final List<Type> superinterfaces;
    private final List<IdentityAttribute> identity;
    private final List<StateAttribute> state;
    private final List<Method> methods;

    public Clazz(String name, Option<Type> superclass, List<Type> superinterfaces, List<IdentityAttribute> identity, List<StateAttribute> state, List<Method> methods) {
        this.name = name;
        this.superclass = superclass;
        this.superinterfaces = superinterfaces;
        this.identity = identity;
        this.state = state;
        this.methods = methods;
    }

    @Override
    public final TypeSpec javaPoetSpec() {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(name);
        if(superclass.isDefined()) {
            classBuilder.superclass(superclass.get().type());
        }
        classBuilder.addSuperinterfaces(superinterfaces.map(Type::type));
        classBuilder.addFields(
            identity.map(id -> FieldSpec.builder(id.type(), id.name(), Modifier.PRIVATE, Modifier.FINAL).build())
        );
        classBuilder.addFields(
            state.map((StateAttribute st) -> {
                Optional<CodeBlock> initializer = st.initializer();
                FieldSpec.Builder builder = FieldSpec.builder(st.type(), st.name(), Modifier.STATIC, Modifier.PRIVATE, Modifier.FINAL);
                initializer.ifPresent(cb -> builder.initializer(cb));
                return builder.build();
            })
        );
        classBuilder.addMethod(
            identity.foldLeft(
                MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC),
                (builder, f) -> builder
                        .addParameter(f.type(), f.name())
                        .addStatement("this.$L = $L", f.name(), f.name())
            ).build()
        );
        classBuilder.addMethods(
            methods.map(m -> {
                MethodSpec.Builder builder = MethodSpec.methodBuilder(m.name());
                builder.addModifiers(Modifier.PUBLIC);
                builder.returns(m.returns());
                builder.addParameters(
                    m.args().map(a -> ParameterSpec.builder(a.type(), a.name()).build())
                );
                Option<CodeBlock> body = m.body();
                if(body.isEmpty()) {
                    builder.addModifiers(Modifier.ABSTRACT);
                } else {
                    builder.addModifiers(Modifier.FINAL);
                    builder.addCode(body.get());
                }
                return builder.build();
            })
        );
        return classBuilder.build();
    }
}
