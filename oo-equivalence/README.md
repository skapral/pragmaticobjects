# OO-Equivalence

Equivalence logic generator for Java objects. For information about what equivalence is, and how it is generated, read this [guide](INTERNALS.md).

## Installation

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>com.pragmaticobjects.oo.equivalence</groupId>
        <artifactId>equivalence-base</artifactId>
        <version>x.y.z</version>
    </dependency>
</dependencies>

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

### Gradle

```groovy
plugins {
    id 'java'
    id 'com.pragmaticobjects.oo.equivalence' version 'x.y.z'
}

dependencies {
    implementation 'com.pragmaticobjects.oo.equivalence:equivalence-base:x.y.z'
}
```

Or with Kotlin DSL (`build.gradle.kts`):

```kotlin
plugins {
    java
    id("com.pragmaticobjects.oo.equivalence") version "x.y.z"
}

dependencies {
    implementation("com.pragmaticobjects.oo.equivalence:equivalence-base:x.y.z")
}
```
