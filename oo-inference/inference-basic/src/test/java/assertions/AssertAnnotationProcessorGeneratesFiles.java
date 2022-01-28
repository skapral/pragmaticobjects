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
package assertions;

import com.pragmaticobjects.oo.tests.Assertion;
import io.vavr.collection.List;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author skapral
 */
public class AssertAnnotationProcessorGeneratesFiles implements Assertion {
    private static final Logger log = LoggerFactory.getLogger(AssertAnnotationProcessorGeneratesFiles.class);

    public static class SourceFile {
        private final String resourceName;
        private final Path compileDirPath;

        public SourceFile(String resourceName, Path compileDirPath) {
            this.resourceName = resourceName;
            this.compileDirPath = compileDirPath;
        }

        public SourceFile(String resourceName) {
            this(
                resourceName,
                Paths.get(resourceName)
            );
        }

        public final void prepare(Path dir) {
            Path file = dir.resolve(compileDirPath);
            try (InputStream is = Objects.requireNonNull(
                this.getClass().getClassLoader().getResourceAsStream(resourceName),
                "null - " + resourceName
            )) {
                Files.createDirectories(file.getParent());
                try (OutputStream os = Files.newOutputStream(file)) {
                    IOUtils.copy(is, os);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        public final void assertPresence(Path root) {
            Path file = root.resolve(resourceName);
            Assertions.assertThat(file).exists();
            try (InputStream is = Files.newInputStream(file)) {
                try (InputStream isExp = Objects.requireNonNull(
                    this.getClass().getClassLoader().getResourceAsStream(resourceName),
                    "null - " + resourceName
                )) {
                    String contents = IOUtils.toString(is, Charset.defaultCharset());
                    String contentsExpected = IOUtils.toString(isExp, Charset.defaultCharset());
                    Assertions.assertThat(contents).isEqualToNormalizingNewlines(contentsExpected);
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private final AbstractProcessor processor;
    private final List<SourceFile> sources;
    private final List<SourceFile> sourcesAfterProcessing;

    public AssertAnnotationProcessorGeneratesFiles(AbstractProcessor processor, List<SourceFile> sources, List<SourceFile> sourcesAfterProcessing) {
        this.processor = processor;
        this.sources = sources;
        this.sourcesAfterProcessing = sourcesAfterProcessing;
    }

    @Override
    public final void check() throws Exception {
        Path tmpDir = Files.createTempDirectory("AssertAnnotationProcessorGeneratesFiles");
        log.info("Testing directory - " + tmpDir);
        sources.forEach(s -> s.prepare(tmpDir));
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
                fileManager.getJavaFileObjects(
                    Files.find(tmpDir, 100, (p, attrs) -> Files.isRegularFile(p) && p.toString().endsWith(".java"))
                        .map(Path::toFile)
                        .<File>toArray(i -> new File[i])
                )
            );
            task.setProcessors(Arrays.asList(processor));
            if (task.call()) {
                for (SourceFile file : sourcesAfterProcessing) {
                    file.assertPresence(tmpDir);
                }
            } else {
                throw new AssertionError("Compilation failure occurred");
            }
        }
    }
}

