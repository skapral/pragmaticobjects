/*-
 * ===========================================================================
 * inference-basic
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
package com.pragmaticobjects.oo.inference.basic;

import com.pragmaticobjects.oo.meta.anno.procesor.AssertAnnotationProcessorGeneratesFiles;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import io.vavr.collection.List;
import java.nio.file.Paths;

/**
 *
 * @author skapral
 */
public class InferredAliasGeneratorTest extends TestsSuite {
    public InferredAliasGeneratorTest() {
        super(
            new TestCase(
                "Fraction inference alias",
                new AssertAnnotationProcessorGeneratesFiles(
                    new InferredAliasGenerator(),
                    Paths.get("com","test", "FracFromStringInference.java"),
                    String.join("\r\n",
                        "package com.test;",
                        "import com.pragmaticobjects.oo.inference.api.Infers;",
                        "import com.pragmaticobjects.oo.inference.api.Inference;",
                        "import com.test.Fraction;",
                        "import com.test.FracFixed;",
                        "public @Infers(\"FracFromString\") class FracFromStringInference implements Inference<Fraction> {",
                        "    private final String str;",

                        "    public FracFromStringInference(String str) {",
                        "        this.str = str;",
                        "    }",

                        "    @Override",
                        "    public final Fraction inferredInstance() {",
                        "        return new FracFixed(",
                        "            Integer.parseInt(str.split(\"/\")[0]),",
                        "            Integer.parseInt(str.split(\"/\")[1])",
                        "        );",
                        "    }",
                        "}"
                    ),
                    List.of(
                        new AssertAnnotationProcessorGeneratesFiles.File(
                            Paths.get("com", "test", "FracFromString.java"),
                            String.join("\r\n",
                                "package com.test;",
                                "",
                                "import com.pragmaticobjects.oo.inference.api.Inference;",
                                "",
                                "public class FracFromString extends FractionInferred {",
                                "    public FracFromString(String str) {",
                                "        super(",
                                "            new FracFromStringInference(str)",
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
                "Fraction memoized alias",
                new AssertAnnotationProcessorGeneratesFiles(
                    new InferredAliasGenerator(),
                    Paths.get("com","test", "FracFromStringInference.java"),
                    String.join("\r\n",
                        "package com.test;",
                        "import com.pragmaticobjects.oo.inference.api.Infers;",
                        "import com.pragmaticobjects.oo.inference.api.Inference;",
                        "import com.test.Fraction;",
                        "import com.test.FracFixed;",
                        "public @Infers(value = \"FracFromString\", memoized = true) class FracFromStringInference implements Inference<Fraction> {",
                        "    private final String str;",

                        "    public FracFromStringInference(String str) {",
                        "        this.str = str;",
                        "    }",

                        "    @Override",
                        "    public final Fraction inferredInstance() {",
                        "        return new FracFixed(",
                        "            Integer.parseInt(str.split(\"/\")[0]),",
                        "            Integer.parseInt(str.split(\"/\")[1])",
                        "        );",
                        "    }",
                        "}"
                    ),
                    List.of(
                        new AssertAnnotationProcessorGeneratesFiles.File(
                            Paths.get("com", "test", "FracFromString.java"),
                            String.join("\r\n",
                                "package com.test;",
                                "",
                                "import com.pragmaticobjects.oo.inference.api.Inference;",
                                "import com.pragmaticobjects.oo.inference.api.MemoizedInference;",
                                "import com.pragmaticobjects.oo.memoized.core.Memory;",
                                "",
                                "public class FracFromString extends FractionInferred {",
                                "    public FracFromString(String str, Memory memory) {",
                                "        super(",
                                "            new MemoizedInference(",
                                "                new FracFromStringInference(str),",
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
