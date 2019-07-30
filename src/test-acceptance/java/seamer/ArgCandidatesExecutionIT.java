package seamer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seamer.core.Invocation;
import seamer.core.Seamer;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArgCandidatesExecutionIT {

    private static final Logger LOG = LoggerFactory.getLogger(ArgCandidatesExecutionIT.class);
    public static final String SEAM_ID = ArgCandidatesExecutionIT.class.getName();

    private ArgCandidatesDemo demo;

    @BeforeAll
    void setUp() {
        SeamerFactory.reset(SEAM_ID);
        demo = new ArgCandidatesDemo();
        demo.entrypoint(null, null, null); // persist seam

        SeamerFactory.load(SEAM_ID, demo.getClass())
            .addArgCandidates(0, null, "hello", "world")
            .addArgCandidates(1, null, 1, 2, 3)
            .addArgCandidates(2, new SomeObject("hello", SomeObjectState.READY), new SomeObject("world", SomeObjectState.DONE))
            .shuffleArgsAndExecute();
    }

    @ParameterizedTest
    @MethodSource("invocations")
    void testAllInvocations(Object[] args, Object expected) {
        Seamer seamer = SeamerFactory.load(SEAM_ID, demo.getClass());

        Object actual = seamer.execute(args);

        assertEquals(expected, actual);
    }

    public Stream<Arguments> invocations() {
        List<Invocation> invocations = SeamerFactory.loadInvocations(SEAM_ID);
        return invocations.stream()
            .map(c -> Arguments.of(c.getArgs(), c.getResult()));
    }


    public static class ArgCandidatesDemo {

        public void entrypoint(String arg1, Integer arg2, SomeObject arg3) {
            String result = SeamerFactory.createAndPersist(
                a -> blackbox((String) a[0], (Integer) a[1], (SomeObject) a[2]), this.getClass(), SEAM_ID
            ).executeAndRecord(arg1, arg2, arg3);

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
