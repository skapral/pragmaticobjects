/*-
 * ===========================================================================
 * meta-freemarker
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 Kapralov Sergey
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
package com.pragmaticobjects.oo.meta.freemarker;

import com.pragmaticobjects.oo.meta.base.AssertArtifactContents;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;

/**
 *
 * @author skapral
 */
public class FreemarkerArtifactTest extends TestsSuite {
    public FreemarkerArtifactTest() {
        super(
            new TestCase(
                "Elegant-model test",
                new AssertArtifactContents(
                    new FreemarkerArtifact(
                        "testTemplate",
                        new ElegantModel("Hello", "World")
                    ),
                    String.join("\r\n",
                        "Hello, World!"
                    )
                )
            ),
                new TestCase(
                "Generic model test",
                new AssertArtifactContents(
                    new FreemarkerArtifact(
                        "testTemplate",
                        new GenericModel("Hello", "World")
                    ),
                    String.join("\r\n",
                        "Hello, World!"
                    )
                )
            )
        );
    }
    
    public static class ElegantModel {
        private final String greeting;
        private final String name;

        public ElegantModel(String greeting, String name) {
            this.greeting = greeting;
            this.name = name;
        }
        
        public final String greeting() {
            return greeting;
        }
        
        public final String name() {
            return name;
        }
    }
    
    public static class GenericModel {
        private final String greeting;
        private final String name;

        public GenericModel(String greeting, String name) {
            this.greeting = greeting;
            this.name = name;
        }
        
        public final Object get(String index) {
            switch(index) {
                case "greeting": {
                    return greeting;
                }
                case "name": {
                    return name;
                }
                default: {
                    throw new RuntimeException();
                }
            }
        }
    }
}
