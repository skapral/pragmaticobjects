/*-
 * ===========================================================================
 * inference-basic
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2020 Kapralov Sergey
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
package com.pragmaticobjects.oo.inference.basic;

import com.pragmaticobjects.oo.meta.anno.procesor.AssertAnnotationProcessorGeneratesFiles;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import io.vavr.collection.List;

import java.nio.file.Paths;

public class InferredAliasGeneratorGenericsSupportTest extends TestsSuite {
    public InferredAliasGeneratorGenericsSupportTest() {
        super(
            new TestCase(
                "Plain generic",
                new AssertAnnotationProcessorGeneratesFiles(
                    new InferredAliasGenerator(),
                    Paths.get("com","test", "FracFromListInference.java"),
                    String.join("\r\n",
                        "package com.test;",
                        "import com.pragmaticobjects.oo.inference.api.Infers;",
                        "import com.pragmaticobjects.oo.inference.api.Inference;",
                        "import com.test.Fraction;",
                        "import com.test.FracFixed;",
                        "import io.vavr.collection.List;",
                        "",
                        "public @Infers(value = \"FracFromList\", memoized = true) class FracFromListInference implements Inference<Fraction> {",
                        "    private final List<Integer> list;",
                        "",
                        "    public FracFromListInference(List<Integer> list) {",
                        "        this.list = list;",
                        "    }",
                        "",
                        "    @Override",
                        "    public final Fraction inferredInstance() {",
                        "        return new FracFixed(",
                        "            list.get(0),",
                        "            list.get(1)",
                        "        );",
                        "    }",
                        "}"
                    ),
                    List.of(
                        new AssertAnnotationProcessorGeneratesFiles.File(
                            Paths.get("com", "test", "FracFromList.java"),
                            String.join("\r\n",
                                "package com.test;",
                                "",
                                "import com.pragmaticobjects.oo.inference.api.Inference;",
                                "import com.pragmaticobjects.oo.inference.api.MemoizedInference;",
                                "import com.pragmaticobjects.oo.memoized.core.Memory;",
                                "import io.vavr.collection.List;",
                                "",
                                "public class FracFromList extends FractionInferred {",
                                "    public FracFromList(List<Integer> list, Memory memory) {",
                                "        super(",
                                "            new MemoizedInference(",
                                "                new FracFromListInference(list),",
                                "                memory",
                                "            )",
                                "        );",
                                "    }",
                                "}",
                                ""
                            )
                        )
                    )
                )
            ),
            new TestCase(
                "Wildcard generic",
                new AssertAnnotationProcessorGeneratesFiles(
                    new InferredAliasGenerator(),
                    Paths.get("com","test", "FracFromListInference.java"),
                    String.join("\r\n",
                            "package com.test;",
                            "import com.pragmaticobjects.oo.inference.api.Infers;",
                            "import com.pragmaticobjects.oo.inference.api.Inference;",
                            "import com.test.Fraction;",
                            "import com.test.FracFixed;",
                            "import io.vavr.collection.List;",
                            "",
                            "public @Infers(value = \"FracFromList\", memoized = true) class FracFromListInference implements Inference<Fraction> {",
                            "    private final List<? extends Integer> list;",
                            "",
                            "    public FracFromListInference(List<? extends Integer> list) {",
                            "        this.list = list;",
                            "    }",
                            "",
                            "    @Override",
                            "    public final Fraction inferredInstance() {",
                            "        return new FracFixed(",
                            "            list.get(0),",
                            "            list.get(1)",
                            "        );",
                            "    }",
                            "}"
                    ),
                    List.of(
                        new AssertAnnotationProcessorGeneratesFiles.File(
                            Paths.get("com", "test", "FracFromList.java"),
                            String.join("\r\n",
                                    "package com.test;",
                                    "",
                                    "import com.pragmaticobjects.oo.inference.api.Inference;",
                                    "import com.pragmaticobjects.oo.inference.api.MemoizedInference;",
                                    "import com.pragmaticobjects.oo.memoized.core.Memory;",
                                    "import io.vavr.collection.List;",
                                    "",
                                    "public class FracFromList extends FractionInferred {",
                                    "    public FracFromList(List<? extends Integer> list, Memory memory) {",
                                    "        super(",
                                    "            new MemoizedInference(",
                                    "                new FracFromListInference(list),",
                                    "                memory",
                                    "            )",
                                    "        );",
                                    "    }",
                                    "}",
                                    ""
                            )
                        )
                    )
                )
            )
        );
    }
}
