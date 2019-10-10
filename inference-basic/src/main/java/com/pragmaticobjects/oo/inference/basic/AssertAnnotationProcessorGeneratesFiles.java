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
package com.pragmaticobjects.oo.inference.basic;

import com.pragmaticobjects.oo.tests.Assertion;
import io.vavr.collection.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import javax.annotation.processing.AbstractProcessor;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;

/**
 *
 * @author skapral
 */
public class AssertAnnotationProcessorGeneratesFiles implements Assertion {

    public static class File {
        private final Path path;
        private final String contents;

        public File(Path path, String contents) {
            this.path = path;
            this.contents = contents;
        }

        public final void assertPresence(Path root) {
            Assertions.assertThat(root.resolve(path)).exists();
            try {
                String contents = IOUtils.toString(new FileInputStream(root.resolve(path).toFile()),Charset.defaultCharset());
                Assertions.assertThat(contents).isEqualToNormalizingNewlines(this.contents);
            } catch(Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private final AbstractProcessor processor;
    private final Path sourceFileLocations;
    private final String sourceFileContents;
    private final List<File> files;

    public AssertAnnotationProcessorGeneratesFiles(AbstractProcessor processor, Path sourceFileLocations, String sourceFileContents, List<File> files) {
        this.processor = processor;
        this.sourceFileLocations = sourceFileLocations;
        this.sourceFileContents = sourceFileContents;
        this.files = files;
    }
    
    @Override
    public final void check() throws Exception {
        Path tmpDir = Files.createTempDirectory("AssertAnnotationProcessorGeneratesFiles");
        Files.createDirectories(tmpDir.resolve(sourceFileLocations).getParent());
        try(OutputStream os = new FileOutputStream(tmpDir.resolve(sourceFileLocations).toFile())) {
            IOUtils.write(sourceFileContents, os, Charset.defaultCharset());
        }
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            ClassLoader cl = lookup.lookupClass().getClassLoader();
            StringBuilder classpath = new StringBuilder();
            String separator = System.getProperty("path.separator");
            String prop = System.getProperty("java.class.path");

            if (prop != null && !"".equals(prop)) {
                classpath.append(prop);
            }
            if (cl instanceof URLClassLoader) {
                for (URL url : ((URLClassLoader) cl).getURLs()) {
                    if (classpath.length() > 0) {
                        classpath.append(separator);
                    }
                    if ("file".equals(url.getProtocol())) {
                        classpath.append(new java.io.File(url.toURI()));
                    }
                }
            }
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(tmpDir.toFile()));
            
            JavaCompiler.CompilationTask task = compiler.getTask(
                null,
                fileManager,
                null,
                Arrays.asList(
                    "-classpath", classpath.toString()
                ),
                null,
                fileManager.getJavaFileObjects(tmpDir.resolve(sourceFileLocations).toFile())
            );
            task.setProcessors(Arrays.asList(processor));
            if (task.call()) {
                for(File file : files) {
                    file.assertPresence(tmpDir);
                }
            } else {
                Assertions.fail("Compilation failure occurred");
            }
        }
    }

    private static String classPathFor(Class<?> clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
    }
}
