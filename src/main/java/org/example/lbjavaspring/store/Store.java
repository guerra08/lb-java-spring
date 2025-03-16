package org.example.lbjavaspring.store;

public interface Store<T>{
    T get();
    void set(T value);
}
