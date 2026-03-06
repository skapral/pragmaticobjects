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

import com.pragmaticobjects.oo.equivalence.codegen.cp.CpCombined;
import com.pragmaticobjects.oo.equivalence.codegen.cp.CpExplicit;
import com.pragmaticobjects.oo.equivalence.codegen.stage.StandardInstrumentationStage;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;

/**
 * Gradle task that instruments test classes after test compilation.
 * Equivalent to the Maven {@code instrument-tests} mojo.
 *
 * @author Kapralov Sergey
 */
public abstract class InstrumentTestsTask extends BaseInstrumentTask {

    /**
     * Directory containing compiled production classes (added to classpath for test instrumentation).
     *
     * @return production classes directory
     */
    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    public abstract DirectoryProperty getClassesDirectory();

    /**
     * Directory containing compiled test classes to instrument.
     *
     * @return test classes directory
     */
    @InputDirectory
    @OutputDirectory
    public abstract DirectoryProperty getTestClassesDirectory();

    @Override
    public final void instrument() {
        doInstrumentation(
            new StandardInstrumentationStage(),
            new CpCombined(
                buildClassPath(),
                new CpExplicit(getClassesDirectory().get().getAsFile().toPath())
            ),
            getTestClassesDirectory().get().getAsFile().toPath()
        );
    }
}
