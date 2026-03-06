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

import com.pragmaticobjects.oo.equivalence.assertions.Assertion;
import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;
import org.assertj.core.api.Assertions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IteratingTest extends TestsSuite {
    public IteratingTest() {
        super(
            new TestCase(
                "Iterating over a non-empty List produces bracketed, comma-separated string representations",
                new AssertStringifyResult(
                    new Iterating(new Default()),
                    Arrays.asList("hello", "world"),
                    "[hello, world]"
                )
            ),
            new TestCase(
                "Iterating over an empty List produces empty brackets",
                new AssertStringifyResult(
                    new Iterating(new Default()),
                    Collections.emptyList(),
                    "[]"
                )
            ),
            new TestCase(
                "Iterating over a plain object returns an empty string",
                new AssertStringifyResult(
                    new Iterating(new Default()),
                    42,
                    ""
                )
            ),
            new TestCase(
                "Iterating with custom delimiters uses those delimiters",
                new AssertStringifyResult(
                    new Iterating(new Default(), "(", ")", " | "),
                    Arrays.asList("a", "b", "c"),
                    "(a | b | c)"
                )
            ),
            new TestCase(
                "Iterating applies the inner ToStringMethod to each element",
                new AssertStringifyResult(
                    new Iterating(new Hide("***")),
                    Arrays.asList("x", "y", "z"),
                    "[***, ***, ***]"
                )
            ),
            new TestCase(
                "Iterating over a single-element List produces no separator",
                new AssertStringifyResult(
                    new Iterating(new Default()),
                    Collections.singletonList("only"),
                    "[only]"
                )
            ),
            new TestCase(
                    "Iterating over an array of primitives",
                    new AssertStringifyResult(
                        new Iterating(new Hex()),
                        new int[] {1, 2, 3},
                        "[0x01, 0x02, 0x03]"
                    )
            )
        );
    }
}
