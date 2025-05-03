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

    @Scheduled(fixedRate = 5000L)
    public void healthCheck() {
        this.healthCheck.healthCheck();
    }
    
}
