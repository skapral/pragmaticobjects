/*-
 * ===========================================================================
 * equivalence-base
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2023 Kapralov Sergey
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
package com.pragmaticobjects.oo.equivalence.base;

/**
 * Helper interface for all equivalence-compliant candidates for instrumentation.
 *
 * It is supposed that all implementors of this interface must also implement equals/hashCode/toString
 * methods, delegating their calls to {@code com.pragmaticobjects.oo.equivalence.base.EquivalenceLogic}, as follows:
 *
 * <pre><code>
 *     public final boolean equals(Object obj) {
 *         return EquivalenceLogic.equals(this, obj);
 *     }
 *
 *     public final int hashCode() {
 *         return EquivalenceLogic.hashCode(this);
 *     }
 *
 *     public final String toString() {
 *         return EquivalenceLogic.toString(this);
 *     }
 * </code></pre>
 *
 * Instrumentor will generate implementations implicitly for each implementor it will meet.
 */
public interface EObject extends EquivalenceCompliant {
    /**
     * @return Object's attributes
     */
    Object[] attributes();

    /**
     * @return Object's hash seed
     */
    int hashSeed();

    /**
     * @return Object's name
     */
    Class<?> baseType();
}
