package org.example.lbjavaspring.data;

import lombok.Builder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@Builder
public record Request(String path, HttpMethod method, HttpHeaders headers, byte[] body) {
}
