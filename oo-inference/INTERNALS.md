# Inferences in detail

## Terminology

- *Inference* is a builder for a certain contract, which implements `Inference` interface:

```java
interface Inference<T> {
    T inferredInstance();
}
```

- *Inferred class* --- a class that delegates all method calls to the object, obtained from encapsulated inference.

```java
class InferredUser implements User {
    private final Inference<User> inference;

    public InferredUser(Inference<User> inference) {
        this.inference = inference;
    }

    public final String name() {
        inference.inferredInstance().name();
    }
}
```

## Why?

Inferred classes can be a basis for object aliases, behavior of which is resolved at runtime.
Provided that we have an inferred user that is resolved from Github API...

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

...we can make inferred alias for it, extending `InferredUser`...

```java
public class GithubUser extends UserInferred {
    public GithubUser(String apiToken, Memory memory) {
        super(
            new GithubUserInference(apiToken),
        );
    }
}
```
...and use it as just a usual user implementation: 

```java
User user = new GithubUser("xxxsomegithubtokenapixxx");
user.name();
```

Also, because inference is an interface, it can be decorated in various ways.

For example, you can wrap
your inference into `MemoizedInference` decorator and reduce round-trips
to heavy logic.

```java
// This object calls GitHub API only once, when it is called first time.
// Inferred user, once obtained from GithubUserInference, is kept in provided 
// Memory instance
public class GithubUser extends UserInferred {
    public GithubUser(String apiToken, Memory memory) {
        super(
            new MemoizedInference(
                new GithubUserInference(apiToken),
                memory
            )
        );
    }
}
```

## @Infers annotation

All inferences, code for which is supposed to be generated, must be annotated with @Infers
annotation. Annotation has two attributes:

- `String value()` --- mandatory, defines the name of generated inferred object.
- `boolean memoized() default false` --- defines whether inferred object will memoize the inference results or not. Currently, memoization
of the interences is done by means of [OO-Memoized](../oo-memoized).

