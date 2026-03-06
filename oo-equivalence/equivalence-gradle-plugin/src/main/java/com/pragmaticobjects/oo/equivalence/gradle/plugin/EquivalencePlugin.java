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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;

/**
 * Gradle plugin that wires equivalence instrumentation into the Java build lifecycle.
 * <p>
 * Registers two tasks:
 * <ul>
 *   <li>{@code instrumentClasses} — instruments production classes, runs after {@code compileJava}</li>
 *   <li>{@code instrumentTestClasses} — instruments test classes, runs after {@code compileTestJava}</li>
 * </ul>
 * Both tasks are automatically inserted into the build graph so that subsequent tasks
 * (e.g. {@code test}, {@code jar}) consume the instrumented bytecode.
 *
 * @author Kapralov Sergey
 */
public class EquivalencePlugin implements Plugin<Project> {

    private static final String TASK_INSTRUMENT = "instrumentClasses";
    private static final String TASK_INSTRUMENT_TESTS = "instrumentTestClasses";

    @Override
    public void apply(Project project) {
        // Require the Java plugin to be applied first
        project.getPlugins().apply(JavaPlugin.class);

        final SourceSetContainer sourceSets = project.getExtensions()
            .getByType(SourceSetContainer.class);
        final SourceSet main = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME);
        final SourceSet test = sourceSets.getByName(SourceSet.TEST_SOURCE_SET_NAME);

        // --- instrumentClasses task ---
        final TaskProvider<InstrumentTask> instrumentTask = project.getTasks()
            .register(TASK_INSTRUMENT, InstrumentTask.class, task -> {
                task.setDescription("Instruments production classes with equivalence bytecode.");
                task.setGroup("build");
                task.getClassesDirectory().set(
                    main.getOutput().getClassesDirs().getSingleFile()
                );
                task.getInstrumentationClasspath().setFrom(
                    project.getConfigurations().getByName(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME)
                );
                task.dependsOn(JavaPlugin.COMPILE_JAVA_TASK_NAME);
            });

        // Insert instrumentClasses between compileJava and classes
        project.getTasks().named(JavaPlugin.CLASSES_TASK_NAME)
            .configure(t -> t.dependsOn(instrumentTask));

        // --- instrumentTestClasses task ---
        final TaskProvider<InstrumentTestsTask> instrumentTestsTask = project.getTasks()
            .register(TASK_INSTRUMENT_TESTS, InstrumentTestsTask.class, task -> {
                task.setDescription("Instruments test classes with equivalence bytecode.");
                task.setGroup("build");
                task.getClassesDirectory().set(
                    main.getOutput().getClassesDirs().getSingleFile()
                );
                task.getTestClassesDirectory().set(
                    test.getOutput().getClassesDirs().getSingleFile()
                );
                task.getInstrumentationClasspath().setFrom(
                    project.getConfigurations().getByName(JavaPlugin.TEST_RUNTIME_CLASSPATH_CONFIGURATION_NAME)
                );
                task.dependsOn(JavaPlugin.COMPILE_TEST_JAVA_TASK_NAME, instrumentTask);
            });

        // Insert instrumentTestClasses between compileTestJava and testClasses
        project.getTasks().named(JavaPlugin.TEST_CLASSES_TASK_NAME)
            .configure(t -> t.dependsOn(instrumentTestsTask));
    }
}
