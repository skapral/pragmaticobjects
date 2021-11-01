# OO-Inference

[![Travis](https://travis-ci.org/pragmatic-objects/oo-inference.svg?branch=master)](https://travis-ci.org/pragmatic-objects/oo-inference)
[![AppVeyor](https://ci.appveyor.com/api/projects/status/0qvup376hvsg571m?svg=true)](https://ci.appveyor.com/project/skapral/oo-inference)
[![Codecov](https://codecov.io/gh/pragmatic-objects/oo-inference/branch/master/graph/badge.svg)](https://codecov.io/gh/pragmatic-objects/oo-inference)

Annotation processor for generating inferred objects. For information about what are inferences and inferred objects and why do they needed, check [this page](INTERNALS.md).

## How to use it

1. Add a maven dependency:

```
<dependencies>
    <dependency>
        <groupId>com.pragmaticobjects.oo.inference</groupId>
        <artifactId>inference-basic</artifactId>
        <version>x.y.z</version>
    </dependency>
</dependencies>
```

2. Define and annotate an inference:

```java
public @Infers(value = "GithubUser", memoized = true) class GithubUserInference implements Inference<User> {
    private final String apiToken;

    public GithubUserInference(String apiToken) {
        this.apiToken = apiToken;
    }

    @Override
    public final User inferredInstance() {
        try {
            Github github = new RtGithub(apiToken);
            com.jcabi.github.User self = github.users().self();
            return new StaticUser(self.login(), self.emails().iterate());    
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
```

3. Build the project

During the project build, sources for inferred class (`UserInferred`) and inferred alias (`GithubUser`) for the
defined inference will automatically be generated.

4. Use generated sources

```java
public class App {
    public static void main( String[] args ) throws Exception {
        Memory mem = new MemoryCHM();
        User user = new GithubUser("xxxsomegithubtokenxxx", mem);
        System.out.println(user.name());
    }
}
```

