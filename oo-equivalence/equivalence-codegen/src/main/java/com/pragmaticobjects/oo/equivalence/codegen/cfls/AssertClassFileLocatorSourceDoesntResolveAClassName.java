/*-
 * ===========================================================================
 * equivalence-codegen
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2024 Kapralov Sergey
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
package com.pragmaticobjects.oo.equivalence.codegen.cfls;

import com.pragmaticobjects.oo.equivalence.assertions.Assertion;
import net.bytebuddy.dynamic.ClassFileLocator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Assertion that checks that {@link ClassFileLocator}, produced by {@link ClassFileLocatorSource} under test,
 * doesn't resolve certain class name.
 *
 * @author Kapralov Sergey
 */
public class AssertClassFileLocatorSourceDoesntResolveAClassName implements Assertion {
    private final ClassFileLocatorSource cfls;
    private final String className;

    /**
     * Ctor.
     *
     * @param cfls {@link ClassFileLocatorSource} under test.
     * @param className Expected class name.
     */
    public AssertClassFileLocatorSourceDoesntResolveAClassName(ClassFileLocatorSource cfls, String className) {
        this.cfls = cfls;
        this.className = className;
    }

    @Override
    public final void check() throws Exception {
        final ClassFileLocator.Resolution resolution = cfls.classFileLocator().locate(className);
        assertThat(resolution.isResolved()).isFalse();
    }
}
