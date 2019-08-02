/*-
 * ===========================================================================
 * memoized-core
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
package com.pragmaticobjects.oo.memoized.core;

import com.pragmaticobjects.oo.tests.Assertion;
import java.util.function.Function;
import java.util.function.Supplier;
import static org.mockito.Mockito.*;

/**
 * Asserts that calculation under test is executed certain number of times.
 * @author skapral
 */
public class AssertCalculationTriggeredCertainNumberOfTimes implements Assertion {
    private final Function<Supplier, Calculation> calculationFn;
    private final int timesToCall;
    private final int timesCalculated;

    /**
     * Ctor.
     * 
     * @param calculationFn calculation constructor
     * @param timesToCall times to call calculation under test
     * @param timesCalculated expected number of actual calculation execution
     */
    public AssertCalculationTriggeredCertainNumberOfTimes(Function<Supplier, Calculation> calculationFn, int timesToCall, int timesCalculated) {
        this.calculationFn = calculationFn;
        this.timesToCall = timesToCall;
        this.timesCalculated = timesCalculated;
    }

    @Override
    public final void check() throws Exception {
        final Supplier mock = mock(Supplier.class);
        when(mock.get()).thenReturn(new Object());
        final Calculation calculationUnderTest = calculationFn.apply(mock);
        for(int i = 0; i < timesToCall; i++) {
            calculationUnderTest.calculate();
        }
        verify(mock, times(timesCalculated)).get();
    }
}
