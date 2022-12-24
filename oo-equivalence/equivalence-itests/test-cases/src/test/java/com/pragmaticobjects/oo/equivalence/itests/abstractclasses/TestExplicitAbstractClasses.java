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
package com.pragmaticobjects.oo.equivalence.itests.abstractclasses;

import com.pragmaticobjects.oo.equivalence.assertions.AssertAssertionFails;
import com.pragmaticobjects.oo.equivalence.assertions.AssertCombined;
import com.pragmaticobjects.oo.equivalence.assertions.AssertSubtypeOf;
import com.pragmaticobjects.oo.equivalence.assertions.AssertTwoObjectsEquality;
import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;
import com.pragmaticobjects.oo.equivalence.base.EObject;
import com.pragmaticobjects.oo.equivalence.itests.module1.abstractclasses.ExternalAbstractEObject;
import com.pragmaticobjects.oo.equivalence.itests.module1.abstractclasses.ExternalAbstractNonEObject;
import com.pragmaticobjects.oo.equivalence.itests.module1.abstractclasses.ExternalAbstractPlainObject;

public class TestExplicitAbstractClasses extends TestsSuite {
    private static final Object KEY1 = new Object();
    private static final Object KEY2 = new Object();
    
    
    public TestExplicitAbstractClasses() {
        super(
            new TestCase(
                "types hierarchy is preserved",
                new AssertCombined(
                    new AssertSubtypeOf(
                        PlainObjectImpl.class,
                        ExternalAbstractPlainObject.class
                    ),
                    new AssertSubtypeOf(
                        EObjectImpl.class,
                        ExternalAbstractEObject.class
                    ),
                    new AssertSubtypeOf(
                        NonEObjectImpl.class,
                        ExternalAbstractNonEObject.class
                    )
                )
            ),
            new TestCase(
                "EObject hints are taken into account",
                new AssertCombined(
                    new AssertAssertionFails(
                        new AssertSubtypeOf(
                            PlainObjectImpl.class,
                            EObject.class,
                            true
                        )
                    ),
                    new AssertSubtypeOf(
                        EObjectImpl.class,
                        EObject.class,
                        true
                    ),
                    new AssertAssertionFails(
                        new AssertSubtypeOf(
                            NonEObjectImpl.class,
                            EObject.class,
                            true
                        )
                    )
                )
            ),
            new TestCase(
                "attributes of base class(es) are participating in eqivalence calculation",
                new AssertCombined(
                    new AssertTwoObjectsEquality(
                        new EObjectImpl(KEY1),
                        new EObjectImpl(KEY1),
                        true
                    ),
                    new AssertTwoObjectsEquality(
                        new EObjectImpl(KEY1),
                        new EObjectImpl(KEY2),
                        false
                    )
                )
            )
        );
    }
}
