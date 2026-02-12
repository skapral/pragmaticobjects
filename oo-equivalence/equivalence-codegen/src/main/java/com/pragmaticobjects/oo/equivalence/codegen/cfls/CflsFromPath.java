/*-
 * ===========================================================================
 * equivalence-codegen
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
package com.pragmaticobjects.oo.equivalence.codegen.cfls;

import net.bytebuddy.dynamic.ClassFileLocator;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Source for {@link net.bytebuddy.dynamic.ClassFileLocator}, made from specified path.
 * It automatically determines the nature of path provided (a directory or a jar file)
 * and chooses suitable locator for it.
 *
 * @author Kapralov Sergey
 */
public class CflsFromPath extends CflsInferred implements ClassFileLocatorSource {
    private static class Inference implements ClassFileLocatorSource.Inference {
        private final Path path;

        /**
         * Ctor.
         *
         * @param path Path
         */
        public Inference(final Path path) {
            this.path = path;
        }

        @Override
        public final ClassFileLocatorSource classFileLocatorSource() {
            if(!Files.exists(path)) {
                return new CflsExplicit(
                    ClassFileLocator.NoOp.INSTANCE
                );
            }
            if(Files.isDirectory(path)) {
                return new CflsDirectory(path);
            } else {
                return new CflsJar(path);
            }
        }
    }

    /**
     * Ctor.
     *
     * @param path Path
     */
    public CflsFromPath(final Path path) {
        super(
            new Inference(
                path
            )
        );
    }
}
