package org.example.lbjavaspring.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lbjavaspring.data.AddServerRequest;
import org.example.lbjavaspring.data.Server;
import org.example.lbjavaspring.data.ServerStatistics;
import org.example.lbjavaspring.store.KeyValueStore;
import org.example.lbjavaspring.store.Store;
import org.example.lbjavaspring.core.Algorithm;
import org.example.lbjavaspring.data.ServerInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
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

    @GetMapping("/server/statistics")
    public ResponseEntity<List<ServerStatistics>> getServerStatistics() {
        return ResponseEntity.ok().body(serverStore.getAll().stream().map(ServerStatistics::fromServer).toList());
    }

    @PostMapping("/server")
    public ResponseEntity<String> addServer(@RequestBody final AddServerRequest addServerRequest) {
        serverStore.put(addServerRequest.name(), getServerInstance(addServerRequest));
        return ResponseEntity.ok().build();
    }

    private static ServerInstance getServerInstance(final AddServerRequest addServerRequest) {
        return ServerInstance.builder().server(Server.builder().name(addServerRequest.name()).address(addServerRequest.address()).build()).build();
    }

}
