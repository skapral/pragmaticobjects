/*-
 * ===========================================================================
 * equivalence-maven-plugin
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
package com.pragmaticobjects.oo.equivalence.maven.plugin;

import com.pragmaticobjects.oo.equivalence.codegen.cn.CnFromPath;
import com.pragmaticobjects.oo.equivalence.codegen.cp.ClassPath;
import com.pragmaticobjects.oo.equivalence.codegen.cp.CpCombined;
import com.pragmaticobjects.oo.equivalence.codegen.cp.CpExplicit;
import com.pragmaticobjects.oo.equivalence.codegen.cp.CpFromString;
import com.pragmaticobjects.oo.equivalence.codegen.instrumentation.ApplyStages;
import com.pragmaticobjects.oo.equivalence.codegen.stage.Stage;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Base Mojo for different instrumentations
 *
 * @author Kapralov Sergey
 */
public abstract class BaseMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;
    @Parameter(defaultValue = "false", required = true, readonly = true)
    private boolean instrumentPomProjects;

    protected final ClassPath buildClassPath() {
        PluginDescriptor pluginDescriptor = (PluginDescriptor) this.getPluginContext().get("pluginDescriptor");
        String classPath = HashSet.ofAll(project.getArtifacts())
            .addAll(pluginDescriptor.getArtifacts())
            .map(Artifact::getFile)
            .map(File::toString)
            .collect(Collectors.joining(";"));
        ClassPath cp = new CpFromString(classPath);
        return cp;
    }


    protected final List<Path> multiReleaseWorkingSubdirectories(Path workingDirectory) {
        final List<Path> multiReleaseWorkingSubdirectories;
        final Path multiReleaseVersionsPath = workingDirectory.resolve("META-INF/versions");
        if (Files.exists(multiReleaseVersionsPath)) {
            try (Stream<Path> stream = Files.find(
                multiReleaseVersionsPath,
                1,
                (p, bf) -> !multiReleaseVersionsPath.equals(p) && Files.isDirectory(p)
            )) {
                multiReleaseWorkingSubdirectories = stream.collect(List.collector());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            multiReleaseWorkingSubdirectories = List.empty();
        }
        return multiReleaseWorkingSubdirectories;
    }

    protected final void doInstrumentation(
        Stage stage,
        ClassPath cp,
        Path workingDirectory
    ) throws MojoExecutionException, MojoFailureException {
        if (!project.getPackaging().equals("pom") || instrumentPomProjects) {
            new ApplyStages(
                cp,
                workingDirectory,
                new CnFromPath(
                    workingDirectory
                ),
                stage
            ).apply();

            final List<Path> multiReleaseSubdirs = multiReleaseWorkingSubdirectories(workingDirectory);
            for (final Path workingSubdir : multiReleaseSubdirs) {
                new ApplyStages(
                    new CpCombined(
                        new CpExplicit(
                            workingDirectory
                        ),
                        cp
                    ),
                    workingSubdir,
                    new CnFromPath(
                        workingSubdir
                    ),
                    stage
                ).apply();
            }
        }
    }
}
