package org.example.lbjavaspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class LbJavaSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(LbJavaSpringApplication.class, args);
    }

}
