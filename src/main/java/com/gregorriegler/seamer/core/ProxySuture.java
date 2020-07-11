package com.gregorriegler.seamer.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class ProxySuture<T> implements ArgsSuture<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ProxySuture.class);

    private Object target;
    private String methodName;

    private ProxySuture(Object target, String methodName) {
        this.target = target;
        this.methodName = methodName;
    }

    // for deserialization
    private ProxySuture() {
    }

    public static <T> ProxySuture<T> of(Object target, String methodName) {
        return new ProxySuture<>(target, methodName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T apply(Object[] args) {
        try {
            Method[] declaredMethods = target.getClass().getDeclaredMethods();
            Optional<Method> optionalMethod = Arrays.stream(declaredMethods)
                .filter(m -> m.getName().equals(methodName))
                .findFirst();
            Method method = optionalMethod.orElseThrow(NoSuchMethodException::new);
            return (T) method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOG.error("failed to invoke seam", e);
            throw new SeamerFailed();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProxySuture<?> that = (ProxySuture<?>) o;
        return Objects.equals(target, that.target) &&
            Objects.equals(methodName, that.methodName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(target, methodName);
    }

    @Override
    public String toString() {
        return "ProxySignature{" +
            "target=" + target +
            ", methodName='" + methodName + '\'' +
            '}';
    }
}
