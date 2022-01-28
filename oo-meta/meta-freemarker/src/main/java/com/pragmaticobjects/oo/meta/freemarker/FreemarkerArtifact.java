/*-
 * ===========================================================================
 * meta-freemarker
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2022 Kapralov Sergey
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
package com.pragmaticobjects.oo.meta.freemarker;

import com.pragmaticobjects.oo.meta.base.Artifact;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;

/**
 *
 * @author skapral
 */
public class FreemarkerArtifact implements Artifact {
    private static final Configuration CFG = new Configuration(
            Configuration.getVersion()
    );

    static {
        DefaultObjectWrapper ow = new DefaultObjectWrapper(
            Configuration.getVersion()
        ) {
            @Override
            protected void finetuneMethodAppearance(Class clazz, Method m, MethodAppearanceDecision decision) {
                if (m.getDeclaringClass() != Object.class && m.getReturnType() != void.class && m.getParameterTypes().length == 0) {
                    String mName = m.getName();
                    if (!(mName.startsWith("get") && (mName.length() == 3 || Character.isUpperCase(mName.charAt(3))))) {
                        decision.setExposeMethodAs(null);
                        try {
                            decision.setExposeAsProperty(new PropertyDescriptor(mName, clazz, mName, null));
                        } catch (IntrospectionException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        };
        ow.setIterableSupport(true);
        ow.setExposeFields(true);
        CFG.setClassForTemplateLoading(FreemarkerArtifact.class, "/templates");
        CFG.setObjectWrapper(ow);
    }

    private final String templateName;
    private final FreemarkerArtifactModel model;

    public FreemarkerArtifact(String templateName, FreemarkerArtifactModel model) {
        this.templateName = templateName;
        this.model = model;
    }

    @Override
    public final String contents() {
        try {
            Template template = CFG.getTemplate(templateName);
            Writer out = new StringWriter();
            template.process(model, out);
            return out.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
