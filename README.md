# OO-Memoized

[![Build Status (Travis)](https://travis-ci.org/pragmatic-objects/oo-memoized.svg?branch=master)](https://travis-ci.org/pragmatic-objects/oo-memoized)
[![Build status (AppVeyor)](https://ci.appveyor.com/api/projects/status/wvoq20l4c8pn0sr9/branch/master?svg=true)](https://ci.appveyor.com/project/skapral/oo-memoized/branch/master)
[![Codecov](https://codecov.io/gh/pragmatic-objects/oo-memoized/branch/master/graph/badge.svg)](https://codecov.io/gh/pragmatic-objects/oo-memoized)

Objects for caching and memoization.

## How to install

Add maven dependency

```
<dependency>
    <groupId>com.pragmaticobjects.oo.memoized</groupId>
    <artifactId>oo-memoized</artifactId>
    <version>x.y.z</version>
</dependency>
```

## How to use

OO-Memoized provides a contract named `Memory`. Instances of `Memory` 
encapsulate access to shared state, where memoized and cached values are stored.
One important characteristic of `Memory` instances is reproducability - for 
two [equivalent](https://www.pragmaticobjects.com/chapters/004_object_equivalence.html#objects-equivalence) 
instances of `Memory` there is a guarantee that they reproduce
same shared state.

For now, there is a simple implementation of `Memory` named `MemoryCHM`,
which stores memoized values to `ConcurrentHashMap` instance.

## Example

TODO
