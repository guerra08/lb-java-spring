package org.example.lbjavaspring.data;

import lombok.Builder;

@Builder
public record Server(String name, String address) {
}
