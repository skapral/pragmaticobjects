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
package com.pragmaticobjects.oo.equivalence.base.tostring;

public class Hex implements ToStringMethod {
    private static final ToStringMethod DEFAULT = new Default();

    private final String prefix;

    public Hex(String prefix) {
        this.prefix = prefix;
    }

    public Hex() {
        this("0x");
    }

    @Override
    public final String stringify(Object obj) {
        final String result;
        if (obj instanceof Integer) {
            result = Integer.toHexString((int) obj);
        } else if (obj instanceof Long) {
            result = Long.toHexString((long) obj);
        } else if (obj instanceof Short) {
            result = Integer.toHexString((short) obj);
        } else if (obj instanceof Byte) {
            result = Integer.toHexString((byte) obj);
        } else if (obj instanceof Float) {
            result = Float.toHexString((float) obj);
        } else if (obj instanceof Double) {
            result = Double.toHexString((double) obj);
        } else {
            return DEFAULT.stringify(obj);
        }
        return prefix + result;
    }
}
