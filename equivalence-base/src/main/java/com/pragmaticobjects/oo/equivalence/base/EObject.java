/*
 * MIT License
 *
 * Copyright (c) 2019 Kapralov Sergey
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.pragmaticobjects.oo.equivalence.base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
     * @return Object's attributes as {@link Map}
     */
    protected Map<String, Object> attributesMap() {
        return new HashMap<>();
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (hasIdentity(obj) && (this.getClass().isAssignableFrom(obj.getClass()) || obj.getClass().isAssignableFrom(this.getClass()))) {
            final Object[] thisAttrs = attributes();
            final Object[] objAttrs = ((EObject) obj).attributes();
            int length = thisAttrs.length == objAttrs.length ? thisAttrs.length : -1;
            if(length < 0) {
                return false;
            }
            for(int i = 0; i < thisAttrs.length; i++) {
                if(!equal(thisAttrs[i], objAttrs[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public final int hashCode() {
        final Object[] attrs = attributes();
        final int length = attrs.length;
        if(length < 1) {
            return 0;
        }
        int result = hash(attrs[0]);
        for(int i = 1; i < length; i++) {
            result = 31 * result + hash(attrs[i]);
        }
        return result;
    }

    @Override
    public final String toString() {
        Map<String, Object> attrs = attributesMap();
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(
            attrs.entrySet()
                .stream()
                .map(es -> toString(es.getKey(), es.getValue()))
                .collect(Collectors.joining(", "))
        );
        sb.append("}");
        return sb.toString();
    }

    private final static HashSet<Class> NATURALLY_EQUIVALENT = new HashSet<Class>() {
        {
            add(String.class);
            add(Boolean.class);
            add(Byte.class);
            add(Short.class);
            add(Integer.class);
            add(Long.class);
            add(Float.class);
            add(Double.class);
            add(UUID.class);
        }
    };

    protected static boolean hasIdentity(Object obj) {
        return obj instanceof EObject || NATURALLY_EQUIVALENT.contains(obj.getClass());
    }
    
    protected static boolean equal(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if (object1 == null) {
            return object2 == null;
        }
        if(hasIdentity(object1)) {
            return object1.equals(object2);
        }
        return false;
    }
    
    protected static int hash(Object element) {
        if(element == null) {
            return 0;
        }
        if(hasIdentity(element)) {
            return element.hashCode();
        }
        return systemIdentity(element);
    }

    protected static int systemIdentity(Object element) {
        if(FIXED_STATIC_IDENTITY) {
            return System.identityHashCode(element);
        } else {
            return element.getClass().getName().hashCode();
        }
    }

    protected static String toString(String name, Object value) {
        if(hasIdentity(value)) {
            return "\"" + name + "\": " + value.toString();
        } else {
            return "\"" + name + "\": \"" + value.getClass().getName() + "#" + systemIdentity(value) + "\"";
        }
    }
}
