package org.example.lbjavaspring.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
@Slf4j
public class TaskSchedulingConfig {

    @Bean("lbTaskScheduler")
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(2);
        scheduler.setThreadNamePrefix("lb-scheduler-");
        scheduler.setErrorHandler(t -> log.error("Unhandled exception in scheduled task", t));
        scheduler.setRemoveOnCancelPolicy(true);
        return scheduler;
    }
}
