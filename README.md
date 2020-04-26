Seamer - gets your legacy code under test
----------------------------------------

Seamer is a tool that aims at making it easy to create characterization tests.

### Usage

Suppose you have a code like the following.

```
String result = someReallyComplicatedLegacyMethod(String param1, Integer param2);

doSometingWith(result);
```

You have no idea what `someReallyComplicatedLegacyMethod` is doing so you want to refactor it safely.
This is where Seamer comes to play. 
Seamer allows you to intercept the method, and record all invocations and results like the following.

```
String result = Seamer.intercept(
    "MySeam",
    this.getClass(),
    (SignatureWith2Arguments<String, Integer, String>) this::someReallyComplicatedLegacyMethod
).invoke(param1, param2);

doSometingWith(result);
```

#### Recording some invocations
You may now run your application. You would click around the UI and have this thing invoked a couple times with realistic arguments.
Or, you would record some invocations using the following code.

```java
// shuffles given argument candidates, records all possible scenarios and its results.
Seamer.load("MySeam", ClassCapturingTheSeam.class)
            .addArgCandidates(0, "hello", "world", null)
            .addArgCandidates(1, () -> asList(1, 2, 3))
            .shuffleArgsAndExecute();
```

These results can then get verified in a test-harness.

#### Verifying the Seam in a test-harness
You may now setup a test that replays all recorded invocations and verifies if the code is still doing what it is supposed to do.
It's really easy.
```java
public class EverythingStillWorkingTest extends SeamerTest {
    @Override
    public Class<?> capturingClass() {
        return ClassCapturingTheSeam.class;
    }

    @Override
    public String seamId() {
        return "MySeam";
    }
}
```

And that's it. you may now refactor the code using this test as a feedback tool.

Another way to verify the Seam without a JUnit TestCase is to run the following code.
```java
Seamer.load("MySeam", ClassCapturingTheSeam.class).verify();
```

#### Create a seam via Proxy
##### Spring+AspectJ
```java
public static class AspectJDemo {

    @Seam("MySeam")
    public String blackbox(String arg1, Integer arg2) {
        return arg1 + arg2;
    }
}
```
Make sure to enable the SeamerAspect 
```java
@EnableAspectJAutoProxy
@Configuration
class SeamerConfig {

    @Bean
    public SeamerAspect seamerAspect() {
        return new SeamerAspect();
    }   
}
```
##### cglib
```java
SeamerCglibFactory.createProxySeam(ClassCaputringTheSeam.class, "legacyMethod", "MySeam")
```




#### Suture
This tool is inspired by https://github.com/testdouble/suture which does a similar thing in ruby.
