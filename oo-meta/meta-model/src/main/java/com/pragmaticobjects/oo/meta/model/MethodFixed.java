/*-
 * ===========================================================================
 * meta-model
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2022 Kapralov Sergey
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
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * @author skapral
 */
public class MethodFixed implements Method {
    private final Type returns;
    private final String name;
    private final Iterable<Argument> args;
    private final Iterable<Type> exceptions;

    public MethodFixed(Type returns, String name, Iterable<Argument> args, Iterable<Type> exceptions) {
        this.returns = returns;
        this.name = name;
        this.args = args;
        this.exceptions = exceptions;
    }

    @Override
    public final Type returns() {
        return returns;
    }

    @Override
    public final String name() {
        return name;
    }

    @Override
    public final Iterable<Argument> args() {
        return args;
    }

    @Override
    public final Iterable<Type> throwsExceptions() {
        return exceptions;
    }

    @Override
    public final Collection<Type> getImports() {
        HashSet<Type> types = new HashSet<>();
        types.add(returns);
        for (Argument arg : args) {
            types.add(arg.type());
        }
        for (Type ex : exceptions) {
            types.add(ex);
        }
        return types;
    }

    public final String declaration() {
        StringBuilder sb = new StringBuilder();
        sb.append(returns().name()).append(" ");
        sb.append(name());
        sb.append("(");
        sb.append(List.ofAll(args())
            .map(arg -> arg.type().name() + " " + arg.name())
            .collect(Collectors.joining(", ")));
        sb.append(")");
        List<Type> exceptions = List.ofAll(throwsExceptions());
        if(!exceptions.isEmpty()) {
            sb.append(" ").append("throws").append(" ");
            String exceptionsDecl = exceptions.map(e -> e.name()).collect(Collectors.joining(", "));
            sb.append(exceptionsDecl);
        }
        return sb.toString();
    }
}
