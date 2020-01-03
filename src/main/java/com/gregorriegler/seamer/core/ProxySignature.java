package com.gregorriegler.seamer.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class ProxySignature<T> implements ArgsSignature<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ProxySignature.class);

    private final Object target;
    private final String methodName;

    private ProxySignature(Object target, String methodName) {
        this.target = target;
        this.methodName = methodName;
    }

    public static <T> ProxySignature<T> of(Object target, String methodName) {
        return new ProxySignature<>(target, methodName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T apply(Object[] args) {
        try {
            Class<?>[] argsClasses = Arrays.stream(args)
                .map(arg -> arg.getClass())
                .toArray(Class<?>[]::new);
            Method[] declaredMethods = target.getClass().getDeclaredMethods();
            Optional<Method> optionalMethod = Arrays.stream(declaredMethods)
                .filter(m -> m.getName().equals(methodName))
                .findFirst();
            Method method = optionalMethod.orElseThrow(() -> new NoSuchMethodException());
            return (T) method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOG.error("failed to invoke seam", e);
            throw new SeamerFailed();
        }
    }
}
