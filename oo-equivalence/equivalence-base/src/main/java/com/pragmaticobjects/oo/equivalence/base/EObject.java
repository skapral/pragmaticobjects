/*-
 * ===========================================================================
 * equivalence-base
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
package com.pragmaticobjects.oo.equivalence.base;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Base class for all equivalence-compliant objects
 *
 * @author skapral
 */
public abstract class EObject {
    private static final boolean FIXED_STATIC_IDENTITY = System.getProperty("fixedStaticIdentity") != null;

    /**
     * @return Object's attributes
     */
    protected abstract Object[] attributes();

    /**
     * @return Object's hash seed
     */
    protected abstract int hashSeed();
    
    /**
     * @return Object's name
     */
    protected abstract Class<?> baseType();

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof EObject && (this.baseType() == ((EObject) obj).baseType())) {
            final Object[] thisAttrs = attributes();
            final Object[] objAttrs = ((EObject) obj).attributes();
            int length = thisAttrs.length == objAttrs.length ? thisAttrs.length : -1;
            if (length < 0) {
                return false;
            }
            for (int i = 0; i < thisAttrs.length; i++) {
                if (!equal(thisAttrs[i], objAttrs[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public final int hashCode() {
        final Object[] attrs = attributes();
        int result = hash(hashSeed());
        for (Object attr : attrs) {
            result = 31 * result + hash(attr);
        }
        return result;
    }

    @Override
    public final String toString() {
        final Object[] attrs = attributes();
        StringBuilder sb = new StringBuilder();
        sb.append(baseType().getSimpleName());
        sb.append("(");
        sb.append(
            Arrays.stream(attrs)
                .map(EObject::toString)
                .collect(Collectors.joining(", "))
        );
        sb.append(")");
        return sb.toString();
    }

    private final static HashSet<Class<?>> NATURALLY_EQUIVALENT = new HashSet<Class<?>>();

    static {
        NATURALLY_EQUIVALENT.add(String.class);
        NATURALLY_EQUIVALENT.add(Boolean.class);
        NATURALLY_EQUIVALENT.add(Byte.class);
        NATURALLY_EQUIVALENT.add(Short.class);
        NATURALLY_EQUIVALENT.add(Integer.class);
        NATURALLY_EQUIVALENT.add(Long.class);
        NATURALLY_EQUIVALENT.add(Float.class);
        NATURALLY_EQUIVALENT.add(Double.class);
        NATURALLY_EQUIVALENT.add(UUID.class);
        NATURALLY_EQUIVALENT.add(Optional.class);
    }

    ;

    protected static boolean hasIdentity(Object obj) {
        return obj == null || obj instanceof EObject || obj instanceof Enum || NATURALLY_EQUIVALENT.contains(obj.getClass());
    }

    protected static boolean equal(Object object1, Object object2) {
        if (object1 == null) {
            return object2 == null;
        }
        if (object1 == object2) {
            return true;
        }
        if (hasIdentity(object1)) {
            return object1.equals(object2);
        }
        return false;
    }

    protected static int hash(Object element) {
        if (element == null) {
            return 0;
        }
        if (hasIdentity(element)) {
            return element.hashCode();
        }
        return systemIdentity(element);
    }

    protected static int systemIdentity(Object element) {
        if (FIXED_STATIC_IDENTITY) {
            return element.getClass().getName().hashCode();
        } else {
            return System.identityHashCode(element);
        }
    }

    protected static String toString(Object value) {
        if (hasIdentity(value)) {
            return value == null ? "null" : value.toString();
        } else {
            return value.getClass().getName() + "#" + systemIdentity(value);
        }
    }
}
