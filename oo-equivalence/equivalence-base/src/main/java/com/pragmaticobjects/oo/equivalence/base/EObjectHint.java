/*-
 * ===========================================================================
 * equivalence-base
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2023 Kapralov Sergey
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
package com.pragmaticobjects.oo.equivalence.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A hint, telling equivalence instrumentor whether the class should (or should not) be subtype of {@link EObjectContract}.
 * 
 * The class, annotated by this annotation, is allowed to have only the {@link Object} superclass.
 * Attempt to annotate and instrument a class that doesn't fit this requirement will be terminated by exception.
 * 
 * If the class is abstract, the list of implemented interfaces for it is updated with
 * {@link EObjectContract}, and instrumentor handles its non-abstract subtypes as EObjects. Abstract class, that is not annotated by {@link EObjectHint},
 * is not considered as a candidate for instrumentation.
 * 
 * The abstract class with hint enabled must fit these requirements:
 * <ul>
 * <li>All its non-abstract methods should be final</li>
 * <li>All its non-static properties should be protected final</li>
 * </ul>
 * 
 * Instrumentation post-checks will fail the instrumentation process, if any violation found.
 * 
 * For non-abstract classes, the annotation is ignored, unless hint is explicitly disabled ("enabled" property is set to false). In this case, the class will
 * be left uninstrumented.
 *
 * EObjectHint, set on package level, is applied to all classes of package
 *
 * @author skapral
 */
@Target({ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.CLASS)
public @interface EObjectHint {
    boolean enabled() default true;
}
