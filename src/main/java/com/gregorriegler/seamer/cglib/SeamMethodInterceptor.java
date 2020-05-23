package com.gregorriegler.seamer.cglib;

import com.gregorriegler.seamer.Seamer;
import com.gregorriegler.seamer.core.ProxyMethod;
import com.gregorriegler.seamer.core.SeamRecorder;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class SeamMethodInterceptor<T> implements MethodInterceptor {

    private String methodName;
    private String seamId;
    private SeamRecorder<T> seam;

    // for deserialization
    public SeamMethodInterceptor() {
    }

    public SeamMethodInterceptor(String methodName, String seamId) {
        this.methodName = methodName;
        this.seamId = seamId;
    }

    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (!methodName.equals(method.getName())) {
            return proxy.invokeSuper(target, args);
        }

        if (seam == null) {
            seam = Seamer.intercept(
                seamId,
                target.getClass(),
                ProxyMethod.of(target, methodName)
            );
            return seam.execute(args);
        }

        T result = invoke(proxy, target, args);
        seam.recordInvocation(args, result);
        return result;
    }

    @SuppressWarnings("unchecked")
    public T invoke(MethodProxy proxy, Object target, Object[] args) throws Throwable {
        return (T) proxy.invokeSuper(target, args);
    }
}
