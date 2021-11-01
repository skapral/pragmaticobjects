# How OO-Equivalence works?

## Before reading this

This page is written with assumption that the reader knows [what the term "object equivalence" means](https://www.pragmaticobjects.com/chapters/009_equivalence_101.html).

## Overview

Main purpose of OO-Equivalence is to make sure that classes, for which the term "equivalence" can be applied, have proper implementations for `equals`,
`hashCode` and `toString` methods.

> Equivalence term is applicable to instances of only those classes, constructors of 
which are logic free and attributes of which are final.

Instrumentation procedure is implemented as a Maven plugin with two goals:

- `instrument` goal instruments main classes at `process-classes` phase
- `instrument-tests` goal instruments test classes at `process-test-classes` phase

For each class, Equivalence Maven plugin executes the sequence of actions 
(see [StandardInstrumentationStage](equivalence-codegen/src/main/java/com/pragmaticobjects/oo/equivalence/codegen/stage/StandardInstrumentationStage.java) class). This sequence includes:

- *Prechecks* --- at this stage, each project class is checked for
compliance to equivalence logic. Generally, non-abstract classes, derived straight from 
`java.util.Object`, non-static attributes of which are final, are considered as
equivalence-compliant.
- *Marking EObjects* --- each equivalence-compliant class is instrumented to implement `EObject` instead of `java.util.Object`.
- *Implementing EObjects* --- implementation of `EObject`'s abstract methods is generated for each class, marked with `EObject` on previous step.
- *Postchecks* --- verification stage, checking sanity for all equivalence-compliant objects within the project. It is mainly checking that the methods of equivalence-compliant classes
are not overridable, static attributes (if any) are final, [aliases]() from equivalence-compliant objects doesn't declare new fields and methods, etc.
On any mismatch found, the build is failed.

# EObject

[EObject](equivalence-base/src/main/java/com/pragmaticobjects/oo/equivalence/base/EObject.java) is a class, that provides generic equivalence logic. It implements and seals `equals`, `hashCode`, `toString` methods.
All equivalence-compliant classes are supposed to extend this class, instead of `java.util.Object`, making methods
implementations final.

Though it is possible and supported by Equivalence Maven Plugin, **it is not recommended to extend `EObject` manually**.
Equivalence Maven plugin detects all equivalence-compliant classes, makes them to extend `EObject`,
and implements all three methods automatically.

Also, **`EObject` class is not recommended to be used in source code directly**, unless you design some tracing or debugging tool which works with Java classes.

All `EObject` inheritors must implement these abstract methods:

- `protected abstract Object[] attributes();` --- a set of object's attributes, that represent the object's identity.
It's return value must stay constant per object instance. Usually, all non-static attributes of a class is supposed to be returned here, *provided that they are all final*.
- `protected abstract int hashSeed();` --- a seed, from which the `hashCode` is calculated.
Should return a constant integer.
- `protected abstract Class<? extends EObject> baseType();` --- a method for 
obtaining the base type of the instance, closest to `EObject` in hierarchy.
Usually, it is sufficient to just return the class, where the method is implemented, and make it final.

Classes, for which equivalence is applicable, should be extended from `EObject`. Direct inheritors from EObject must make the methods implementations final.

Example of `EObject` implementation:

```java
public class Point2D extends EObject {
    private final int x;
    private final int y;

    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    protected final Object[] attributes() {
        return new Object[] {x, y};
    }

    @Override
    protected final int hashSeed() {
        return 42;
    }

    @Override
    protected final Class<? extends EObject> baseType() {
        return Point2D.class;
    }
}
```

## EObject::equals

Returns `true` if the passed object is either the same as `this`, or equivalent to `this`. If passed object is not instance of `EObject`,
the result will be always `false`, as well as if the passed reference is `null`.

The logic of `EObject::equals` takes identity of both objects (`this` and the one provided as `equals` argument) by calling `attributes` method. Then,
for each attribute, it checks whether the pair of objects referenced by it are either the same, or equivalent. Which means:

- if one of the objects referred by an attribute is not instance of `EObject`, the references are compared by `==` operator.
- if both objects are `EObject`, the references are compared by calling `equals` method on them.

### Natural equivalence

In Java, there are certain classes, that don't implement `EObject`, but behave like they are equivalence-compliant.
Examples are boxed primitives and `java.util.UUID`. The special trait about all of them is that they are immutable and reproducible (one can recreate same instances of them in any context).
Later in this document, I will call such classes "natural-equivalence-compliant", and two equivalent instances of these classes --- naturally equivalent.

For natural-equvalence-compliant objects, exclusions from the rules are made: despite the fact that they don't extend `EObject`, they 
are still compared by means of calling `equals` on them, and treated by the oo-equivalence as like they
are equivalence-compliant.

A list of naturally-equivalent classes is currently hardcoded in [EObject](equivalence-base/src/main/java/com/pragmaticobjects/oo/equivalence/base/EObject.java). Hardcode is a temporal measure.

## EObject::hashCode

Returns hash code for `EObject`, which is consistent with equivalence results. If two objects are equivalent, the `hashCode` is guaranteed to be the same.
Implementation of `EObject::hashCode` obtains (by calling `EObject::attributes`), calculates and combines the hash codes for the attributes of the object
(in the same manner like it is proposed by Josh Bloch's Effective Java) but with certain corrections:

- if the attribute is of type `EObject` (or of the naturally-equivalent type), it's hash code is obtained by means of calling `hashCode` on it.
- if the attribute is of type `java.util.Object`, its hash code is obtained by calling `System::identityHashCode` on it.

## EObject::toString

`EObject`'s `toString` method is reserved for tracing and debugging purposes. It prints out the object's internal structure. Placed into exceptions' messages and logs,
`toString` output provides information to developers about the internal structure for the object of interest. For two equivalent objects, output of `toString` is always the same.

**Note** that `EObject::toString`'s output is not intended for parsing in application. Respect the objects, don't break their encapsulation.

