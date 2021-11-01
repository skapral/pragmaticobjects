/*-
 * ===========================================================================
 * inference-basic
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2020 Kapralov Sergey
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

import com.pragmaticobjects.oo.inference.api.Infers;
import com.pragmaticobjects.oo.inference.codegen.model.InferredAliasModel;
import com.pragmaticobjects.oo.meta.freemarker.FreemarkerArtifact;
import com.pragmaticobjects.oo.meta.model.Argument;
import com.pragmaticobjects.oo.meta.model.ArgumentFromVariableElement;
import com.pragmaticobjects.oo.meta.model.Type;
import com.pragmaticobjects.oo.meta.model.TypeFromDeclaredType;
import com.pragmaticobjects.oo.meta.model.TypeFromTypeMirror;
import com.pragmaticobjects.oo.meta.model.TypeReferential;
import io.vavr.collection.List;
import io.vavr.control.Option;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
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
public class InferredAliasGenerator extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for(Element elem : roundEnv.getElementsAnnotatedWith(Infers.class)) {
            Infers anno = elem.getAnnotation(Infers.class);
            DeclaredType iface = Option.of(elem)
                    .map(a -> (TypeElement) a)
                    .map(a -> a.getInterfaces())
                    .filter(i -> i.size() == 1)
                    .map(i -> i.get(0))
                    .map(i -> (DeclaredType) i)
                    .map(i -> i.getTypeArguments().get(0))
                    .map(i -> (DeclaredType) i)
                    .get();
            List<Argument> args = List.ofAll(elem.getEnclosedElements())
                    .filter(e -> e.getKind() == ElementKind.FIELD)
                    .filter(e -> !e.getModifiers().contains(Modifier.STATIC))
                    .map(e -> (VariableElement) e)
                    .map(e -> new ArgumentFromVariableElement(e));
            String packageName = ((PackageElement)elem.getEnclosingElement()).getQualifiedName().toString();
            String inferredImplementationName = iface.asElement().getSimpleName().toString() + "Inferred";
            String aliasName = anno.value();
            InferredAliasModel model = new InferredAliasModel(
                new TypeReferential(packageName, aliasName),
                new TypeReferential(packageName, inferredImplementationName),
                new TypeFromDeclaredType(iface),
                new TypeFromTypeMirror(((TypeElement) elem).asType()),
                args
            );
            FreemarkerArtifact freemarkerArtifact = new FreemarkerArtifact(
                anno.memoized() ? "memoizedAlias" : "inferredAlias",
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
}
