/*-
 * ===========================================================================
 * inference-codegen
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
package com.pragmaticobjects.oo.inference.codegen;

import com.pragmaticobjects.oo.inference.codegen.model.InferredClassModel;
import com.pragmaticobjects.oo.inference.codegen.model.Method;
import com.pragmaticobjects.oo.inference.codegen.model.Type;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.JavaFileObject;

/**
 *
 * @author skapral
 */
@SupportedAnnotationTypes({
    "com.pragmaticobjects.oo.inference.api.Infers"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class InferredImplementationGenerator extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        HashSet<DeclaredType> ifaces = HashSet.ofAll(annotations)
                .flatMap(a -> roundEnv.getElementsAnnotatedWith(a))
                .map(a -> (TypeElement) a)
                .map(a -> a.getInterfaces())
                .filter(i -> i.size() == 1)
                .map(i -> i.get(0))
                .map(i -> (DeclaredType) i)
                .map(i -> i.getTypeArguments().get(0))
                .map(i -> (DeclaredType) i);
        for(DeclaredType iface : ifaces) {
            PackageElement pe = (PackageElement) processingEnv.getTypeUtils().asElement(iface).getEnclosingElement();
            String packageName = pe.getQualifiedName().toString();
            String inferredImplementationName = iface.asElement().getSimpleName().toString() + "Inferred";
            InferredClassModel model = new InferredClassModel(
                new Type(packageName, inferredImplementationName),
                Type.from(iface),
                List.of(iface.asElement())
                    .flatMap(i -> i.getEnclosedElements())
                    .map(e -> Method.from((ExecutableElement) e))
                    .toJavaStream().collect(Collectors.toList())
            );
            FreemarkerArtifact freemarkerArtifact = new FreemarkerArtifact(
                "inferredClass",
                model
            );
            try {
                JavaFileObject newSrc = processingEnv.getFiler().createSourceFile(model.getThis().getFullName());
                try(OutputStream os = newSrc.openOutputStream()) {
                    os.write(freemarkerArtifact.contents().getBytes());
                    os.flush();
                }
            } catch (IOException ex) {
                Logger.getLogger(InferredImplementationGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
}
