/*-
 * ===========================================================================
 * project-name
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
package com.pragmaticobjects.oo.meta.base;

import com.pragmaticobjects.oo.meta.base.ifaces.IdFixed;
import com.pragmaticobjects.oo.meta.base.ifaces.MethodWithBody;
import com.pragmaticobjects.oo.meta.base.ifaces.StateFixed;
import com.pragmaticobjects.oo.meta.base.ifaces.TypeFixed;
import com.pragmaticobjects.oo.meta.base.ifaces.TypeNamed;
import com.pragmaticobjects.oo.meta.src.AssertAssumingTemporaryDirectory;
import com.pragmaticobjects.oo.meta.src.AssertSourceFileGenerated;
import com.pragmaticobjects.oo.meta.src.DestToPath;
import com.pragmaticobjects.oo.meta.src.SrcFileJavaPoet;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterizedTypeName;
import io.vavr.collection.List;
import io.vavr.control.Option;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author skapral
 */
public class ClazzTest extends TestsSuite {
    public ClazzTest() {
        super(
            new TestCase(
                "Counter",
                new AssertAssumingTemporaryDirectory(
                    tmp -> new AssertSourceFileGenerated(
                        new SrcFileJavaPoet(
                            "com.test",
                            new Clazz(
                                "Counter",
                                Option.none(),
                                List.empty(),
                                List.of(
                                    new IdFixed("String", "identity")
                                ),
                                List.of(
                                    new StateFixed(
                                        new TypeFixed(
                                            ParameterizedTypeName.get(Map.class, String.class, Integer.class)
                                        ),
                                        "STATE",
                                        CodeBlock.of("new $T<$T, $T>()", HashMap.class, String.class, Integer.class)
                                    )
                                ),
                                List.of(
                                    new MethodWithBody(
                                        new TypeNamed("Integer"),
                                        "next",
                                        List.empty(),
                                        CodeBlock.of(
                                            String.join(
                                                "\r\n",
                                                "return STATE.compute(identity, (key, value) -> {",
                                                "    if($T.isNull(value)) {",
                                                "        return 0;",
                                                "    } else {",
                                                "        return value++;",
                                                "    }",
                                                "});",
                                                ""
                                            ),
                                            Objects.class
                                        )
                                    )
                                )
                            ),
                            new DestToPath(tmp)
                        ),
                        tmp.resolve("com/test/Counter.java"),
                        String.join(
                            "\r\n",
                            "package com.test;",
                            "",
                            "import java.lang.Integer;",
                            "import java.lang.String;",
                            "import java.util.HashMap;",
                            "import java.util.Map;",
                            "import java.util.Objects;",
                            "",
                            "class Counter {",
                            "  private static final Map<String, Integer> STATE = new HashMap<String, Integer>();",
                            "",
                            "  private final String identity;",
                            "",
                            "  public Counter(String identity) {",
                            "    this.identity = identity;",
                            "  }",
                            "",
                            "  public final Integer next() {",
                            "    return STATE.compute(identity, (key, value) -> {",
                            "        if(Objects.isNull(value)) {",
                            "            return 0;",
                            "        } else {",
                            "            return value++;",
                            "        }",
                            "    });",
                            "  }",
                            "}",
                            ""
                        )
                    )
                )
            )
        );
    }
}
