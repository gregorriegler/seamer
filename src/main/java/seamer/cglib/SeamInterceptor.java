package seamer.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import seamer.SeamerFactory;
import seamer.core.ProxySeam;
import seamer.core.Seamer;

import java.lang.reflect.Method;

public class SeamInterceptor<T> implements MethodInterceptor {

    private final String methodName;
    private final String seamId;
    private Seamer<T> seamer;

    public SeamInterceptor(String methodName, String seamId) {
        this.methodName = methodName;
        this.seamId = seamId;
    }

    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (!methodName.equals(method.getName())) {
            return proxy.invokeSuper(target, args);
        }

        if (seamer == null) {
            seamer = SeamerFactory.intercept(
                ProxySeam.of(target, methodName),
                target.getClass(),
                seamId
            );
            return seamer.execute(args);
        }

        T result = invoke(proxy, target, args);
        seamer.record(args, result);
        return result;
    }

    @SuppressWarnings("unchecked")
    public T invoke(MethodProxy proxy, Object target, Object[] args) throws Throwable {
        return (T) proxy.invokeSuper(target, args);
    }
}
