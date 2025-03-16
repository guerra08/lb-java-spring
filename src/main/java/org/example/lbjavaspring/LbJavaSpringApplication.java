package org.example.lbjavaspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LbJavaSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(LbJavaSpringApplication.class, args);
        // Load server configuration
        // Schedule health check
        // Start load balancer service
    }

}
