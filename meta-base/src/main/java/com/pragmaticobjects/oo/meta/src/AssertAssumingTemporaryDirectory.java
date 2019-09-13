/*-
 * ===========================================================================
 * data-core
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
package com.pragmaticobjects.oo.meta.src;

import com.pragmaticobjects.oo.tests.Assertion;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

/**
 * Assertion, that prepares a temporary directory for the test
 * 
 * @author skapral
 */
public class AssertAssumingTemporaryDirectory implements Assertion {
    private final Function<Path, Assertion> assertionFn;

    /**
     * Ctor.
     * @param assertionFn Assertion constructor
     */
    public AssertAssumingTemporaryDirectory(Function<Path, Assertion> assertionFn) {
        this.assertionFn = assertionFn;
    }
    
    @Override
    public final void check() throws Exception {
        final Path tempPath;
        try {
            tempPath = Files.createTempDirectory("AssertAssumingTemporaryDirectory");
        } catch(IOException ex) {
            throw new RuntimeException("Creating temporary directory was unsuccessfull", ex);
        }
        try {
            assertionFn.apply(tempPath).check();
        } finally {
            try {
                Files.delete(tempPath);
            } catch(Exception ex) {
                //Intentionally supressed
            }
        }
        
    }
}
