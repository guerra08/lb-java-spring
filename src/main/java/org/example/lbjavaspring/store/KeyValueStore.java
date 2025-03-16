package org.example.lbjavaspring.store;

import java.util.Collection;

public interface KeyValueStore<T> {
    Collection<T> getAll();
    void put(String key, T value);
}
