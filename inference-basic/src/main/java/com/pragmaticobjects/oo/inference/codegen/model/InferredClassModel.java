/*-
 * ===========================================================================
 * inference-basic
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
package com.pragmaticobjects.oo.inference.codegen.model;

import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import java.util.Arrays;
import java.util.Collection;


public class InferredClassModel implements ImportsProvider {
    private final Type _this;
    private final Type _interface;
    private final Collection<Method> _methods;

    public InferredClassModel(Type _this, Type _interface, Collection<Method> _methods) {
        this._this = _this;
        this._interface = _interface;
        this._methods = _methods;
    }

    public InferredClassModel(Type _this, Type _interface, Method... _methods) {
        this(_this, _interface, Arrays.asList(_methods));
    }

    public final Type getThis() {
        return _this;
    }
    
    public final Type getInterface() {
        return _interface;
    }

    public final Collection<Method> getMethods() {
        return _methods;
    }

    @Override
    public final Collection<Type> getImports() {
        return new java.util.HashSet<>(
            HashSet.of(_interface).addAll(List.ofAll(_methods).flatMap(m -> m.getImports()))
                .filter(t -> !t.isPrimitive())
                .filter(t -> !t.packageName.startsWith("java.lang"))
                .filter(t -> !t.packageName.equals(_this.packageName))
                .toJavaSet()
        );
    }
}

