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

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.vavr.collection.List;
import java.util.function.Supplier;
import javax.lang.model.element.Modifier;

/**
 *
 * @author skapral
 */
public class Interface implements Supplier<TypeSpec> {
    private final String name;
    private final List<Supplier<MethodSpec>> methods;
    private final List<Supplier<TypeName>> types;

    public Interface(String name, List<Supplier<MethodSpec>> methods, List<Supplier<TypeName>> types) {
        this.name = name;
        this.methods = methods;
        this.types = types;
    }
    
    @Override
    public TypeSpec get() {
        TypeSpec.Builder interfaceBuilder = TypeSpec.interfaceBuilder(name);
        interfaceBuilder = interfaceBuilder.addModifiers(Modifier.PUBLIC);
        interfaceBuilder = interfaceBuilder.addMethods(methods.map(Supplier::get));
        interfaceBuilder = interfaceBuilder.addSuperinterfaces(types.map(Supplier::get));
        return interfaceBuilder.build();
    }
}


