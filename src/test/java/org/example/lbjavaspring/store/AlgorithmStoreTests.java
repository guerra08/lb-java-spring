package org.example.lbjavaspring.store;

import org.example.lbjavaspring.core.Algorithm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlgorithmStoreTests {

    @Test
    void defaultAlgorithmIsRoundRobin() {
        AlgorithmStore store = new AlgorithmStore();
        assertEquals(Algorithm.ROUND_ROBIN, store.get());
    }

    @Test
    void canUpdateAlgorithm() {
        AlgorithmStore store = new AlgorithmStore();
        store.set(Algorithm.LEAST_CONNECTIONS);
        assertEquals(Algorithm.LEAST_CONNECTIONS, store.get());

        store.set(Algorithm.RESPONSE_TIME);
        assertEquals(Algorithm.RESPONSE_TIME, store.get());
    }
}
