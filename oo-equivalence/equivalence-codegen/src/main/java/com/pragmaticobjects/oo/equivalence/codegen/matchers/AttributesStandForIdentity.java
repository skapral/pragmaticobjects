/*-
 * ===========================================================================
 * equivalence-codegen
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
package com.pragmaticobjects.oo.equivalence.codegen.matchers;

import java.lang.reflect.Modifier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Type matcher, checking that type's attributes can be classified as object's identity
 * (which in turn means, that equivalence definition can be applied to the type)
 * 
 * @author skapral
 */
public class AttributesStandForIdentity implements ElementMatcher<TypeDescription> {
    private static final Logger LOG = LoggerFactory.getLogger(AttributesStandForIdentity.class);
            
    @Override
    public boolean matches(TypeDescription td) {
        final ElementMatcher<FieldDescription> visibilityMatcher = new DisjunctionMatcher<>(
            td.isAbstract() ?
                ElementMatchers.isProtected() :
                ElementMatchers.isPrivate(),
            ElementMatchers.isPublic()
        );
        Stream<FieldDescription.InDefinedShape> stream = td.getDeclaredFields().stream();
        if(LOG.isDebugEnabled()) {
            LOG.debug("Checking AttributesStandForIdentity for " + td.getActualName());
            LOG.debug("{} fields found", td.getDeclaredFields().size());
            stream = stream.peek(item -> {
                LOG.debug(
                    " -> {} {} {}",
                    Modifier.toString(item.getModifiers()),
                    item.getType(),
                    item.getActualName()
                );
            });
        }
        return stream
                .filter(
                    ElementMatchers.not(
                        ElementMatchers.isSynthetic()
                    )::matches
                )
                .filter(
                    ElementMatchers.not(
                        ElementMatchers.isStatic()
                    )::matches
                )
                .map(
                    new ConjunctionMatcher<>(
                        visibilityMatcher,
                        ElementMatchers.isFinal()
                    )::matches
                )
                .collect(Collectors.reducing(true, Boolean::logicalAnd));
    }
}
