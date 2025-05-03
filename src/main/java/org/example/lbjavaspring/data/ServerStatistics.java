package org.example.lbjavaspring.data;

public record ServerStatistics(String name, String address, int connections, long lastResponseTime, boolean healthy) {
    public static ServerStatistics fromServer(final ServerInstance serverInstance) {
        return new ServerStatistics(serverInstance.getServer().name(), serverInstance.getServer().address(), serverInstance.connections(), serverInstance.lastResponseTime(), serverInstance.getIsHealthy().get());
    }
}
