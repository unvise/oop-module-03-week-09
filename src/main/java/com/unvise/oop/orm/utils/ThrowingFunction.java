package com.unvise.oop.orm.utils;

import java.util.function.Function;

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> {
    R apply(T t) throws E;

    static <T, R> Function<T, R> wrap(ThrowingFunction<T, R, Exception> throwingFunction) {
        return t -> {
            try {
                return throwingFunction.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
