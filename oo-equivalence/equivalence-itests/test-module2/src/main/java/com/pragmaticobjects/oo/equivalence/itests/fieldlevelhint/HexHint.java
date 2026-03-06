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
import com.pragmaticobjects.oo.equivalence.base.tostring.Hex;

public class HexHint {
    private final int ref;
    private final @EquivalenceHint(toStringMethod = Hex.class) int ref2;
    private final @EquivalenceHint(toStringMethod = Hex.class) short ref3;
    private final @EquivalenceHint(toStringMethod = Hex.class) char ref4;
    private final @EquivalenceHint(toStringMethod = Hex.class) byte ref5;
    private final @EquivalenceHint(toStringMethod = Hex.class) boolean ref6;
    private final @EquivalenceHint(toStringMethod = Hex.class) long ref7;
    private final @EquivalenceHint(toStringMethod = Hex.class) String ref8;

    public HexHint(int ref, int ref2, short ref3, char ref4, byte ref5, boolean ref6, long ref7, String ref8) {
        this.ref = ref;
        this.ref2 = ref2;
        this.ref3 = ref3;
        this.ref4 = ref4;
        this.ref5 = ref5;
        this.ref6 = ref6;
        this.ref7 = ref7;
        this.ref8 = ref8;
    }
}
