# How OO-Equivalence works?

## Overview

A pair of class instances `A` and `B` is called *equivalent*, if it is guaranteed that for each code call using object `A`, substituting `B` in place
of `A` changes nothing in the logic behaviour and side effects.

Main purpose of OO-Equivalence is to make sure that classes, for which the term "equivalence" can be applied, have 
proper implementations for `equals`, `hashCode` and `toString` methods. Criteria, by which classes are decided to be
equivalence-logic-compliant, are:

- All non-static attributes of the class and all its parents up to the top of the hierarchy are final.
- All constructors of the class do nothing apart setting attributes or calling other constructors.
- Class has either no parent, or, if having one, neither extends it with new fields or methods, nor overrides anything.

Example of equivalence-compliant class is the class `Counter` below:

```java
class Counter {
    private final Map<String, Integer> state;
    private final String identity;

    public Counter(String identity, Map<String, Integer> state) {
        this.state = state;
        this.identity = identity;
    }
    
    public final Integer next() {
        return state.compute(identity, (key, value) -> {
            if(Objects.isNull(value)) {
                return 0;
            } else {
                return value++;
            }
        });
    }
}
```

In the code snippet below, counter `a` is equivalent to counter `b`, but not equivalent to counter `c`:

```java
class Main {
    public static void main(String... args) {
        Counter a = new Counter("A");
        a.next(); // 0

        Counter b = new Counter("A");
        b.next(); // 1
        a.next(); // 2

        Counter c = new Counter("C");
        c.next(); // 0
      
        System.out.println(a.equals(b)); //true
        System.out.println(a.equals(c)); //false
    }
}
```


# Contracts
[EquivalenceLogic](equivalence-base/src/main/java/com/pragmaticobjects/oo/equivalence/base/EquivalenceLogic.java) utility 
class defines equivalence logic as a set of static equals/hashCode/toString methods. `EquivalenceLogic` is supposed to be used with `EObject` implementors.
[EObject](equivalence-base/src/main/java/com/pragmaticobjects/oo/equivalence/base/EObject.java) is marker interface, that
declares compliance of the class implementing it with the logic, defined in `EquivalenceLogic`. All equivalence-compliant classes 
are supposed to implement this interface, making methods implementations final.

Though it is possible and supported by Equivalence Maven Plugin, **it is not recommended to implement `EObject` manually**.
Equivalence Maven plugin detects all equivalence-compliant classes, makes them implement `EObject`,
and generates implementation of all three methods automatically.

Also, **`EObject` interface is not recommended to be used in source code directly**, unless you design some tracing or debugging tool which works with Java classes.

All `EObject` inheritors must implement these methods:

- `Object[] attributes();` --- a set of object's attributes, that represent the object's identity.
It's return value must stay constant per object instance. Usually, all non-static attributes of a class is supposed to be returned here, *provided that they are all final*.
- `int hashSeed();` --- a seed, from which the `hashCode` is calculated.
Should return a constant integer.
- `Class<? extends EObject> baseType();` --- a method for obtaining the base type of the instance.
Usually, it is sufficient to just return the class, where the method is implemented, and make it final.

Classes, for which equivalence is applicable, should implement `EObject`.
Non-abstract EObject implementors must make the methods implementations final.
All classes, that implement `EObject` manually, must also define `equals/hashCode/toString` triad,
delegating all calls from them to `EquivalenceLogic`.

Example of `EObject` implementation for the `Counter` class:

```java
class Counter implements EObject {
  private final Map<String, Integer> state;
  private final String identity;

  public Counter(String identity, Map<String, Integer> state) {
    this.state = state;
    this.identity = identity;
  }

  public final Integer next() {
    return (Integer)this.state.compute(this.identity, (key, value) -> {
      if (Objects.isNull(value)) {
        return 0;
      } else {
        value + 1;
        return value;
      }
    });
  }
  
  protected final Object[] attributes() {
    return new Object[]{this.state, this.identity};
  }
  
  protected final Class baseType() {
    return Counter.class;
  }
  
  protected final int hashSeed() {
    return 17;
  }
  
  public final boolean equals(Object obj) {
    return EquivalenceLogic.equals(this, obj);
  }
 
  public final int hashCode() {
    return EquivalenceLogic.hashCode(this);
  }
 
  public final String toString() {
    return EquivalenceLogic.toString(this);
  }
}
```

## EObject::equals

Returns `true` if the passed reference points to either `this` object, or an equivalent to `this`. If passed object is not instance of `EObject`,
the result will be always `false`, as well as if the passed reference is `null`.

The logic of `EObject::equals` takes identity of both objects (`this` and the one provided as `equals` argument) by calling `attributes` method. Then,
for each attribute, it checks whether the pair of objects referenced by it are either the same, or equivalent. Which means:

- if one of the objects referred by an attribute is not instance of `EObject`, the references are compared by `==` operator.
- if both objects are `EObject`, the references are compared by calling `equals` method on them.

### Natural equivalence

In Java, there are certain classes, that don't implement `EObject`, but behave like they are equivalence-compliant.
Examples are boxed primitives and `java.util.UUID`. The special trait about all of them is that they are immutable 
and different instances of the same value don't impact the result of execution where they participate in.
Later in this document, I will call such classes "natural-equivalence-compliant",
and two equivalent instances of these classes --- naturally equivalent.

For natural-equvalence-compliant objects, exclusions from the rules above are made: despite the fact that they don't extend `EObject`, 
they are still compared by means of calling `equals` on them, and treated by the `oo-equivalence` as like they
are equivalence-compliant.

A list of naturally-equivalent classes is currently hardcoded in [EquivalenceLogic](equivalence-base/src/main/java/com/pragmaticobjects/oo/equivalence/base/EquivalenceLogic.java).
Hardcode is a temporal measure.

Also, it is possible to enforce equivalence check for a certain attribute of an object, annotating it with @EquivalenceHint annotation, like in example below:

```java
public class TimeRange {
    public final @EquivalenceHint(enabled = true) LocalDate beginDate;
    public final @EquivalenceHint(enabled = true) LocalDate endDate;

    public TimeRange(LocalDate beginDate, LocalDate endDate) {
        this.beginDate = beginDate;
        this.endDate = endDate;
    }
}
```

After instrumentation, `TimeRange` will compare `beginDate` and `endDate` attributes assuming natural equivalence for them.

## EObject::hashCode

Returns hash code for `EObject`, which is consistent with equivalence results. If two `EObject`'s are equivalent, their `hashCode`'s are guaranteed to return the same result.
Implementation of `EObject::hashCode` obtains (by calling `EObject::attributes`), calculates and combines the hash codes for the attributes of the object
(in the same manner like it is proposed by Josh Bloch's Effective Java) but with certain corrections:

- if the attribute is instance of `EObject` (or of some natural-equivalence-compliant type), it's hash code is obtained by means of calling `hashCode` on it.
- if the attribute is not equivalence-compliant, its hash code is obtained by calling `System::identityHashCode` on it.

## EObject::toString

`EObject`'s `toString` method is reserved for tracing and debugging purposes. It prints out the object's internal structure.
Placed into exceptions' messages and logs,
`toString` output provides information to developers about the internal structure for the object of interest.
For two equivalent objects, output of `toString` is guaranteed to be the same.

**Note** that `EObject::toString`'s output is not intended for parsing in application.

## Instrumentation procedure

Instrumentation procedure is implemented as a Maven plugin with two goals:

- `instrument` goal instruments main classes at `process-classes` phase
- `instrument-tests` goal instruments test classes at `process-test-classes` phase

For each class, Equivalence Maven plugin executes the sequence of actions
(see [StandardInstrumentationStage](equivalence-codegen/src/main/java/com/pragmaticobjects/oo/equivalence/codegen/stage/StandardInstrumentationStage.java) class). This sequence includes:

- *Prechecks* --- at this stage, each project class is checked for compliance to equivalence logic.
- *Marking EObjects* --- each equivalence-compliant class is instrumented to implement `EObject`.
- *Implementing EObjects* --- implementation of `EObject`'s abstract methods and `equals/hashCode/toString` triad is generated for each class, marked with `EObject` on previous step.
- *Postchecks* --- verification stage, checking sanity and consistency for all equivalence-compliant classes within the project.
  It is mainly checking that the methods of equivalence-compliant classes
  are not overridable, aliases from equivalence-compliant objects doesn't declare new fields and methods, etc.
  On any mismatch found, the build is failed.
