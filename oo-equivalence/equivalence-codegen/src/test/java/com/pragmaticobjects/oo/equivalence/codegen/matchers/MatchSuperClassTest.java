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

import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;
import java.io.Serializable;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;


/**
 *
 * @author skapral
 */
public class MatchSuperClassTest extends TestsSuite {
    public MatchSuperClassTest() {
        super(
            new TestCase(
                "match object's direct subclass",
                new AssertThatTypeMatches(
                    new TypeDescription.ForLoadedType(Integer.class),
                    new MatchSuperClass(
                        new MatchClass(Number.class)
                    )
                )
            ),
            new TestCase(
                "mismatch object's indirect subclass",
                new AssertThatTypeDoesNotMatch(
                    new TypeDescription.ForLoadedType(Integer.class),
                    new MatchSuperClass(
                        new MatchClass(Object.class)
                    )
                )
            ),
            new TestCase(
                "mismatch interface",
                new AssertThatTypeDoesNotMatch(
                    new TypeDescription.ForLoadedType(Serializable.class),
                    new MatchSuperClass(
                        new MatchClass(Object.class)
                    )
                )
            ),
            new TestCase(
                "mismatch object",
                new AssertThatTypeDoesNotMatch(
                    new TypeDescription.ForLoadedType(Object.class),
                    new MatchSuperClass(
                        new MatchClass(Object.class)
                    )
                )
            )
        );
    }
}

class MatchClass implements ElementMatcher<TypeDescription> {
    private final Class<?> clazz;

    public MatchClass(Class<?> clazz) {
        this.clazz = clazz;
    }
    
    @Override
    public final boolean matches(TypeDescription td) {
        return td.represents(clazz);
    }
}
