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
package com.pragmaticobjects.oo.meta.base.ifaces;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import io.vavr.collection.List;
import io.vavr.control.Option;

/**
 *
 * @author skapral
 */
public class MethodWithBody implements Method {
    private final Type returns;
    private final String name;
    private final List<IdentityAttribute> arguments;
    private final Option<CodeBlock> body;

    private MethodWithBody(Type returns, String name, List<IdentityAttribute> arguments, Option<CodeBlock> body) {
        this.returns = returns;
        this.name = name;
        this.arguments = arguments;
        this.body = body;
    }
    
    public MethodWithBody(Type returns, String name, List<IdentityAttribute> arguments, CodeBlock body) {
        this(returns, name, arguments, Option.of(body));
    }
    
    @Override
    public final TypeName returns() {
        return returns.type();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public List<IdentityAttribute> args() {
        return arguments;
    }

    @Override
    public Option<CodeBlock> body() {
        return body;
    }
}
