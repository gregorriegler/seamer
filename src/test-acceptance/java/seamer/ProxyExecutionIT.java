package seamer;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import seamer.core.Invocation;
import seamer.core.ProxySeam;
import seamer.core.Seam;
import seamer.core.Seamer;
import seamer.file.FileInvocationRecorder;
import seamer.file.FileSeamPersister;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProxyExecutionIT {

    public static final String SEAM_ID = ProxyExecutionIT.class.getName();

    @BeforeAll
    void setUp() {
        SeamerFactory.reset(SEAM_ID);

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ProxyDemo.class);
        enhancer.setCallback(new MethodInterceptor() {

            private boolean record = false;

            @Override
            public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                if (record == true) {
                    Object result = proxy.invokeSuper(target, args);
                    new FileInvocationRecorder(SEAM_ID).record(Invocation.of(args, result));
                    return result;
                }

                Seam<String> seam = new ProxySeam(target, method.getName());

                Seamer<String> seamer = new Seamer<>(
                    seam,
                    new FileSeamPersister(SEAM_ID),
                    new FileInvocationRecorder(SEAM_ID)
                );
                seamer.persist(target.getClass());

                record = true;
                String result = seamer.execute(args);
                return result;
            }
        });
        ProxyDemo proxyDemo = (ProxyDemo) enhancer.create();
        proxyDemo.blackbox("hello", 1);
        proxyDemo.blackbox("world", 2);
        proxyDemo.blackbox("hello", 3);
        proxyDemo.blackbox("world", 4);
    }

    @ParameterizedTest
    @MethodSource("invocations")
    void testAllInvocations(Object[] args, Object expected) {
        Seamer seamer = SeamerFactory.load(SEAM_ID, ProxyDemo.class);

        Object actual = seamer.execute(args);

        assertEquals(expected, actual);
    }

    public Stream<Arguments> invocations() {
        List<Invocation> invocations = SeamerFactory.loadInvocations(SEAM_ID);
        return invocations.stream()
            .map(c -> Arguments.of(c.getArgs(), c.getResult()));
    }

    public static class ProxyDemo {

        public String blackbox(String arg1, Integer arg2) {
            String result = arg1 + arg2 + "r";
            System.out.println(result);
            return result;
        }
    }


}
