/*-
 * ===========================================================================
 * equivalence-itests.test-module2
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
package com.pragmaticobjects.oo.equivalence.itests.fieldlevelhint;

import com.pragmaticobjects.oo.equivalence.base.EquivalenceHint;
import io.vavr.collection.List;

/**
 * Test fixture class with array and {@link io.vavr.collection.List} fields annotated to use
 * {@link HexedIterable} as the toString strategy, used to verify iterable hex rendering.
 *
 * @author Kapralov Sergey
 */
public class IterableHexes {
    private static final int[] REF_ARRAY = new int[] {1, 2, 3};
    private static final List<Integer> REF_LIST = List.of(1, 2, 3);

    private final @EquivalenceHint(enabled = false, toStringMethod = HexedIterable.class) int[] refArray;
    private final @EquivalenceHint(enabled = false, toStringMethod = HexedIterable.class) List<Integer> refList;

    /**
     * Ctor.
     *
     * @param refArray int array rendered with {@link HexedIterable}
     * @param refList  integer list rendered with {@link HexedIterable}
     */
    public IterableHexes(int[] refArray, List<Integer> refList) {
        this.refArray = refArray;
        this.refList = refList;
    }

    /**
     * Ctor. Uses default reference values.
     */
    public IterableHexes() {
        this(
            REF_ARRAY,
            REF_LIST
        );
    }
}
