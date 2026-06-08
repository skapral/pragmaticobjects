/*-
 * ===========================================================================
 * guidelines-archunit
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
package com.pragmaticobjects.oo.guidelines.archunit.archunit.exclusions;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaCodeUnit;

/**
 * Exclusion record that marks constructor or method calls as permitted within constructor
 * analysis conditions. A call is excluded when both the owning class matches {@code owner}
 * and the called code unit matches {@code target}.
 *
 * @author Kapralov Sergey
 */
public class ConstructorCallsExclusion {
    /** Predicate that matches the class that owns the called code unit. */
    public final DescribedPredicate<JavaClass> owner;
    /** Predicate that matches the called code unit (constructor or method). */
    public final DescribedPredicate<JavaCodeUnit> target;

    /**
     * Ctor.
     *
     * @param owner  predicate matching the class that owns the called code unit
     * @param target predicate matching the called code unit itself
     */
    public ConstructorCallsExclusion(
        DescribedPredicate<JavaClass> owner,
        DescribedPredicate<JavaCodeUnit> target
    ) {
        this.owner = owner;
        this.target = target;
    }
}
