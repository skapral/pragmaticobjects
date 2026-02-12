/*-
 * ===========================================================================
 * equivalence-codegen
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
package com.pragmaticobjects.oo.equivalence.codegen.matchers;

import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see MatchEObjectAlias
 * @author skapral
 */
public class ClassDeclaresAdditionalFieldsAndMethods implements ElementMatcher<TypeDescription> {
    private static final Logger log = LoggerFactory.getLogger(ClassDeclaresAdditionalFieldsAndMethods.class);
    
    private static final ElementMatcher<FieldDescription> FIELDS_DECLARED = ElementMatchers.not(
        new DisjunctionMatcher<>(
            ElementMatchers.isTransient(),
            ElementMatchers.isStatic(),
            ElementMatchers.isSynthetic()
        )
    );
    private static final ElementMatcher<MethodDescription> METHODS_DECLARED = ElementMatchers.not(
        new DisjunctionMatcher<>(
            ElementMatchers.isStatic(),
            ElementMatchers.isConstructor(),
            ElementMatchers.isSynthetic()
        )
    );
    private static final ElementMatcher<TypeDescription> DECLARES_FIELDS_OR_METHODS = new DisjunctionMatcher<>(
        ElementMatchers.declaresField(
            FIELDS_DECLARED
        ),
        ElementMatchers.declaresMethod(
            METHODS_DECLARED
        )
    );
    
    
    @Override
    public final boolean matches(TypeDescription td) {
        boolean declaresAnything = DECLARES_FIELDS_OR_METHODS.matches(td);
        log.debug("{} is alias", td.getSimpleName());
        log.debug("  declaresAnything = {}", declaresAnything);
        if(log.isDebugEnabled() && declaresAnything) {
            log.debug("{} declares some fields or methods:", td.getSimpleName());
            td.getDeclaredFields().filter(FIELDS_DECLARED).forEach(field -> log.debug("  Field:  {}", field.toString()));
            td.getDeclaredMethods().filter(METHODS_DECLARED).forEach(field -> log.debug("  Method: {}", field.toString()));
        }
        return declaresAnything;
    }
}
