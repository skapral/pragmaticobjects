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
package com.pragmaticobjects.oo.equivalence.codegen.matchers;

import com.pragmaticobjects.oo.equivalence.assertions.AssertCombined;
import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;
import com.pragmaticobjects.oo.equivalence.base.EObject;

public class AliasesDoesntDeclareAdditionalFieldsAndMethodsTest extends TestsSuite {
    public AliasesDoesntDeclareAdditionalFieldsAndMethodsTest() {
        super(
            new TestCase(
                "match non-alias (direct eobject implementor)",
                new AssertThatTypeMatches(
                    Base.class,
                    new AliasesDoesntDeclareAdditionalFieldsAndMethods()
                )
            ),
            new TestCase(
                "match alias (eobject inheritor)",
                new AssertThatTypeMatches(
                    Base2Correct.class,
                    new AliasesDoesntDeclareAdditionalFieldsAndMethods()
                )
            ),
            new TestCase(
                "mismatch incorrect alias (eobject inheritor)",
                new AssertCombined(
                    new AssertThatTypeDoesNotMatch(
                        Base2Incorrect_ExtraFields.class,
                        new AliasesDoesntDeclareAdditionalFieldsAndMethods()
                    ),
                    new AssertThatTypeDoesNotMatch(
                        Base2Incorrect_ExtraMethods.class,
                        new AliasesDoesntDeclareAdditionalFieldsAndMethods()
                    )
                )
            ),
            new TestCase(
                "match non-alias (direct eobject abstract implementor)",
                new AssertThatTypeMatches(
                    Abstract.class,
                    new AliasesDoesntDeclareAdditionalFieldsAndMethods()
                )
            ),
            new TestCase(
                "match non-alias (abstract eobject inheritors)",
                new AssertThatTypeMatches(
                    Abstract2.class,
                    new AliasesDoesntDeclareAdditionalFieldsAndMethods()
                )
            ),
            new TestCase(
                "match non-alias (abstract eobject implementor)",
                new AssertThatTypeMatches(
                    AbstractBase.class,
                    new AliasesDoesntDeclareAdditionalFieldsAndMethods()
                )
            ),
            new TestCase(
                "match alias (abstract eobject implementor's inheriotor)",
                new AssertThatTypeMatches(
                    AbstractBase2Correct.class,
                    new AliasesDoesntDeclareAdditionalFieldsAndMethods()
                )
            ),
            new TestCase(
                "mismatch incorrect alias (abstract eobject implementor's inheriotor)",
                new AssertThatTypeDoesNotMatch(
                    AbstractBase2Incorrect.class,
                    new AliasesDoesntDeclareAdditionalFieldsAndMethods()
                )
            )
        );
    }
    
    public static class Base extends EObject {
        private final int x;
        private final int y;

        public Base(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        protected final Object[] attributes() {
            return new Object[] {x, y};
        }

        @Override
        protected final int hashSeed() {
            return 31;
        }

        @Override
        protected final Class<?> baseType() {
            return Base.class;
        }    
    }
    
    public static class Base2Correct extends Base {
        public Base2Correct() {
            super(1, 2);
        }
    }
    
    public static class Base2Incorrect_ExtraFields extends Base {
        private final int z;
        
        public Base2Incorrect_ExtraFields(int x, int y, int z) {
            super(x, y);
            this.z = z;
        }
    }
    
    public static class Base2Incorrect_ExtraMethods extends Base {
        public Base2Incorrect_ExtraMethods() {
            super(1, 2);
        }
        
        public final void additionalMethod() {}
    }
    
    public static abstract class Abstract extends EObject {
        protected final int x;

        public Abstract(int x) {
            this.x = x;
        }

        public final int getX() {
            return x;
        }
    }
    
    public static abstract class Abstract2 extends Abstract {
        protected final int y;

        public Abstract2(int x, int y) {
            super(x);
            this.y = y;
        }

        public final int getY() {
            return y;
        }
    }
    
    public static class AbstractBase extends Abstract {
        protected final int y;

        public AbstractBase(int x, int y) {
            super(x);
            this.y = y;
        }

        public final int getY() {
            return y;
        }

        @Override
        protected final Object[] attributes() {
            return new Object[] {x, y};
        }

        @Override
        protected final int hashSeed() {
            return 31;
        }

        @Override
        protected final Class<?> baseType() {
            return AbstractBase.class;
        }
    }
    
    public static class AbstractBase2Correct extends AbstractBase {
        public AbstractBase2Correct() {
            super(1, 2);
        }
    }
    
    public static class AbstractBase2Incorrect extends AbstractBase {
        private final int z;
        
        public AbstractBase2Incorrect(int x, int y, int z) {
            super(x, y);
            this.z = z;
        }

        public final int getZ() {
            return z;
        }
    }
}
