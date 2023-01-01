/*-
 * ===========================================================================
 * inference-basic
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
package com.pragmaticobjects.oo.inference.hack;

import com.pragmaticobjects.oo.inference.api.GenerateInferred;
import com.pragmaticobjects.oo.meta.model.Type;
import com.pragmaticobjects.oo.meta.model.TypeReferential;
import io.vavr.collection.HashMap;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Hacks {
    public static TypeMirror extractType(Supplier<Class<?>> classSupplier) {
        try {
            Class<?> clazz = classSupplier.get();
            throw new RuntimeException("Expected to be failed till that moment - " + clazz.getName());
        } catch (MirroredTypeException mte) {
            TypeMirror classTypeMirror = mte.getTypeMirror();
            return classTypeMirror;
        }
    }


    private static AnnotationMirror getAnnotationMirror(Element element, Class<?> clazz) {
        String clazzName = clazz.getName();
        for(AnnotationMirror m : element.getAnnotationMirrors()) {
            if(m.getAnnotationType().toString().equals(clazzName)) {
                return m;
            }
        }
        throw new NullPointerException();
    }

    private static AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror, String key) {
        for(Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet() ) {
            if(entry.getKey().getSimpleName().toString().equals(key)) {
                return entry.getValue();
            }
        }
        throw new NullPointerException(
            annotationMirror.getElementValues().keySet().stream()
                .map(s -> s.toString())
                .collect(Collectors.joining(","))
        );
    }


    public static TypeMirror extractType2(Element foo, Class<? extends Annotation> anno, String field) {
        AnnotationMirror am = getAnnotationMirror(foo, anno);
        AnnotationValue av = getAnnotationValue(am, field);
        return (TypeMirror) av.getValue();
    }

    public static io.vavr.collection.Map<String, Type> deduceInferenceImplementation(RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(GenerateInferred.class).stream()
            .map(e -> (PackageElement) e)
            .map(pe -> {
                GenerateInferred anno = pe.getAnnotation(GenerateInferred.class);
                DeclaredType iface = (DeclaredType) Hacks.extractType(anno::value);
                String packageName = pe.getQualifiedName().toString();
                String inferredImplementationName = iface.asElement().getSimpleName().toString() + "Inferred";
                Type generatedType = new TypeReferential(packageName, inferredImplementationName);
                return generatedType;
            })
            .collect(HashMap.collector(t -> t.name(), t -> t));
    }
}
