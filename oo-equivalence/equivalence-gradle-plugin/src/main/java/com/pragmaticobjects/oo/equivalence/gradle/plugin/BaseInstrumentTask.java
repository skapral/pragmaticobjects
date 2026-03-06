/*-
 * ===========================================================================
 * equivalence-gradle-plugin
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2026 Kapralov Sergey
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
package com.pragmaticobjects.oo.equivalence.gradle.plugin;

import com.pragmaticobjects.oo.equivalence.codegen.cn.CnFromPath;
import com.pragmaticobjects.oo.equivalence.codegen.cp.ClassPath;
import com.pragmaticobjects.oo.equivalence.codegen.cp.CpCombined;
import com.pragmaticobjects.oo.equivalence.codegen.cp.CpExplicit;
import com.pragmaticobjects.oo.equivalence.codegen.cp.CpFromString;
import com.pragmaticobjects.oo.equivalence.codegen.instrumentation.ApplyStages;
import com.pragmaticobjects.oo.equivalence.codegen.stage.Stage;
import io.vavr.collection.List;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Base Gradle task for equivalence instrumentation.
 *
 * @author Kapralov Sergey
 */
public abstract class BaseInstrumentTask extends DefaultTask {

    /**
     * Returns the classpath used for instrumentation (project + plugin runtime).
     *
     * @return instrumentation classpath
     */
    @Classpath
    public abstract ConfigurableFileCollection getInstrumentationClasspath();

    /**
     * Builds a {@link ClassPath} from the task's instrumentation classpath.
     *
     * @return classpath instance
     */
    protected final ClassPath buildClassPath() {
        final String cpString = getInstrumentationClasspath().getFiles().stream()
            .map(File::toString)
            .collect(Collectors.joining(";"));
        return new CpFromString(cpString);
    }

    /**
     * Discovers multi-release JAR version subdirectories under {@code META-INF/versions}.
     *
     * @param workingDirectory root classes directory
     * @return list of multi-release version subdirectories
     */
    protected final List<Path> multiReleaseWorkingSubdirectories(Path workingDirectory) {
        final Path versionsPath = workingDirectory.resolve("META-INF/versions");
        if (!Files.exists(versionsPath)) {
            return List.empty();
        }
        try (Stream<Path> stream = Files.find(
            versionsPath,
            1,
            (p, bf) -> !versionsPath.equals(p) && Files.isDirectory(p)
        )) {
            return stream.collect(List.collector());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Applies instrumentation stage to the given working directory and its multi-release subdirectories.
     */
    protected final void doInstrumentation(Stage stage, ClassPath cp, Path workingDirectory) {
        new ApplyStages(
            cp,
            workingDirectory,
            new CnFromPath(workingDirectory),
            stage
        ).apply();

        for (final Path subdir : multiReleaseWorkingSubdirectories(workingDirectory)) {
            new ApplyStages(
                new CpCombined(new CpExplicit(workingDirectory), cp),
                subdir,
                new CnFromPath(subdir),
                stage
            ).apply();
        }
    }

    /**
     * Entry point for Gradle task execution.
     */
    @TaskAction
    public abstract void instrument();
}
