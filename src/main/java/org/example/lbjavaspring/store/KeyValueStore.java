package org.example.lbjavaspring.store;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface KeyValueStore<T> {
    Collection<T> getAll();
    Set<Map.Entry<String, T>> getAllEntries();
    void put(String key, T value);
    void remove(String key);
}
