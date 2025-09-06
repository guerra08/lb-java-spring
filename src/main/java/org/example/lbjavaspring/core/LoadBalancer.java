package org.example.lbjavaspring.core;

public interface LoadBalancer<T, R> {
    R handle(T request);
}
