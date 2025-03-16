package org.example.lbjavaspring.core;

import org.example.lbjavaspring.data.Request;
import org.springframework.http.ResponseEntity;

public interface LoadBalancer {
    ResponseEntity<String> handle(Request request);
}
