package com.springboot.sample.future;

public interface CustomCallable<V> {
    V call() throws Exception;
}
