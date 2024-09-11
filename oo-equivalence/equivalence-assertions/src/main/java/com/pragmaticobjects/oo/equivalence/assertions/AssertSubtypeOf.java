/*-
 * ===========================================================================
 * equivalence-assertions
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
package com.pragmaticobjects.oo.equivalence.assertions;

import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author skapral
 */
public class AssertSubtypeOf implements Assertion {
    private final Class<?> subtype;
    private final Class<?> type;
    private final boolean deepSearch;

    public AssertSubtypeOf(Class<?> subtype, Class<?> type, boolean deepSearch) {
        this.subtype = subtype;
        this.type = type;
        this.deepSearch = deepSearch;
    }

    public AssertSubtypeOf(Class<?> subtype, Class<?> type) {
        this(subtype, type, false);
    }

    @Override
    public final void check() throws Exception {
        if(deepSearch) {
            Class<?> supertype = subtype.getSuperclass();
            while(supertype != null) {
                if(supertype.equals(type)) {
                    return;
                }
                supertype = supertype.getSuperclass();
            }
            Assertions.fail(
                String.format("Expected %s to be subtype of %s", subtype.getName(), type.getName())
            );
        } else {
            assertThat(subtype.getSuperclass())
                .withFailMessage("Expected %s to be subtype of %s", subtype.getName(), type.getName())
                .isEqualTo(type);
        }
    }
}
