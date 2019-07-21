Seamer - fun with seams
-----------------------

A refactoring tool for Java, designed to help and get _blackboxes_ under test to enable safe refactorings.
Works with pure functions as well with those with side-effects. One seam per class is currently supported. 

Inspired by https://github.com/testdouble/suture

>A seam is a place where you can alter behavior in your program without editing in that place.
> 
>~ Michael C. Feathers

### Usage

#### Create Seam
This records all invocations and their return values of the blackbox.

```
public void entrypoint(String arg1, Integer arg2) {
    String result = SeamerFactory.createAndPersist(a -> blackbox((String) a[0], (Integer) a[1]), this)
                          .executeAndRecord(arg1, arg2);
    // ...
}

public String blackbox(String arg1, Integer arg2) {
    return arg1 + arg2;
}
```

#### Execute the Seam with specific arguments of your choice
You may record invocations with arguments of your choice, by first providing argument 
candidates for every argument index (0 is the first argument, and so forth).
Seamer will then automatically invoke your blackbox with all possible combinations
of your provided arguments and record the results.
 
```
SeamerFactory.load(demo)
            .addArgCandidates(0, null, "hello", "world")
            .addArgCandidates(1, null, 1, 2, 3)
            .addArgCandidates(2, new SomeObject("hello", SomeObjectState.READY))
            .shuffleArgsAndExecute();
```

These results can then get verified in a test-harness.

#### Execute the Seam of a pure function in a test-harness
This reruns all previously recorded invocations, and verifies if the results still match.
```
public class TwoArgTest extends PureSeamTest {
    @Override
    public Object createCarrier() {
        return new YourClassCarryingTheSeam();
    }
}
```

#### Execute the Seam of a function that has side-effects in a test-harness
This reruns all previously recorded invocations, and verifies if the results still match.
```
public class TwoArgTest extends SideEffectSeamTest {
    @Override
    public Object createCarrier() {
        return new YourClassCarryingTheSeam();
    }
}
```

