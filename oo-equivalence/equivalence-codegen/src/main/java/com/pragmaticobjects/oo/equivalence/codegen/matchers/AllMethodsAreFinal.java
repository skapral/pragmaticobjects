/*-
 * ===========================================================================
 * equivalence-codegen
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2020 Kapralov Sergey
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

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Matcher which matches types, methods of which are final.
 *
 * @author Kapralov Sergey
 */
public class AllMethodsAreFinal implements ElementMatcher<TypeDescription> {
    private static final Logger LOG = LoggerFactory.getLogger(AllMethodsAreFinal.class);
    
    @Override
    public final boolean matches(TypeDescription target) {
        MethodList<MethodDescription.InDefinedShape> methodsToCheck = target.getDeclaredMethods()
                .filter(not(isConstructor()))
                .filter(isPublic())
                .filter(not(isStatic()))
                .filter(not(isAbstract()))
                .filter(not(isBridge()))
                .filter(not(named("equals")))
                .filter(not(named("hashCode")))
                .filter(not(named("toString")));
        
        for(MethodDescription md : methodsToCheck) {
            LOG.debug(md.getDeclaringType().getActualName() + "::" + md.getName());
            if(!md.isFinal()) {
                LOG.debug("false");
                return false;
            }
        }
        LOG.debug("true");
        return true;
    }
}
