/*-
 * ===========================================================================
 * inference-basic
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
package com.pragmaticobjects.oo.inference.codegen.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;

public class Type implements ImportsProvider {
    public final String packageName;
    public final String name;

    public static Type from(DeclaredType t) {
        return new Type(
            ((PackageElement)t.asElement().getEnclosingElement()).getQualifiedName().toString(),
            t.asElement().getSimpleName().toString()
        );
    }
    
    public static Type from(PrimitiveType t) {
        return new Type(
            null,
            t.toString()
        );
    }
    
    public static Type from(TypeMirror t) {
        if(t instanceof DeclaredType) {
            return from((DeclaredType) t);
        }
        if(t instanceof PrimitiveType) {
            return from((PrimitiveType) t);
        }
        throw new RuntimeException();
    }
    
    public static Type from(TypeElement t) {
        return from(t.asType());
    }
    
    public Type(String packageName, String name) {
        this.packageName = packageName;
        this.name = name;
    }
    
    public final boolean isPrimitive() {
        return packageName == null;
    }
    
    public final String getFullName() {
        if(packageName == null) {
            return name;
        } else {
            return packageName + "." + name;
        }
    }

    @Override
    public final Collection<Type> getImports() {
        return Collections.singleton(this);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.packageName);
        hash = 23 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Type other = (Type) obj;
        if (!Objects.equals(this.packageName, other.packageName)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
}

