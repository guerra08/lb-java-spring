package org.example.lbjavaspring.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lbjavaspring.store.KeyValueStore;
import org.example.lbjavaspring.store.Store;
import org.example.lbjavaspring.core.Algorithm;
import org.example.lbjavaspring.data.ServerInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
@Slf4j
public class LbConfigurationController {

    private final KeyValueStore<ServerInstance> serverStore;
    private final Store<Algorithm> algorithmStore;

    @GetMapping("/algorithm")
    public ResponseEntity<String> getAlgorithm() {
        return ResponseEntity.ok(algorithmStore.get().name());
    }

    @PostMapping("/algorithm")
    public ResponseEntity<String> setAlgorithm(@RequestBody final Map<String, String> payload) {
        try {
            final Algorithm algorithm = Algorithm.valueOf(payload.get("algorithm"));
            algorithmStore.set(algorithm);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Unable to set current algorithm with payload {}", payload, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/server")
    public ResponseEntity<Collection<ServerInstance>> getServers() {
        return ResponseEntity.ok(serverStore.getAll());
    }

}
