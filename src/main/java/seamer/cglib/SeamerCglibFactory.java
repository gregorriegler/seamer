package seamer.cglib;

import net.sf.cglib.proxy.Enhancer;

public class SeamerCglibFactory {
    @SuppressWarnings("unchecked")
    public static  <T> T createProxySeam(Class<T> clazz, String methodName, String seamId) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new SeamInterceptor(methodName, seamId));
        return (T) enhancer.create();
    }
}
