package org.example.lbjavaspring.data;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record AddServerPayload(@NonNull String name, @NonNull String address) {
}
