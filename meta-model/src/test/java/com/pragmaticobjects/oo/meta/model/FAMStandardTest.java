/*-
 * ===========================================================================
 * project-name
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
package com.pragmaticobjects.oo.meta.model;

import com.pragmaticobjects.oo.meta.freemarker.AssertFreemarkerModelValue;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import java.util.Collection;

/**
 *
 * @author skapral
 */
public class FAMStandardTest extends TestsSuite {
    public FAMStandardTest() {
        super(
            new TestCase(
                "this",
                new AssertFreemarkerModelValue(
                    new FAMStandard(
                        new TypeReferential("com.test", "TestObject"),
                        HashMap.empty()
                    ),
                    "this",
                    new TypeReferential("com.test", "TestObject")
                )
            ),
            new TestCase(
                "imports",
                new AssertFreemarkerModelValue(
                    new FAMStandard(
                        new TypeReferential("com.test", "TestObject"),
                        HashMap.<String, Object>of(
                            "somedependency", new TypeReferential("com.dependency", "Dependency")
                        )
                    ),
                    "imports",
                    List.<Type>of(
                        new TypeReferential("com.dependency", "Dependency")
                    ).asJava()
                )
            )
        );
    }
}

class DummyType implements Type {
    @Override
    public final boolean isPrimitive() {
        return false;
    }

    @Override
    public final String getFullName() {
        return packageName() + "." + name();
    }

    @Override
    public final String packageName() {
        return "com.test";
    }

    @Override
    public final String name() {
        return "DummyType";
    }

    @Override
    public final Collection<Type> getImports() {
        return List.<Type>of(
            new TypeReferential("com.test", "Dependency")
        ).asJava();
    }
}