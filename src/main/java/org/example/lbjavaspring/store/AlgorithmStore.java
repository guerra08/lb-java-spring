package org.example.lbjavaspring.store;

import org.example.lbjavaspring.core.Algorithm;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicReference;

@Configuration
public class AlgorithmStore implements Store<Algorithm> {

    private final AtomicReference<Algorithm> currentAlgorithm = new AtomicReference<>(Algorithm.ROUND_ROBIN); // Default algorithm is ROUND_ROBIN

    @Override
    public Algorithm get() {
        return currentAlgorithm.get();
    }

    @Override
    public void set(Algorithm value) {
        this.currentAlgorithm.set(value);
    }
}
