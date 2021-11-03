/*-
 * ===========================================================================
 * equivalence-codegen
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2021 Kapralov Sergey
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
package com.pragmaticobjects.oo.equivalence.codegen.ii;

import com.pragmaticobjects.oo.equivalence.assertions.Assertion;
import java.util.concurrent.atomic.AtomicReference;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.matcher.ElementMatcher;

import static org.assertj.core.api.Assertions.*;

/**
 * Assertion that passes if {@link InstrumentationIteration} under the test produces a valid class.
 * The class is implied as valid if it is successfully loaded by JVM and passes Bytecode
 * verification
 * 
 * @author Kapralov Sergey
 */
public class AssertClassAfterInstrumentation implements Assertion {
    private final InstrumentationIteration ii;
    private final Class<?> type;
    private final ElementMatcher<TypeDescription> matcher;

    /**
     * Ctor.
     *
     * @param ii {@link InstrumentationIteration} under test.
     * @param type A type to instrument.
     * @param matcher A matcher
     */
    public AssertClassAfterInstrumentation(InstrumentationIteration ii, Class<?> type, ElementMatcher<TypeDescription> matcher) {
        this.ii = ii;
        this.type = type;
        this.matcher = matcher;
    }

    @Override
    public final void check() throws Exception {
        final DynamicType.Builder<?> builder = new ByteBuddy().redefine(type);
        final TypeDescription typeDescription = new TypeDescription.ForLoadedType(type);
        final AtomicReference<Class<?>> typeRef = new AtomicReference<>();
        assertThatCode(() -> {
            final DynamicType.Unloaded<?> make = ii
                    .apply(builder, typeDescription)
                    .make();
            final Class<?> clazz = make.load(new AnonymousClassLoader(), ClassLoadingStrategy.Default.CHILD_FIRST).getLoaded();
            clazz.getMethods(); // Initiate validation.
            typeRef.set(clazz);
        }).doesNotThrowAnyException();
        assertThat(
            matcher.matches(
                new TypeDescription.ForLoadedType(
                    typeRef.get()
                )
            )
        ).withFailMessage("Expected instrumented class to match").isTrue();
    }

    /**
     * Anonymous class loader for isolating calls on {@link AssertClassAfterInstrumentation}
     *
     * @author Kapralov Sergey
     */
    private static final class AnonymousClassLoader extends ClassLoader {}
}
