# OO-Equivalence

Equivalence logic generator for Java objects. For information about what equivalence is, and how it is generated, read this [guide](INTERNALS.md).

## Installation

1. Add dependency:

```
<dependency>
  <groupId>com.pragmaticobjects.oo.equivalence</groupId>
  <artifactId>equivalence-base</artifactId>
  <version>x.y.z</version>
</dependency>
```

2. Add Maven plugin:

```
<build>
    <plugins>
        <plugin>
            <groupId>com.pragmaticobjects.oo.equivalence</groupId>
            <artifactId>equivalence-maven-plugin</artifactId>
            <version>x.y.z</version>
            <executions>
                <execution>
                    <goals>
                        <goal>instrument</goal>
                        <goal>instrument-tests</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
