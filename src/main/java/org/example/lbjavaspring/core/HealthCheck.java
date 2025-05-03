package org.example.lbjavaspring.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lbjavaspring.store.ServerStore;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class HealthCheck {

    private final RestTemplate restTemplate;
    private final ServerStore serverStore;

    public void healthCheck() {
        log.info("Executing health check on servers...");

        if(serverStore.getAll().isEmpty()) {
            log.error("No healthy servers available");
        }

        serverStore.getAllEntries().parallelStream().forEach(entry -> {
            try {
                final ResponseEntity<String> response = restTemplate.getForEntity(entry.getValue().getServer().address().concat("/health"), String.class);
                if (!response.getStatusCode().is2xxSuccessful()) {
                    log.error("Server {} is not healthy", entry.getValue().getServer().name());
                    serverStore.remove(entry.getKey());
                }
            } catch (Exception e) {
                log.error("Server {} is not healthy", entry.getValue().getServer().name(), e);
                serverStore.remove(entry.getKey());
            }
        });
    }

}
