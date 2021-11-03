# OO-Tests

## Introduction:

Good tests must have only one statement. In theory, it's hard to disagree with that. 
But in practice sometimes it's hard to achieve. Some modules under test may have 
complex preconditions and assumptions, and their simulation may not fit into one statement.

For example, consider a class, which makes a call to some HTTP API: 
- In order to make a good isolated unit test for such class, one will need to bootstrap and configure mock HTTP server. It
is not usually a single-statement operation.
- If the code for bootstrapping mock HTTP server is located in test code or in BeforeTest/AfterTest blocks, it means that 
it's coverage is not tracked. It is not tested, yet may contain mistakes.
- There may be another classes which call the same HTTP API. But the mock server initialization in Before/AfterTest
block is not reusable outside the test module.

OO-tests project provides new way of testing object-oriented code. Reusable assertion is a Java class which implements 
`com.pragmaticobjects.oo.tests.Assertion` interface. It's aim is to validate some certain invariant on the object under 
test. As an ordinary classes, assertions may be covered with tests and distributed together with classes, so that developers
which extend your code may use them for their own tests.

For the detailed idea description, refer to [this](https://pragmaticobjects.com/chapters/003_reusable_assertions.html) blogpost.

OO-tests currently supports JUnit4 and JUnit5 testing frameworks. It is highly recommented (but not mandatory) to use `OO-tests` on 
projects, instrumented by `maven-equivalence-plugin`. 

## JUnit 4

In order to start using OO-Tests with JUnit 4, add the maven dependency:

```
    <dependency>
        <groupId>com.pragmaticobjects.oo.tests</groupId>
        <artifactId>tests-junit4</artifactId>
        <version>x.y.z</version>
    </dependency>
```

## JUnit 5

In order to start using OO-Tests with JUnit 5, add the maven dependency:

```
    <dependency>
        <groupId>com.pragmaticobjects.oo.tests</groupId>
        <artifactId>tests-junit5</artifactId>
        <version>x.y.z</version>
    </dependency>
```

If you use maven surefire plugin below 2.22 version, you must also add dependency on `junit-platform-surefire-provider`:

```
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>2.21.0</version>
        <configuration>
            <trimStackTrace>false</trimStackTrace>
        </configuration>
        <dependencies>
            <dependency>
                <groupId>org.junit.platform</groupId>
                <artifactId>junit-platform-surefire-provider</artifactId>
                <version>1.2.0</version>
            </dependency>
         </dependencies>
    </plugin>
```
