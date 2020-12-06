package com.gregorriegler.seamer.cglib;

import com.gregorriegler.seamer.file.FileLocation;
import net.sf.cglib.proxy.Enhancer;

public class SeamerCglibFactory {
    @SuppressWarnings("unchecked")
    public static <T> T createProxySeam(Class<T> clazz, String methodName, String seamId) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new SeamMethodInterceptor<T>(FileLocation.basePath(), methodName, seamId));
        return (T) enhancer.create();
    }

    public static <T> T createProxySeam(String basePath, Class<T> clazz, String methodName, String seamId) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new SeamMethodInterceptor<T>(basePath, methodName, seamId));
        return (T) enhancer.create();
    }
}
