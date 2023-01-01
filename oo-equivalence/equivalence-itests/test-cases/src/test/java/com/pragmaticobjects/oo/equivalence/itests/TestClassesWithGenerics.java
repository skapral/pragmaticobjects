/*-
 * ===========================================================================
 * equivalence-itests.test-cases
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
package com.pragmaticobjects.oo.equivalence.itests;

import com.pragmaticobjects.oo.equivalence.assertions.*;
import com.pragmaticobjects.oo.equivalence.itests.classes.GenericsTest;
import com.pragmaticobjects.oo.equivalence.itests.classes.TreeNode;

/**
 *
 * @author skapral
 */
public class TestClassesWithGenerics extends TestsSuite {
    public TestClassesWithGenerics() {
        super(
            new TestCase(
                "TreeNode(42) == TreeNode(42)",
                new AssertTwoObjectsEquality(
                    new TreeNode<>(42),
                    new TreeNode<>(42),
                    true
                )
            ),
            new TestCase(
                "TreeNode(1, TreeNode(2),  TreeNode(3)) == TreeNode(1, TreeNode(2),  TreeNode(3))",
                new AssertTwoObjectsEquality(
                    new TreeNode<>(1, new TreeNode<>(2), new TreeNode<>(3)),
                    new TreeNode<>(1, new TreeNode<>(2), new TreeNode<>(3)),
                    true
                )
            ),
            new TestCase(
                "TreeNode(42) != TreeNode(43)",
                new AssertTwoObjectsEquality(
                    new TreeNode<>(42),
                    new TreeNode<>(43),
                    false
                )
            ),
            new TestCase(
                "TreeNode(42) != TreeNode(T)",
                new AssertTwoObjectsEquality(
                    new TreeNode<>(42),
                    new TreeNode<>("T"),
                    false
                )
            ),
            new TestCase(
                "TreeNode(1, TreeNode(2),  TreeNode(3)) == TreeNode(1, TreeNode(2),  TreeNode(4))",
                new AssertTwoObjectsEquality(
                    new TreeNode<>(1, new TreeNode<>(2), new TreeNode<>(3)),
                    new TreeNode<>(1, new TreeNode<>(2), new TreeNode<>(4)),
                    false
                )
            ),
            new TestCase(
                "Issue 3 regression test: see https://github.com/skapral/pragmaticobjects/issues/3",
                new AssertAssertionPasses(
                    new AssertImplements(
                        GenericsTest.class,
                        "com.pragmaticobjects.oo.equivalence.itests.classes.GenericsTestIface<io.vavr.collection.Map<java.lang.String, java.lang.Object>>"
                    )
                )
            )
        );
    }
}
