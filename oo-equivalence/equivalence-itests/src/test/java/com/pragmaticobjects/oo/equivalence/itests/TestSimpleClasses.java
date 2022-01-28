/*-
 * ===========================================================================
 * equivalence-itests
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2022 Kapralov Sergey
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

import com.pragmaticobjects.oo.equivalence.assertions.AssertTwoObjectsEquality;
import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;
import com.pragmaticobjects.oo.equivalence.itests.classes.Counter;
import com.pragmaticobjects.oo.equivalence.itests.classes.GuestList;
import java.util.HashSet;

/**
 *
 * @author skapral
 */
public class TestSimpleClasses extends TestsSuite {
    private static final HashSet<String> HASH_SET = new HashSet<>();
    
    public TestSimpleClasses() {
        super(
            new TestCase(
                "Counter(A) == Counter(A)",
                new AssertTwoObjectsEquality(
                    new Counter("A"),
                    new Counter("A"),
                    true
                )
            ),
            new TestCase(
                "Counter(A) != Counter(B)",
                new AssertTwoObjectsEquality(
                    new Counter("A"),
                    new Counter("B"),
                    false
                )
            ),
            new TestCase(
                "GuestList(set1) != GuestList(set2)",
                new AssertTwoObjectsEquality(
                    new GuestList(new HashSet<String>()),
                    new GuestList(new HashSet<String>()),
                    false
                )
            ),
            new TestCase(
                "GuestList(set) == GuestList(set)",
                new AssertTwoObjectsEquality(
                    new GuestList(HASH_SET),
                    new GuestList(HASH_SET),
                    true
                )
            )
        );
    }
}
