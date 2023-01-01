/*-
 * ===========================================================================
 * meta-model
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2023 Kapralov Sergey
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
package com.pragmaticobjects.oo.meta.model;

import com.pragmaticobjects.oo.meta.freemarker.FreemarkerArtifactModel;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Supplier;

/**
 *
 * @author skapral
 */
public class FAMStandard implements FreemarkerArtifactModel {
    private final Type _this;
    private final Map<String, Object> map;

    public FAMStandard(Type _this, Map<String, Object> map) {
        this._this = _this;
        this.map = map;
    }
    
    @Override
    public final <T> T get(String item) {
        if("this".equals(item)) {
            return (T) _this;
        } else if("imports".equals(item)) {
            return (T) getImports();
        } else {
            Object object = map.get(item).get();
            if(object instanceof Supplier) {
                object = ((Supplier) object).get();
            }
            return (T) object;
        }
    }

    private final Collection<Type> getImports() {
        return map.values()
                .flatMap(a -> a instanceof Iterable<?> ? List.ofAll((Iterable<?>) a) : List.of(a))
                .filter(a -> a instanceof ImportsProvider)
                .map(a -> (ImportsProvider) a)
                .flatMap(ip -> ip.getImports())
                .filter(t -> !t.isPrimitive())
                .filter(t -> !t.packageName().startsWith("java.lang"))
                .filter(t -> !t.packageName().equals(_this.packageName()))
                .distinctBy(Comparator.comparing(Type::getFullName))
                .asJava();
    }
}
