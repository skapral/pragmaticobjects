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
package com.pragmaticobjects.oo.equivalence.codegen.matchers;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import static net.bytebuddy.description.annotation.AnnotationDescription.Loadable;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * Checks whether type has annotation hint of certain type on a class or its package
 * @author skapral
 * @param <Hint> Hint
 */
public class HasHint<Hint extends Annotation> implements ElementMatcher<TypeDescription> {
    private final Class<Hint> annotationType;
    private final Function<Hint, Boolean> annotationAccess;
    private final boolean enabled;

    public HasHint(Class<Hint> annotationType, Function<Hint, Boolean> annotationAccess, boolean enabled) {
        this.annotationType = annotationType;
        this.annotationAccess = annotationAccess;
        this.enabled = enabled;
    }
    
    @Override
    public final boolean matches(TypeDescription target) {
        Loadable<Hint> hint = target.getDeclaredAnnotations().ofType(annotationType);
        hint = Objects.nonNull(hint) ? hint : target.getPackage().getDeclaredAnnotations().ofType(annotationType);
        return Optional.ofNullable(hint)
                .map(Loadable::load)
                .map(a -> annotationAccess.apply(a))
                .map(b -> b.equals(enabled))
                .orElse(false);
    }
}
