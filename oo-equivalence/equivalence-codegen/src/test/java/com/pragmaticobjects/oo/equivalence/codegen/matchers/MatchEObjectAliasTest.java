/*-
 * ===========================================================================
 * equivalence-codegen
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
package com.pragmaticobjects.oo.equivalence.codegen.matchers;

import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;
import com.pragmaticobjects.oo.equivalence.base.EObject;
import com.pragmaticobjects.oo.equivalence.base.EquivalenceLogic;

/**
 *
 * @author skapral
 */
public class MatchEObjectAliasTest extends TestsSuite {

    public MatchEObjectAliasTest() {
        super(
            new TestCase(
                "mismatch non-alias (non-eobject)",
                new AssertThatTypeDoesNotMatch(
                    NonBase.class,
                    new MatchEObjectAlias()
                )
            ),
            new TestCase(
                "mismatch non-alias (non-eobject inheritor)",
                new AssertThatTypeDoesNotMatch(
                    NonBaseInheritor.class,
                    new MatchEObjectAlias()
                )
            ),
            new TestCase(
                "mismatch non-alias (direct eobject implementor)",
                new AssertThatTypeDoesNotMatch(
                    Base.class,
                    new MatchEObjectAlias()
                )
            ),
            new TestCase(
                "match alias (eobject inheritor)",
                new AssertThatTypeMatches(
                    BaseAlias.class,
                    new MatchEObjectAlias()
                )
            ),
            new TestCase(
                "mismatch non-alias (direct eobject abstract implementor)",
                new AssertThatTypeDoesNotMatch(
                    Abstract.class,
                    new MatchEObjectAlias()
                )
            ),
            new TestCase(
                "mismatch non-alias (abstract eobject inheritors)",
                new AssertThatTypeDoesNotMatch(
                    Abstract2.class,
                    new MatchEObjectAlias()
                )
            ),
            new TestCase(
                "mismatch non-alias (abstract eobject implementor)",
                new AssertThatTypeDoesNotMatch(
                    AbstractBase.class,
                    new MatchEObjectAlias()
                )
            ),
            new TestCase(
                "match alias (abstract eobject implementor's inheriotor)",
                new AssertThatTypeMatches(
                    AbstractBaseAlias.class,
                    new MatchEObjectAlias()
                )
            )
        );
    }
    
    public static class NonBase {}
    
    public static class NonBaseInheritor extends NonBase {}
    
    public static class Base implements EObject {
        private final int x;
        private final int y;

        public Base(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public final Object[] attributes() {
            return new Object[] {x, y};
        }

        @Override
        public final int hashSeed() {
            return 31;
        }

        @Override
        public final Class<?> baseType() {
            return Base.class;
        }

        @Override
        public final boolean equals(Object obj) {
            return EquivalenceLogic.equals(this, obj);
        }

        @Override
        public final int hashCode() {
            return EquivalenceLogic.hashCode(this);
        }

        @Override
        public final String toString() {
            return EquivalenceLogic.toString(this);
        }
    }
    
    public static class BaseAlias extends Base {
        public BaseAlias() {
            super(1, 2);
        }
    }
    
    public static abstract class Abstract implements EObject {
        protected final int x;

        public Abstract(int x) {
            this.x = x;
        }

        public final int getX() {
            return x;
        }

        @Override
        public final boolean equals(Object obj) {
            return EquivalenceLogic.equals(this, obj);
        }

        @Override
        public final int hashCode() {
            return EquivalenceLogic.hashCode(this);
        }

        @Override
        public final String toString() {
            return EquivalenceLogic.toString(this);
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
        public final Object[] attributes() {
            return new Object[] {x, y};
        }

        @Override
        public final int hashSeed() {
            return 31;
        }

        @Override
        public final Class<?> baseType() {
            return AbstractBase.class;
        }
    }
    
    public static class AbstractBaseAlias extends AbstractBase {
        public AbstractBaseAlias() {
            super(1, 2);
        }
    }
}
