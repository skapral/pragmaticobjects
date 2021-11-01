/*-
 * ===========================================================================
 * tests-junit4
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
package com.pragmaticobjects.oo.tests.junit4;

import com.github.kimble.FactoryRunner;
import com.pragmaticobjects.oo.tests.TestCase;
import io.vavr.collection.List;
import java.util.Objects;
import org.junit.runner.RunWith;


/**
 * Internal {@link TestCase} adapter for {@link TestsSuite}.
 * 
 * @author skapral
 */
class TestCaseAdapter implements FactoryRunner.Test {
    private final TestCase testCase;

    /**
     * Ctor.
     * 
     * @param testCase  Test case
     */
    public TestCaseAdapter(TestCase testCase) {
        this.testCase = testCase;
    }

    @Override
    public final void execute() throws Throwable {
        testCase.execute();
    }
}


/**
 * A tests suite
 * 
 * @author Kapralov Sergey
 */
@RunWith(FactoryRunner.class)
public class TestsSuite implements FactoryRunner.Producer {
    private final List<TestCase> testCases;

    /**
     * Ctor.
     * 
     * @param testCases Test cases
     */
    public TestsSuite(TestCase... testCases) {
        this(
            List.of(testCases)
        );
    }
    
    /**
     * Ctor.
     * 
     * @param testCases Test cases
     */
    public TestsSuite(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    @Override
    public final void produceTests(FactoryRunner.TestConsumer tc) throws Throwable {
        for(TestCase testCase : testCases) {
            tc.accept(
                testCase.description(), 
                new TestCaseAdapter(testCase)
            );
        }
    }

    @Override
    public final boolean equals(Object o) {
        if(o instanceof TestsSuite) {
            TestsSuite that = (TestsSuite)o;
            return Objects.equals(testCases, that.testCases);
        } else {
            return false;
        }
    }

    @Override
    public final int hashCode() {
        return Objects.hash(testCases);
    }
}
