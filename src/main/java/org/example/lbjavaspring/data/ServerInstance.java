package org.example.lbjavaspring.data;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Builder
@Getter
@ToString
public class ServerInstance {

    private Server server;
    @Builder.Default
    private AtomicBoolean isHealthy = new AtomicBoolean(true);
    @Default
    private AtomicInteger connections = new AtomicInteger(0);
    @Default
    private AtomicLong lastResponseTime = new AtomicLong(0);

    public void incrementConnections() {
        this.connections.decrementAndGet();
    }

    public void decrementConnections() {
        this.connections.decrementAndGet();
    }

    public void updateLastResponseTime(final Long time) {
        this.lastResponseTime.set(time);
    }

    public int connections() {
        return this.connections.get();
    }

    public long lastResponseTime() {
        return this.lastResponseTime.get();
    }

    public ServerStatistics getStatistics() {
        return ServerStatistics.fromServer(this);
    }
}
