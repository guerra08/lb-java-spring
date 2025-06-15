package org.example.lbjavaspring.data;

import lombok.Builder;

@Builder
public record ServerStatistics(String name, String address, int connections, long lastResponseTime, boolean healthy) {
    public static ServerStatistics fromServer(final ServerInstance serverInstance) {
        return ServerStatistics.builder()
                .name(serverInstance.getServer().name())
                .address(serverInstance.getServer().address())
                .connections(serverInstance.connections())
                .lastResponseTime(serverInstance.lastResponseTime())
                .healthy(serverInstance.getIsHealthy().get())
                .build();
    }
}