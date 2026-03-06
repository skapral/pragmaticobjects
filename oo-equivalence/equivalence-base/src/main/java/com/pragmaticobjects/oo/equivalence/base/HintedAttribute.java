/*-
 * ===========================================================================
 * equivalence-base
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
package com.pragmaticobjects.oo.equivalence.base;

import com.pragmaticobjects.oo.equivalence.base.tostring.ToStringMethod;

import java.util.Objects;

public class HintedAttribute implements EquivalenceCompliant {
    private final Object obj;
    private final ToStringMethod stringifyMethod;
    private final boolean equivalenceCompliant;

    public HintedAttribute(Object obj, ToStringMethod stringifyMethod, boolean equivalenceCompliant) {
        this.obj = obj;
        this.stringifyMethod = stringifyMethod;
        this.equivalenceCompliant = equivalenceCompliant;
    }

    @Override
    public final boolean isEquivalenceCompliant() {
        return equivalenceCompliant;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HintedAttribute)) return false;
        HintedAttribute that = (HintedAttribute) o;
        if(this.equivalenceCompliant && that.equivalenceCompliant) {
            return Objects.equals(obj, that.obj);
        } else {
            return obj == that.obj;
        }
    }

    @Override
    public final int hashCode() {
        return Objects.hash(obj);
    }

    @Override
    public final String toString() {
        return stringifyMethod.stringify(obj);
    }
}
