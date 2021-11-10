# OO-Inference

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

2. Assuming you have certain object's contract

```java
interface User {
    String name();
    List<String> emails();
} 
```

3. Generate Inferred implementaiton for it by annotating a package, where you want it to be placed:

```java
// inside package-json.java
@GenerateInferred(User.class)
package com.somepackage;

import com.pragmaticobjects.oo.inference.api.GenerateInferred;
```


4. Define and annotate an inference:

```java
public @Infers(value = "GithubUser", memoized = true, using = com.somepackage.UserInferred.class) class GithubUserInference implements Inference<User> {
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

5. Build the project

During the project build, sources for inferred class (`UserInferred`) and inferred alias (`GithubUser`) for the
defined inference will automatically be generated.

6. Use generated sources. The behavior of `GithubUser` will be inferred from the original `GithubUserInference`, but for outer code
it looks and feels like an ordinary `User`'s implementation.

```java
public class App {
    public static void main( String[] args ) throws Exception {
        Memory mem = new MemoryCHM();
        User user = new GithubUser("xxxsomegithubtokenxxx", mem);
        System.out.println(user.name());
    }
}
```

