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
    String result = SeamerFactory.createAndPersist(
        arg -> blackbox((String) arg[0], (Integer) arg[1]), 
        this.getClass(), 
        "UserDefinedIdOfSeam"
    ).executeAndRecord(arg1, arg2);
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
SeamerFactory.load("UserDefinedIdOfSeam", classCarryingTheSeam.getClass())
            .addArgCandidates(0, "hello", "world", null)
            .addArgCandidates(1, () -> asList(1, 2, 3))
            .addArgCandidates(2, new SomeObject("hello", SomeObjectState.READY))
            .shuffleArgsAndExecute();
```

These results can then get verified in a test-harness.

#### Execute the Seam of a pure function in a test-harness
This reruns all previously recorded invocations, and verifies if the results still match.
```
public class TwoArgTest extends PureSeamTest {
    @Override
    public Class carrierClass() {
        return YourClassCarryingTheSeam.class;
    }

    @Override
    public String seamId() {
        return "UserDefinedIdOfSeam";
    }
}
```

#### Execute the Seam of a non-pure function in a test-harness
Use this route if your seam relies on state within the carrying class (fields).
This reruns all previously recorded invocations, and verifies if the results still match.
```
public class TwoArgTest extends ClosureSeamTest {
    @Override
    public Class carrierClass() {
        return YourClassCarryingTheSeam.class;
    }

    @Override
    public String seamId() {
        return "UserDefinedIdOfSeam";
    }
}
```

#### Create a seam via annotation through AspectJ
This solution only works for pure functions though.
```
public static class AspectJDemo {

    @Seam(SEAM_ID)
    public String blackbox(String arg1, Integer arg2) {
        return arg1 + arg2;
    }
}
```

