# OO-Memoized

[![Build Status (Travis)](https://travis-ci.org/pragmatic-objects/oo-memoized.svg?branch=master)](https://travis-ci.org/pragmatic-objects/oo-memoized)
[![Build status (AppVeyor)](https://ci.appveyor.com/api/projects/status/wvoq20l4c8pn0sr9/branch/master?svg=true)](https://ci.appveyor.com/project/skapral/oo-memoized/branch/master)
[![Codecov](https://codecov.io/gh/pragmatic-objects/oo-memoized/branch/master/graph/badge.svg)](https://codecov.io/gh/pragmatic-objects/oo-memoized)

Objects for caching and memoization.

## How to install

1. Add maven dependency

```
<dependency>
    <groupId>com.pragmaticobjects.oo.memoized</groupId>
    <artifactId>oo-memoized</artifactId>
    <version>x.y.z</version>
</dependency>
```

2. Each object, that is supposed to use caching capabilities, must encapsulate and utilize a reference to memory:

```java
class GithubUser {
    private final String apiToken;
    private final Memory memory;

    public GithubUser(String apiToken, Memory memory) {
        this.apiToken = apiToken;
        this.memory = memory;
    }

    @Override
    public String name() {
        // Here, we use GitHub API to obtain the user's name by api token.
        // Since Github API is rather heavy API, we would desire to avoid calling it on each name() call.
        // That's why we are wrapping this logic inside memoized calculation
        return memory.memoizedCalculation(this, "name", () -> {
            try {
                Github github = new RtGithub(apiToken);
                com.jcabi.github.User self = github.users().self();
                return self.login();
            } catch(IOException ex) {
                throw new RuntimeException(ex);
            }
        }).calculate();
    }
}
```


## How to use

OO-Memoized provides a contract named `Memory`. Instances of `Memory` 
encapsulate access to shared state, where memoized and cached values are stored.
One important characteristic of `Memory` instances is reproducability - for 
two [equivalent](https://www.pragmaticobjects.com/chapters/009_equivalence_101.html) 
instances of `Memory` there is a guarantee that they reproduce
same shared state.

For now, there is a simple implementation of `Memory` named `MemoryCHM`,
which stores memoized values to `ConcurrentHashMap` instance.

