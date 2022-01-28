/*-
 * ===========================================================================
 * equivalence-codegen
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
package com.pragmaticobjects.oo.equivalence.codegen.cn;

import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;


/**
 * Tests suite for {@link CnExcludingPackages}
 *
 * @author Kapralov Sergey
 */
class CnExcludingPackagesTest extends TestsSuite {
    /**
     * Ctor.
     */
    public CnExcludingPackagesTest() {
        super(
            new TestCase(
                "Excluding nothing",
                new AssertClassNamesContainCertainNames(
                    new CnExcludingPackages(
                        new CnExplicit(
                            "com.package1.Foo",
                            "com.package2.Bar"
                        )
                    ),
                    "com.package1.Foo",
                    "com.package2.Bar"
                )
            ),
            new TestCase(
                "Excluding a single package",
                new AssertClassNamesContainCertainNames(
                    new CnExcludingPackages(
                        new CnExplicit(
                            "com.package1.Foo",
                            "com.package2.Bar"
                        ),
                        "com.package1"
                    ),
                    "com.package2.Bar"
                )
            ),
            new TestCase(
                "Excluding non-existing package",
                new AssertClassNamesContainCertainNames(
                    new CnExcludingPackages(
                        new CnExplicit(
                            "com.package1.Foo",
                            "com.package2.Bar"
                        ),
                        "com.package"
                    ),
                    "com.package1.Foo",
                    "com.package2.Bar"
                )
            )
        );
    }
}
