package seamer.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ProxySeam<T> implements Seam<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ProxySeam.class);

    private final Object target;
    private final String methodName;

    public ProxySeam(Object target, String methodName) {
        this.target = target;
        this.methodName = methodName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T apply(Object[] args) {
        try {
            Class<?>[] argsClasses = Arrays.stream(args)
                .map(arg -> arg.getClass())
                .toArray(Class<?>[]::new);
            Method method = target.getClass().getMethod(methodName, argsClasses);
            return (T) method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOG.error("failed to invoke seam", e);
            throw new SeamerFailed();
        }
    }
}
