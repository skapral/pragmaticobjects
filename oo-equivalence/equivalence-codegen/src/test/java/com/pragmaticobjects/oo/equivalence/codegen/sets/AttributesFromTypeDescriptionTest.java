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
package com.pragmaticobjects.oo.equivalence.codegen.sets;

import com.pragmaticobjects.oo.equivalence.assertions.TestCase;
import com.pragmaticobjects.oo.equivalence.assertions.TestsSuite;
import net.bytebuddy.matcher.ElementMatchers;

/**
 *
 * @author skapral
 */
public class AttributesFromTypeDescriptionTest extends TestsSuite {
    public AttributesFromTypeDescriptionTest() {
        super(
            new TestCase(
                "attributes of a simple class",
                new AssertAttributesMatch(
                    new AttributesFromTypeDescription(Type1.class),
                    ElementMatchers.fieldType(int.class),
                    ElementMatchers.fieldType(int.class)
                )
            ),
            new TestCase(
                "attributes of an abstract class",
                new AssertAttributesMatch(
                    new AttributesFromTypeDescription(Type2.class),
                    ElementMatchers.fieldType(int.class)
                )
            ),
            new TestCase(
                "attributes of subclass",
                new AssertAttributesMatch(
                    new AttributesFromTypeDescription(Type3.class),
                    ElementMatchers.fieldType(int.class),
                    ElementMatchers.fieldType(String.class)
                )
            ),
            new TestCase(
                "attributes of anonymous class",
                new AssertAttributesMatch(
                    new AttributesFromTypeDescription(Interface1.instance(42).getClass()),
                    ElementMatchers.fieldType(int.class)
                )
            )
        );
    }
    
    private static class Type1 {
        private final int a;
        private final int b;

        public Type1(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }
    
    private static abstract class Type2 {
        private final int a;
        
        public Type2(int a) {
            this.a = a;
        }
    }
    
    public static class Type3 extends Type2 {
        private final String b;

        public Type3(int a, String b) {
            super(a);
            this.b = b;
        }
    }
    
    public interface Interface1 {
        int value();
        
        static Interface1 instance(int value) {
            return new Interface1() {
                @Override
                public final int value() {
                    return value;
                }
            };
        }
    }
    
    
}
