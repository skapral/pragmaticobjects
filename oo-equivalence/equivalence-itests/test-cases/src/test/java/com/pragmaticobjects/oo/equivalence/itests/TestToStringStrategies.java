/*-
 * ===========================================================================
 * equivalence-itests.test-cases
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
package com.pragmaticobjects.oo.equivalence.itests;

import com.pragmaticobjects.oo.equivalence.assertions.AssertObjectToString;
import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;
import com.pragmaticobjects.oo.equivalence.itests.fieldlevelhint.HexHint;
import com.pragmaticobjects.oo.equivalence.itests.fieldlevelhint.IterableHexes;
import com.pragmaticobjects.oo.equivalence.itests.fieldlevelhint.StubHint;

public class TestToStringStrategies extends TestsSuite {
    public TestToStringStrategies() {
        super(
            new TestCase(
                "stub hint",
                new AssertObjectToString(
                    new StubHint("test", "test"),
                    "StubHint(test, ...)"
                )
            ),
            new TestCase(
                "hex hint",
                new AssertObjectToString(
                    new HexHint(
                        65,
                        65,
                        (short) 65,
                        'A',
                        (byte) 65,
                        true,
                        65L,
                        "65"
                    ),
                    "HexHint(65, 0x41, 0x41, A, 0x41, true, 0x41, 65)"
                )
            ),
            new TestCase(
                "array of hexes hint",
                new AssertObjectToString(
                    new IterableHexes(),
                    "IterableHexes([0x01, 0x02, 0x03], [0x01, 0x02, 0x03])"
                )
            )
        );
    }
}
