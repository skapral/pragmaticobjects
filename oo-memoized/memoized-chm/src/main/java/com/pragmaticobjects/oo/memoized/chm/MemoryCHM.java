/*-
 * ===========================================================================
 * memoized-chm
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
package com.pragmaticobjects.oo.memoized.chm;

import com.pragmaticobjects.oo.memoized.core.MemoizedCallable;
import com.pragmaticobjects.oo.memoized.core.Memory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

/**
 * Memory region, backed by {@link ConcurrentHashMap} instance
 * 
 * @author skapral
 */
public class MemoryCHM implements Memory {
    private static final ConcurrentHashMap<Object, FutureTask> DEFAULT = new ConcurrentHashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(MemoryCHM.class);
    private final ConcurrentHashMap<Object, FutureTask> memoizedObjects;

    /**
     * Ctor.
     * 
     * @param memoizedObjects hash map for memoized calculations
     */
    public MemoryCHM(ConcurrentHashMap<Object, FutureTask> memoizedObjects) {
        this.memoizedObjects = memoizedObjects;
    }

    /**
     * Ctor.
     */
    public MemoryCHM() {
        this(DEFAULT);
    }

    @Override
    public final <T> T memoized(MemoizedCallable<T> callable) {
        try {
            FutureTask t = memoizedObjects.computeIfAbsent(callable, k -> new FutureTask(() -> {
                if(LOG.isTraceEnabled()) {
                    LOG.trace("New calculation: {}", callable.toString());
                }
                return callable.call();
            }));
            t.run();
            return (T) t.get();
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public final <T> Optional<T> dispose(MemoizedCallable<T> callable) {
        return Optional.ofNullable(memoizedObjects.remove(callable))
            .map(futureTask -> {
                try {
                    return futureTask.get();
                } catch(Exception ex) {
                    throw new RuntimeException(ex);
                }
            })
            .map(v -> (T) v);
    }

    @Override
    public final void clean() {
        memoizedObjects.clear();
    }
}
