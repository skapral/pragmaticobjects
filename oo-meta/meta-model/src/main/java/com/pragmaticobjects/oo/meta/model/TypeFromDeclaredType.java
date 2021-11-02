/*-
 * ===========================================================================
 * meta-model
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2021 Kapralov Sergey
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

import io.vavr.collection.List;

import javax.lang.model.element.PackageElement;
import javax.lang.model.type.DeclaredType;

/**
 *
 * @author skapral
 */
public class TypeFromDeclaredType extends TypeInferred {

    public TypeFromDeclaredType(DeclaredType t) {
        super(
            new Inference<Type>() {
                @Override
                public final Type inferredInstance() {
                    return new TypeReferential(
                        ((PackageElement) t.asElement().getEnclosingElement()).getQualifiedName().toString(),
                        t.asElement().getSimpleName().toString(),
                        new GenericSequence(
                            t.getTypeArguments().stream()
                                .map(GenericFromTypeMirror::new)
                                .collect(List.collector())
                        )
                    );
                }
            }
        );
    }
}
