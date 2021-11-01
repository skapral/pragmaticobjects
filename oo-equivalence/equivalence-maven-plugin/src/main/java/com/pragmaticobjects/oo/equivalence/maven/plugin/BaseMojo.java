/*-
 * ===========================================================================
 * equivalence-maven-plugin
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
package com.pragmaticobjects.oo.equivalence.maven.plugin;

import com.pragmaticobjects.oo.equivalence.codegen.cn.CnExcludingPackages;
import com.pragmaticobjects.oo.equivalence.codegen.cn.CnFromPath;
import com.pragmaticobjects.oo.equivalence.codegen.cp.ClassPath;
import com.pragmaticobjects.oo.equivalence.codegen.cp.CpFromString;
import com.pragmaticobjects.oo.equivalence.codegen.stage.Stage;
import com.pragmaticobjects.oo.equivalence.codegen.instrumentation.ApplyStages;
import io.vavr.collection.HashSet;
import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.descriptor.PluginDescriptor;

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
    @Parameter
    private String[] excludePackages;

    protected final ClassPath buildClassPath() {
        PluginDescriptor pluginDescriptor = (PluginDescriptor) this.getPluginContext().get("pluginDescriptor");
        String classPath = HashSet.ofAll(project.getArtifacts())
                    .addAll(pluginDescriptor.getArtifacts())
                    .map(Artifact::getFile)
                    .map(File::toString)
                    .collect(Collectors.joining(":"));
        ClassPath cp = new CpFromString(classPath);
        return cp;
    }
    
    protected final void doInstrumentation(Stage stage, ClassPath cp, Path workingDirectory) throws MojoExecutionException, MojoFailureException {
        if(!project.getPackaging().equals("pom") || instrumentPomProjects) {
            new ApplyStages(
                cp,
                workingDirectory,
                new CnExcludingPackages(
                    new CnFromPath(
                        workingDirectory
                    ),
                    Optional.ofNullable(excludePackages).orElse(new String[] {})
                ),
                stage
            ).apply();
        }
    }
}
