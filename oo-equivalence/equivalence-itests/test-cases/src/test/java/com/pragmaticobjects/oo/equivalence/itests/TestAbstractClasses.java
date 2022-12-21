/*-
 * ===========================================================================
 * equivalence-itests.test-cases
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

import com.pragmaticobjects.oo.equivalence.assertions.AssertCombined;
import com.pragmaticobjects.oo.equivalence.assertions.AssertSubtypeOf;
import com.pragmaticobjects.oo.equivalence.assertions.AssertTwoObjectsEquality;
import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;
import com.pragmaticobjects.oo.equivalence.base.EObject;
import com.pragmaticobjects.oo.equivalence.itests.classes.AbstractNode;
import com.pragmaticobjects.oo.equivalence.itests.classes.BaseNode;
import com.pragmaticobjects.oo.equivalence.itests.classes.LeafNode;
import com.pragmaticobjects.oo.equivalence.itests.classes.NonHintedAbstractClass;

/**
 *
 * @author skapral
 */
public class TestAbstractClasses extends TestsSuite {
    public TestAbstractClasses() {
        super(
            new TestCase(
                "types hierarchy is preserved",
                new AssertCombined(
                    new AssertSubtypeOf(
                        AbstractNode.class,
                        EObject.class
                    ),
                    new AssertSubtypeOf(
                        BaseNode.class,
                        AbstractNode.class
                    ),
                    new AssertSubtypeOf(
                        LeafNode.class,
                        AbstractNode.class
                    ),
                    new AssertSubtypeOf(
                        NonHintedAbstractClass.class,
                        Object.class
                    )
                )
            ),
            new TestCase(
                "attributes of base class are participating in eqiovalence calculation",
                new AssertCombined(
                    new AssertTwoObjectsEquality(
                        new LeafNode<Integer>(42),
                        new LeafNode<Integer>(42),
                        true
                    ),
                    new AssertTwoObjectsEquality(
                        new LeafNode<Integer>(42),
                        new LeafNode<Integer>(24),
                        false
                    )
                )
            )
        );
    }
}
