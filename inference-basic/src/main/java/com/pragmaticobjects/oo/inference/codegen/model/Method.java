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

import io.vavr.collection.List;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import javax.lang.model.element.ExecutableElement;


public class Method implements ImportsProvider {
    public final Type returns;
    public final String name;
    public final Iterable<Argument> args;

    public static Method from(ExecutableElement t) {
        return new Method(
            Type.from(t.getReturnType()), 
            t.getSimpleName().toString(),
            List.of(t)
                .flatMap(_t -> _t.getParameters())
                .map(tm -> new Argument(Type.from(tm.asType()), tm.getSimpleName().toString()))
                .toJavaList()
        );
    }
    
    public Method(Type returns, String name, Iterable<Argument> args) {
        this.returns = returns;
        this.name = name;
        this.args = args;
    }
    
    public Method(Type returns, String name, Argument... args) {
        this(
            returns,
            name,
            Arrays.asList(args)
        );
    }

    @Override
    public Collection<Type> getImports() {
        HashSet<Type> types = new HashSet<>();
        types.add(returns);
        for(Argument arg : args) {
            types.add(arg.type);
        }
        return types;
    }
}
