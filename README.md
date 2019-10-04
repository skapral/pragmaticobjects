# OO-Equivalence

[![Travis](https://travis-ci.org/pragmatic-objects/oo-equivalence.svg?branch=master)](https://travis-ci.org/pragmatic-objects/oo-equivalence)
[![AppVeyor](https://ci.appveyor.com/api/projects/status/op5b7tpb6phxh862?svg=true)](https://ci.appveyor.com/project/skapral/oo-equivalence)
[![Codecov](https://codecov.io/gh/pragmatic-objects/oo-equivalence/branch/master/graph/badge.svg)](https://codecov.io/gh/pragmatic-objects/oo-equivalence)

Equivalence logic generator for Java objects. For information about what equivalence is, read this [post](https://www.pragmaticobjects.com/chapters/009_equivalence_101.html). For information on how it is calculated, read this [guide](INTERNALS.md).

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



