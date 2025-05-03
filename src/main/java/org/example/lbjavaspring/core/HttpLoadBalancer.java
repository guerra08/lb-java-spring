package org.example.lbjavaspring.core;

import lombok.extern.slf4j.Slf4j;
import org.example.lbjavaspring.store.KeyValueStore;
import org.example.lbjavaspring.store.Store;
import org.example.lbjavaspring.data.Request;
import org.example.lbjavaspring.data.ServerInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class HttpLoadBalancer implements LoadBalancer {

    private final RestClient restClient = RestClient.create();
    private final AtomicInteger roundRobinCounter = new AtomicInteger(0);
    private final Store<Algorithm> algorithmStore;
    private final KeyValueStore<ServerInstance> serverStore;

    public HttpLoadBalancer(Store<Algorithm> algorithmStore, KeyValueStore<ServerInstance> serverStore) {
        this.algorithmStore = algorithmStore;
        this.serverStore = serverStore;
    }

    @Override
    public ResponseEntity<String> handle(final Request request) {
        final ServerInstance handler = selectServer();
        log.info("Handling {} request {} on server {}", request.method(), request.path(), handler);
        final String target = handler.getServer().address().concat(request.path());

        handler.incrementConnections();

        final long startTime = System.currentTimeMillis();
        final ResponseEntity<String> response = restClient
                .method(request.method())
                .uri(target)
                .retrieve()
                .toEntity(String.class);
        final long endTime = System.currentTimeMillis() - startTime;
        log.info("{} took {} ms to handle request", handler.getServer().name(), endTime);

        handler.updateLastResponseTime(endTime);
        handler.decrementConnections();

        return response;
    }

    private ServerInstance selectServer() {
        return switch (algorithmStore.get()) {
            case ROUND_ROBIN -> selectForRoundRobin();
            case LEAST_CONNECTIONS -> selectForLeastConnections();
            case RESPONSE_TIME -> selectForResponseTime();
        };
    }

    private ServerInstance selectForResponseTime() {
        return healthyServers().stream().min(Comparator.comparingLong(s -> s.getLastResponseTime().get())).orElseThrow(() -> new IllegalStateException("Unable to find server with least response time"));
    }

    private ServerInstance selectForLeastConnections() {
        return healthyServers().stream().min(Comparator.comparingInt(s -> s.getConnections().get())).orElseThrow(() -> new IllegalStateException("Unable to find server with least connections"));
    }

    private ServerInstance selectForRoundRobin() {
        int index = roundRobinCounter.getAndIncrement() % healthyServers().size(); // Get the current index, increment and check if it's within the range of servers
        return healthyServers().toArray(new ServerInstance[0])[index];
    }

    private Collection<ServerInstance> healthyServers() {
        return this.serverStore.getAll();
    }

}
