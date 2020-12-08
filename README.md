Seamer - gets your legacy code under test
----------------------------------------
[![Build Status](https://travis-ci.com/gregorriegler/seamer.svg?branch=master)](https://travis-ci.com/gregorriegler/seamer)

Seamer aims at making it easy for you to create characterization tests.

### Usage

Suppose you have a code like the following:

```java
String result = someReallyComplicatedLegacyMethod(String param1, Integer param2);

doSometingWith(result);
```

You have no idea what `someReallyComplicatedLegacyMethod` is doing so you want to refactor it safely.
This is where Seamer comes in handy. 
Seamer allows you to intercept the method and record all invocations and results like the following:

```java
String result = Seamer.create()
    .define("MySeam",  // this is just an id of your choice
        (InvokableWith2Arguments<String, Integer, String>) this::someReallyComplicatedLegacyMethod)
    .invokeAndRecord(param1, param2);

doSometingWith(result);
```
Now the code still does the same thing, plus it will record all invocations of the Seam.

##### Taking advantage of closures

As the lambda expression results in a closure that captures its surrounding state, 
the recordings of this seam will be repeatable even if they depend on surrounding state.
Suppose the method is incrementing an `int` field of the surrounding class, 
that side effect will be repeated when we verify the seam.

Check out the [ClosureSeamTest](src/test/java/com/gregorriegler.seamer/ClosureSeamTest.java) which demonstrates this behavior.

#### Recording some invocations
You may now run your application. You would click around the UI and have this thing invoked a couple times with realistic arguments.
Or, you would record some invocations using the following code.

```java
// shuffles given argument candidates, records all possible scenarios and its results.
Seamer.create()
    .customRecordings("MySeam")
    .addArgCandidates(0, "hello", "world", null)
    .addArgCandidates(1, () -> asList(1, 2, 3))
    .shuffleArgsAndExecute();
```

#### Verifying the Seam in a test-harness
You may now setup a test that replays all recorded invocations and verifies if the code is still doing what it is supposed to do.

```java
@Test
void verify_everything_still_works() {
    Seamer.create().verify("MySeam");
}
```

And that's it. you may now refactor the code using this test as a feedback tool.

#### Persistence Configuration
##### The default
By default Seamer is using a file-based persistence that stores its data into `src/test/java/seamer`

##### Custom Path
If you wanted to store Seamers recorded data somewhere else, just define the path like so
```java
Seamer.create("/tmp/seamer")
```

##### Sqlite
If you wanted to store Seamers recorded data in an Sqlite DB, create Seamer like the following
```java
Seamer.create(new SqlitePersistence("jdbc:sqlite:/tmp/seamer"))
```
or even shorter:
```java
Seamer.create(SqlitePersistence.atTmp())
```

#### Create a seam via Proxy
##### Spring+AspectJ
```java
public static class AspectJDemo {

    @Seam("MySeam")
    public String legacyMethod(String arg1, Integer arg2) {
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
