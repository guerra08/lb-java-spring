package org.example.lbjavaspring.data;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record AddServerRequest(@NonNull String name, @NonNull String address) {
}
