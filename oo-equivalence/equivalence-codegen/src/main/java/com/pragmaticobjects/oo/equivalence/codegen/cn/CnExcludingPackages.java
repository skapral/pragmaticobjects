/*-
 * ===========================================================================
 * equivalence-codegen
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
package com.pragmaticobjects.oo.equivalence.codegen.cn;

import io.vavr.collection.List;

/**
 * Class names list, excluding classes from certain packages
 *
 * @author Kapralov Sergey
 */
public class CnExcludingPackages implements ClassNames {
    private final ClassNames delegate;
    private final List<String> packages;

    /**
     * Ctor.
     *
     * @param delegate Source class names
     * @param packages Packages to exclude
     */
    public CnExcludingPackages(ClassNames delegate, List<String> packages) {
        this.delegate = delegate;
        this.packages = packages;
    }

    /**
     * Ctor.
     *
     * @param delegate Source class names
     * @param packages Packages to exclude
     */
    public CnExcludingPackages(ClassNames delegate, String... packages) {
        this(
            delegate,
            List.of(packages)
        );
    }

    @Override
    public final List<String> classNames() {
        List<String> names = this.delegate.classNames();
        for(String pkg : packages) {
            names = names.filter(cn -> !cn.startsWith(pkg + "."));
        }
        return names;
    }
}
