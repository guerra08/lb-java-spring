package org.example.lbjavaspring.core;

import lombok.extern.slf4j.Slf4j;
import org.example.lbjavaspring.store.KeyValueStore;
import org.example.lbjavaspring.store.Store;
import org.example.lbjavaspring.data.Request;
import org.example.lbjavaspring.data.ServerInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class HttpLoadBalancer implements LoadBalancer {

    private final RestTemplate restTemplate;
    private final AtomicInteger roundRobinCounter = new AtomicInteger(0);
    private final Store<Algorithm> algorithmStore;
    private final KeyValueStore<ServerInstance> serverStore;

    public HttpLoadBalancer(RestTemplate restTemplate, Store<Algorithm> algorithmStore, KeyValueStore<ServerInstance> serverStore) {
        this.restTemplate = restTemplate;
        this.algorithmStore = algorithmStore;
        this.serverStore = serverStore;
    }

    @Override
    public ResponseEntity<String> handle(final Request request) {
        final ServerInstance handler = selectServer();
        log.info("Handling {} request {} on server {}", request.method(), request.path(), handler);
        final String target = handler.getServerInfo().address().concat(request.path());

        handler.incrementConnections();

        final long startTime = System.currentTimeMillis();
        final ResponseEntity<String> response = restTemplate.getForEntity(target, String.class);
        final long endTime = System.currentTimeMillis() - startTime;
        log.info("{} took {} ms to handle request", handler.getServerInfo().name(), endTime);

        handler.updateLastResponseTime(endTime);
        handler.decrementConnections();

        return response;
    }

    private ServerInstance selectServer() {
        return switch (algorithmStore.get()) {
            case ROUND_ROBIN -> {
                int index = roundRobinCounter.getAndIncrement() % healthyServers().size(); // Get the current index, increment and check if it's within the range of servers
                yield healthyServers().toArray(new ServerInstance[0])[index];
            }
            case LEAST_CONNECTIONS ->
                    healthyServers().stream().min(Comparator.comparingInt(s -> s.getConnections().get())).orElseThrow();
            case RESPONSE_TIME ->
                    healthyServers().stream().min(Comparator.comparingLong(s -> s.getLastResponseTime().get())).orElseThrow();
        };
    }

    private Collection<ServerInstance> healthyServers() {
        return this.serverStore.getAll();
    }

    //TODO: Feature -> Add health check on the servers, and remove unhealthy servers
    //TODO: Feature -> Add new servers on the fly with an endpoint
    //TODO: Feature -> Statistics endpoint
}
