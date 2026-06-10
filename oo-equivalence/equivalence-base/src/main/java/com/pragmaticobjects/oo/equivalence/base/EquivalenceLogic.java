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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Static utility class that implements {@code equals}, {@code hashCode}, and {@code toString}
 * logic for {@link EObject} instances. All {@link EObject} implementors must delegate
 * those methods to this class.
 *
 * @author Kapralov Sergey
 */
public class EquivalenceLogic {
    private static final boolean FIXED_STATIC_IDENTITY = System.getProperty("fixedStaticIdentity") != null;

    /**
     * Implements equality check for {@link EObject} instances by comparing their base types
     * and attributes pairwise.
     *
     * @param that the object on which {@code equals} was called
     * @param obj  the object to compare with
     * @return {@code true} if both objects have the same base type and all attributes are equal
     */
    public static boolean equals(EObject that, Object obj) {
        if (that == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof EObject && (that.baseType() == ((EObject) obj).baseType())) {
            final Object[] thisAttrs = that.attributes();
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

    /**
     * Computes a hash code for an {@link EObject} using its hash seed and attributes.
     *
     * @param that the object whose hash code is being computed
     * @return the computed hash code
     */
    public static int hashCode(EObject that) {
        final Object[] attrs = that.attributes();
        final int hashSeed = that.hashSeed();
        int result = hash(hashSeed);
        for (Object attr : attrs) {
            result = hashSeed * result + hash(attr);
        }
        return result;
    }

    /**
     * Produces a human-readable string representation of an {@link EObject}, listing its
     * simple class name and all attribute values.
     *
     * @param that the object to stringify
     * @return string representation in the form {@code ClassName(attr1, attr2, ...)}
     */
    public static String toString(EObject that) {
        final Object[] attrs = that.attributes();
        StringBuilder sb = new StringBuilder();
        sb.append(that.baseType().getSimpleName());
        sb.append("(");
        sb.append(
            Arrays.stream(attrs)
                .map(EquivalenceLogic::toString)
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

    private static boolean hasIdentity(Object obj) {
        return obj == null
            || (obj instanceof EquivalenceCompliant && ((EquivalenceCompliant) obj).isEquivalenceCompliant())
            || obj instanceof Enum
            || NATURALLY_EQUIVALENT.contains(obj.getClass());
    }

    private static boolean equal(Object object1, Object object2) {
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

    private static int hash(Object element) {
        if (element == null) {
            return 0;
        }
        if (hasIdentity(element)) {
            return element.hashCode();
        }
        return systemIdentity(element);
    }

    private static int systemIdentity(Object element) {
        if (FIXED_STATIC_IDENTITY) {
            return element.getClass().getName().hashCode();
        } else {
            return System.identityHashCode(element);
        }
    }

    private static String toString(Object value) {
        if (value instanceof HintedAttribute) {
            // HintedAttribute always delegates toString to its stringify strategy,
            // regardless of the equivalenceCompliant flag (which only affects equals/hashCode)
            return value.toString();
        }
        if (hasIdentity(value)) {
            return value == null ? "null" : value.toString();
        } else {
            return value.getClass().getName() + "#" + systemIdentity(value);
        }
    }
}
