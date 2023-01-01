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
package com.pragmaticobjects.oo.equivalence.codegen.sets;

import com.pragmaticobjects.oo.equivalence.assertions.Assertion;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.assertj.core.api.Assertions;

/**
 *
 * @author skapral
 */
public class AssertAttributesMatch implements Assertion {
    private final Attributes attrs;
    private final List<ElementMatcher<FieldDescription>> fieldMatchers;

    public AssertAttributesMatch(Attributes attrs, List<ElementMatcher<FieldDescription>> fieldMatchers) {
        this.attrs = attrs;
        this.fieldMatchers = fieldMatchers;
    }
    
    public AssertAttributesMatch(Attributes attrs, ElementMatcher<FieldDescription>... fieldMatchers) {
        this(attrs, List.of(fieldMatchers));
    }

    @Override
    public final void check() throws Exception {
        List<FieldDescription> attrs = this.attrs.asList()
                // WA: JaCoCo instrumentation impacts results of the testing. Inner JaCoCo fields should be ignored.
                .filter(ElementMatchers.not(ElementMatchers.nameStartsWith("$jacoco"))::matches);
        fieldMatchers.zipAll(attrs, ElementMatchers.none(), null)
                .forEachWithIndex((Tuple2<ElementMatcher<FieldDescription>, FieldDescription> tuple, int index) -> {
                    ElementMatcher<FieldDescription> matcher = tuple._1;
                    FieldDescription field = tuple._2;
                    Assertions
                        .assertThat(field != null && matcher.matches(field))
                        .withFailMessage("Field #%s mismatched: %s", index, field.getType().getActualName(), field.getActualName())
                        .isTrue();
                });
    }
}
