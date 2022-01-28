/*-
 * ===========================================================================
 * inference-basic
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
package com.pragmaticobjects.oo.inference.basic;

import com.pragmaticobjects.oo.inference.api.GenerateInferred;
import com.pragmaticobjects.oo.inference.codegen.model.InferredClassModel;
import com.pragmaticobjects.oo.inference.hack.Hacks;
import com.pragmaticobjects.oo.meta.freemarker.FreemarkerArtifact;
import com.pragmaticobjects.oo.meta.model.MethodFromExecutableElement;
import com.pragmaticobjects.oo.meta.model.Type;
import com.pragmaticobjects.oo.meta.model.TypeFromDeclaredType;
import com.pragmaticobjects.oo.meta.model.TypeReferential;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author skapral
 */
@SupportedAnnotationTypes({
    "com.pragmaticobjects.oo.inference.api.GenerateInferred"
})
public class InferredImplementationGenerator extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for(PackageElement pe : HashSet.ofAll(annotations)
            .flatMap(anno -> roundEnv.getElementsAnnotatedWith(anno))
            .map(e -> (PackageElement) e)) {
            GenerateInferred anno = pe.getAnnotation(GenerateInferred.class);

            DeclaredType iface = (DeclaredType) Hacks.extractType(anno::value);

            String packageName = pe.getQualifiedName().toString();
            String inferredImplementationName = iface.asElement().getSimpleName().toString() + "Inferred";
            Type generatedType = new TypeReferential(packageName, inferredImplementationName);
            InferredClassModel model = new InferredClassModel(
                generatedType,
                new TypeFromDeclaredType(iface),
                List.of(iface.asElement())
                    .flatMap(i -> i.getEnclosedElements())
                    .filter(i -> i instanceof ExecutableElement)
                    .map(e -> new MethodFromExecutableElement((ExecutableElement) e))
                    .toJavaStream().collect(Collectors.toList())
            );
            FreemarkerArtifact freemarkerArtifact = new FreemarkerArtifact(
                "inferredClass",
                model
            );
            try {
                JavaFileObject newSrc = processingEnv.getFiler().createSourceFile(model.<Type>get("this").getFullName());
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

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
}
