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

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.vavr.collection.List;
import java.util.function.Supplier;

/**
 *
 * @author skapral
 */
public class Clazz implements Supplier<TypeSpec> {
    private final String name;
    private final Supplier<TypeName> superclass;
    private final List<Supplier<TypeName>> superinterfaces;
    private final List<Supplier<FieldSpec>> fields;
    private final List<Supplier<MethodSpec>> methods;

    public Clazz(String name, Supplier<TypeName> superclass, List<Supplier<TypeName>> superinterfaces, List<Supplier<FieldSpec>> fields, List<Supplier<MethodSpec>> methods) {
        this.name = name;
        this.superclass = superclass;
        this.superinterfaces = superinterfaces;
        this.fields = fields;
        this.methods = methods;
    }
    
    @Override
    public final TypeSpec get() {
        return TypeSpec.classBuilder(name)
                .superclass(superclass.get())
                .addFields(fields.map(Supplier::get))
                .addSuperinterfaces(superinterfaces.map(Supplier::get))
                .addMethods(methods.map(Supplier::get))
                .build();
    }
}
