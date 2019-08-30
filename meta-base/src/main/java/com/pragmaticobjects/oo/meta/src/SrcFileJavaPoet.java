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
package com.pragmaticobjects.oo.meta.src;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.util.function.Supplier;
import javax.annotation.processing.ProcessingEnvironment;

/**
 * <a href="https://github.com/square/javapoet">Java poet</a> based source file. 
 * @author skapral
 */
public class SrcFileJavaPoet implements SourceFile {
    private final String packageName;
    private final Supplier<TypeSpec> typeSpec;
    private final Destination dest;

    /**
     * Ctor.
     * @param packageName package name
     * @param typeSpec {@link TypeSpec} source
     * @param dest File destination
     */
    public SrcFileJavaPoet(String packageName, Supplier<TypeSpec> typeSpec, Destination dest) {
        this.packageName = packageName;
        this.typeSpec = typeSpec;
        this.dest = dest;
    }

    /**
     * Ctor.
     * @param packageName package name
     * @param typeSpec {@link TypeSpec} source
     * @param env Processing environment
     */
    public SrcFileJavaPoet(String packageName, Supplier<TypeSpec> typeSpec, ProcessingEnvironment env) {
        this(
            packageName,
            typeSpec,
            new DestFromProcessingEnvironment(env)
        );
    }
    
    @Override
    public final void generate() {
        JavaFile javaFile = JavaFile.builder(
            packageName,
            typeSpec.get()
        ).build();
        dest.persist(javaFile);
    }
}
