package com.unvise.oop.orm.utils;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

@Slf4j
public class ProxyUtils {
    public static <T> T cglibProxy(Class<T> clazz, Callback callback) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(callback);
        log.debug("Инициализация прокси - {}", clazz.getSimpleName());
        return clazz.cast(enhancer.create());
    }

    public static <T> T cglibProxy(Class<T> clazz) {
        return cglibProxy(clazz, (MethodInterceptor) (obj, method, args1, proxy) -> proxy.invokeSuper(obj, args1));
    }
}
