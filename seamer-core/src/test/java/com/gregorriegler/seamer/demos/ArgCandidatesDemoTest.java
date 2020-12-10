package com.gregorriegler.seamer.demos;

import com.gregorriegler.seamer.Seamer;
import com.gregorriegler.seamer.core.InvokableWith3Arguments;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Arrays.asList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArgCandidatesDemoTest {

    private static final Logger LOG = LoggerFactory.getLogger(ArgCandidatesDemoTest.class);
    public static final String SEAM_ID = ArgCandidatesDemoTest.class.getName();
    private final Seamer seamer = Seamer.create();

    private ArgCandidatesDemo demo;

    @BeforeAll
    void setUp() {
        seamer.reset(SEAM_ID);
        demo = new ArgCandidatesDemo();
        demo.entrypoint(null, null, null); // persist seam

        seamer.createCustomRecordings(SEAM_ID)
            .addArgCandidates(0, null, "hello", "world")
            .addArgCandidates(1, null, 1, 2, 3)
            .addArgCandidates(2, new SomeObject("hello", SomeObjectState.READY), new SomeObject("world", SomeObjectState.DONE))
            .addArgCandidates(2, () -> asList(new SomeObject("foo", SomeObjectState.READY), new SomeObject("bar", SomeObjectState.DONE)))
            .shuffleArgsAndRecord();
    }

    @Test
    void verify() {
        seamer.get(SEAM_ID)
            .verify();
    }

    public static class ArgCandidatesDemo {

        public void entrypoint(String arg1, Integer arg2, SomeObject arg3) {
            String result = Seamer.create()
                .define(SEAM_ID, (InvokableWith3Arguments<String, Integer, SomeObject, String>) this::blackbox)
                .invokeAndRecord(arg1, arg2, arg3);

            LOG.info(result);
        }

        public String blackbox(String arg1, Integer arg2, SomeObject arg3) {
            if (arg3 != null && SomeObjectState.READY == arg3.state) {
                return arg1 + arg2 + "r";
            } else {
                return arg1 + arg2 + "d";
            }
        }
    }

    private static class SomeObject {

        private final String title;
        private final SomeObjectState state;

        private SomeObject() {
            title = null;
            state = null;
        }

        public SomeObject(String title, SomeObjectState state) {
            this.title = title;
            this.state = state;
        }

        @Override
        public String toString() {
            return "SomeObject{" +
                "title='" + title + '\'' +
                ", state=" + state +
                '}';
        }
    }

    public enum SomeObjectState {
        READY, DONE
    }

}
