/*-
 * ===========================================================================
 * equivalence-codegen
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2024 Kapralov Sergey
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
package com.pragmaticobjects.oo.equivalence.codegen.cn;

import com.pragmaticobjects.oo.equivalence.assertions.Assertion;
import io.vavr.collection.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

/**
 *
 * @author skapral
 */
public class AssertSimulatingClasspath implements Assertion {
    private final Function<Path, Assertion> assertionFn;
    private final List<Path> classes;

    public AssertSimulatingClasspath(Function<Path, Assertion> assertionFn, List<Path> classes) {
        this.assertionFn = assertionFn;
        this.classes = classes;
    }

    public AssertSimulatingClasspath(Function<Path, Assertion> assertionFn, Path... classes) {
        this(assertionFn, List.of(classes));
    }
    
    @Override
    public final void check() throws Exception {
        Path tmpDir = Files.createTempDirectory("SimulatingClassPath");
        for(Path clazz : classes) {
            if(clazz.isAbsolute()) {
                throw new RuntimeException(
                    String.format("Expected non-absolute paths, found %s", clazz)
                );
            }
            try {
                Files.createDirectories(tmpDir.resolve(clazz).getParent());
                Files.createFile(tmpDir.resolve(clazz));
            } catch(Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        assertionFn.apply(tmpDir).check();
    }
}
