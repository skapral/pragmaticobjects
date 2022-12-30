package com.test.assertions;

import org.apache.commons.io.IOUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Set;


@SupportedAnnotationTypes({
    "com.test.assertions.Dummy"
})
public class DummyProcessor extends AbstractProcessor {
    @Override
    public final boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, "Dummy processor");
        for(Element elem : roundEnv.getElementsAnnotatedWith(Dummy.class)) {
            Dummy dummy = elem.getAnnotation(Dummy.class);
            try {
                FileObject javaFileObject = this.processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", dummy.name());
                try(OutputStream stream = javaFileObject.openOutputStream()) {
                    IOUtils.write(dummy.message(), stream, Charset.defaultCharset());
                    stream.flush();
                }
            } catch(Exception ex) {
                new RuntimeException(ex);
            }
        }
        return false;
    }

    @Override
    public final SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
