/*-
 * ===========================================================================
 * meta-freemarker
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
package com.pragmaticobjects.oo.meta.freemarker;

import com.pragmaticobjects.oo.meta.base.AssertArtifactContents;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import io.vavr.collection.HashMap;
import java.util.Map;

/**
 *
 * @author skapral
 */
public class FreemarkerArtifactTest extends TestsSuite {
    public FreemarkerArtifactTest() {
        super(
            new TestCase(
                "Object-based-model test",
                new AssertArtifactContents(
                    new FreemarkerArtifact(
                        "testTemplate",
                        new GenericModel(
                            HashMap.<String, Object>of(
                                "this", new ModelObject("Hello", "World")
                            ).toJavaMap()
                        )
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
                        new GenericModel(
                            HashMap.<String, Object>of(
                                "this",
                                HashMap.<String, Object>of(
                                    "greeting", "Hello",
                                    "name", "World"
                                ).toJavaMap()
                            ).toJavaMap()
                        )
                    ),
                    String.join("\r\n",
                        "Hello, World!"
                    )
                )
            )
        );
    }
    
    public static class ModelObject {
        private final String greeting;
        private final String name;

        public ModelObject(String greeting, String name) {
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
    
    public static class GenericModel implements FreemarkerArtifactModel {
        private final Map<String, Object> map;

        public GenericModel(Map<String, Object> map) {
            this.map = map;
        }
        
        @Override
        public final Object get(String index) {
            return map.get(index);
        }
    }
}
