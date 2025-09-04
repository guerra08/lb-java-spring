package org.example.lbjavaspring.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lbjavaspring.core.HealthCheck;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulingConfiguration {

    private final HealthCheck healthCheck;

    /**
     * Schedules health check of the configured servers at every 10 seconds, initial delay of 5 seconds
     */
    @Scheduled(fixedRate = 10_000L, initialDelay = 5_000L, scheduler = "lbTaskScheduler")
    public void healthCheck() {
        try {
            this.healthCheck.healthCheck();
        } catch (Exception e) {
            // Prevent scheduler thread death on unexpected exceptions
            log.error("Unhandled error during health check execution", e);
        }
    }

}
