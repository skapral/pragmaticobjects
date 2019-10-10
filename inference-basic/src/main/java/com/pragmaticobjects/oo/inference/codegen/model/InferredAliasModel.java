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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author skapral
 */
public class InferredAliasModel implements ImportsProvider {
    private final Type _this;
    private final Type _baseInference;
    private final Type _interface;
    private final Type inference;
    private final Collection<Argument> args;

    public InferredAliasModel(Type _this, Type _baseInference, Type _interface, Type inference, Collection<Argument> args) {
        this._this = _this;
        this._baseInference = _baseInference;
        this._interface = _interface;
        this.inference = inference;
        this.args = args;
    }

    

    public InferredAliasModel(Type _this, Type _baseInference, Type _interface, Type inference, Argument... args) {
        this(_this, _baseInference, _interface, inference, Arrays.asList(args));
    }

    public final Type getThis() {
        return _this;
    }

    public final Type getBaseInference() {
        return _baseInference;
    }

    public final Type getInterface() {
        return _interface;
    }

    public final Type getInference() {
        return inference;
    }

    public final Collection<Argument> getArgs() {
        return args;
    }
    
    @Override
    public final Collection<Type> getImports() {
        HashSet<Type> set = new HashSet<>();
        set.add(inference);
        for(Argument arg : args) {
            set.addAll(arg.getImports());
        }
        return set;
    }
}
