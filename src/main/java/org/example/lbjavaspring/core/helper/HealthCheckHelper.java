package org.example.lbjavaspring.core.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class HealthCheckHelper {

    private final RestClient restClient = RestClient.create();

    @Retryable(backoff = @Backoff(delay = 500L, multiplier = 2))
    public boolean isServerHealthy(final String serverAddress) {
        log.info("Checking health of server {}...", serverAddress);
        final ResponseEntity<String> response = restClient
                .get()
                .uri(serverAddress)
                .retrieve()
                .toEntity(String.class);

        return response.getStatusCode().is2xxSuccessful();
    }

}
