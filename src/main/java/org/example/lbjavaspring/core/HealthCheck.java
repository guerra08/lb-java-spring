package org.example.lbjavaspring.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lbjavaspring.core.helper.HealthCheckHelper;
import org.example.lbjavaspring.data.ServerInstance;
import org.example.lbjavaspring.store.ServerStore;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class HealthCheck {

    private final ServerStore serverStore;
    private final HealthCheckHelper helper;

    public void healthCheck() {
        log.info("Executing health check on servers...");

        if(serverStore.getAll().isEmpty()) {
            log.error("No healthy servers available");
        }

        serverStore.getAllEntries().forEach(mapConsumer());
    }

    private Consumer<Map.Entry<String, ServerInstance>> mapConsumer() {
        return entry -> {
            try {
                final String uri = entry.getValue().getServer().address().concat("/health");
                if (!helper.isServerHealthy(uri)) {
                    log.error("Server {} is not healthy", entry.getValue().getServer().name());
                    serverStore.remove(entry.getKey());
                }
            } catch (Exception e) {
                log.error("Server {} is not healthy", entry.getValue().getServer().name(), e);
                serverStore.remove(entry.getKey());
            }
        };
    }

}
