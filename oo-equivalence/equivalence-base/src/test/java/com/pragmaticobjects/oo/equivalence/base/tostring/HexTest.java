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

import com.pragmaticobjects.oo.equivalence.assertions.AssertCombined;
import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;
import io.vavr.collection.List;

public class HexTest extends TestsSuite {
    public HexTest() {
        super(
                new TestCase(
                    "Hex with default prefix formats Integer as hex with '0x' prefix",
                    new AssertCombined(
                        new AssertStringifyResult(
                                new Hex(),
                                10,
                                "0x0a"
                        ),
                        new AssertStringifyResult(
                            new Hex(),
                            255,
                            "0xff"
                        ),
                        new AssertStringifyResult(
                            new Hex(),
                            260,
                            "0x0104"
                        ),
                        new AssertStringifyResult(
                            new Hex(),
                            6666,
                            "0x1a0a"
                        )
                    )
                ),
                new TestCase(
                        "Hex with custom prefix formats Integer using that prefix",
                        new AssertStringifyResult(
                                new Hex("#"),
                                255,
                                "#ff"
                        )
                ),
                new TestCase(
                        "Hex with default prefix formats Long as hex with '0x' prefix",
                        new AssertStringifyResult(
                                new Hex(),
                                255L,
                                "0xff"
                        )
                ),
                new TestCase(
                        "Hex with default prefix formats Short as hex with '0x' prefix",
                        new AssertStringifyResult(
                                new Hex(),
                                (short) 255,
                                "0xff"
                        )
                ),
                new TestCase(
                        "Hex with default prefix formats Byte as hex with '0x' prefix",
                        new AssertStringifyResult(
                                new Hex(),
                                (byte) 15,
                                "0x0f"
                        )
                ),
                new TestCase(
                        "Hex with default prefix formats Float via Float.toHexString",
                        new AssertStringifyResult(
                                new Hex(),
                                1.0f,
                                "0x" + Float.toHexString(1.0f)
                        )
                ),
                new TestCase(
                        "Hex with default prefix formats Double via Double.toHexString",
                        new AssertStringifyResult(
                                new Hex(),
                                1.0,
                                "0x" + Double.toHexString(1.0)
                        )
                ),
                new TestCase(
                        "Hex delegates non-numeric objects to Default (no '0x' prefix)",
                        new AssertStringifyResult(
                                new Hex(),
                                "hello",
                                new Default().stringify("hello")
                        )
                )
        );
    }
}
