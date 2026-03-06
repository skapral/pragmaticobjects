/*-
 * ===========================================================================
 * equivalence-base
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
package com.pragmaticobjects.oo.equivalence.base.tostring;

import java.lang.reflect.Array;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class Iterating implements ToStringMethod {
    private final ToStringMethod methodForEachItem;
    private final String start;
    private final String end;
    private final String separator;

    public Iterating(ToStringMethod methodForEachItem, String start, String end, String separator) {
        this.methodForEachItem = methodForEachItem;
        this.start = start;
        this.end = end;
        this.separator = separator;
    }

    public Iterating(ToStringMethod methodForEachItem) {
        this(
            methodForEachItem,
            "[",
            "]",
            ", "
        );
    }

    @Override
    public String stringify(Object obj) {
        final String result;
        if (obj != null && obj.getClass().isArray()) {
            // Handles both object arrays and primitive arrays via reflection
            result = IntStream.range(0, Array.getLength(obj))
                .mapToObj(i -> methodForEachItem.stringify(Array.get(obj, i)))
                .collect(Collectors.joining(separator));
        } else if (obj instanceof Iterable) {
            result = StreamSupport.stream(
                    ((Iterable<?>) obj).spliterator(),
                    false
            ).map(methodForEachItem::stringify)
                .collect(Collectors.joining(separator));
        } else {
            return "";
        }
        return start + result + end;
    }
}
