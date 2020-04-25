Seamer - fun with seams
-----------------------

A refactoring tool for Java, designed to help and get legacy code under test.

Inspired by https://github.com/testdouble/suture

>A seam is a place where you can alter behavior in your program without editing in that place.
> 
>~ Michael C. Feathers

### Usage

#### Create Seam
This records all invocations and its return values.

```java
public void entrypoint(String arg1, Integer arg2) {
    String result = Seamer.intercept(
        "UserDefinedIdOfSeam",
        arg -> blackbox((String) arg[0], (Integer) arg[1]), 
        this.getClass()
    ).invoke(arg1, arg2);
    // ...
}

public String blackbox(String arg1, Integer arg2) {
    return arg1 + arg2;
}
```

#### Execute the Seam with specific arguments of your choice
You may record invocations with arguments of your choice by first providing argument candidates for every argument index (0 is the first argument, and so forth).
Seamer will then automatically invoke your blackbox with all possible combinations
of your provided arguments and record the results.
 
```java
Seamer.load("UserDefinedIdOfSeam", classCarryingTheSeam.getClass())
            .addArgCandidates(0, "hello", "world", null)
            .addArgCandidates(1, () -> asList(1, 2, 3))
            .addArgCandidates(2, new SomeObject("hello", SomeObjectState.READY))
            .shuffleArgsAndExecute();
```

These results can then get verified in a test-harness.

#### Execute the Seam in a test-harness
This reruns all previously recorded invocations, and verifies if the results still match.
```java
public class TwoArgTest extends SeamerTest {
    @Override
    public Class<?> capturingClass() {
        return YourClassCapturingTheSeam.class;
    }

    @Override
    public String seamId() {
        return "UserDefinedIdOfSeam";
    }
}
```
or just verify all recorded invocations
```java
Seamer.load(SEAM_ID, demo.getClass()).verify();
```

#### Create a Seam via annotation through AspectJ
This solution only works for stateless methods though.
```java
public static class AspectJDemo {

    @Seam(SEAM_ID)
    public String blackbox(String arg1, Integer arg2) {
        return arg1 + arg2;
    }
}
```

##### Run with Spring 
```java
@RunWith(SpringRunner.class)
public class MissionStateEngineTest {

    @EnableAspectJAutoProxy
    @TestConfiguration
    static class TestConfig {

        @Bean
        public SeamerAspect seamerAspect() {
            return new SeamerAspect();
        }
     
        ...   
    }
    
    ...
}
```

