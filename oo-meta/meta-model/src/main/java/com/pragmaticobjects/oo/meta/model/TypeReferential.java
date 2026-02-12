/*-
 * ===========================================================================
 * meta-model
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2026 Kapralov Sergey
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
package com.pragmaticobjects.oo.meta.model;

import io.vavr.collection.List;

import java.util.Collection;

/**
 *
 * @author skapral
 */
public class TypeReferential implements Type {
    private final String packageName;
    private final String name;
    private final Generic generic;

    public TypeReferential(String packageName, String name) {
        this(packageName, name, new GenericEmpty());
    }

    public TypeReferential(String packageName, String name, Generic generic) {
        this.packageName = packageName;
        this.name = name;
        this.generic = generic;
    }

    @Override
    public final boolean isPrimitive() {
        return false;
    }

    @Override
    public final String getFullName() {
        return packageName + "." + name;
    }

    @Override
    public final String packageName() {
        return packageName;
    }

    @Override
    public final String name() {
        return name;
    }

    @Override
    public final Generic generic() {
        return generic;
    }

    @Override
    public final String declaration() {
        String generic = generic().asString().isEmpty() ? "" : "<" + generic().asString() + ">";
        return name() + generic;
    }

    @Override
    public final Collection<Type> getImports() {
        return List.<Type>of(this)
                .appendAll(generic.getImports())
                .asJava();
    }
}
