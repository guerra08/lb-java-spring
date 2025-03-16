package org.example.lbjavaspring.data;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Builder
@Getter
public class ServerInstance {

    private Server serverInfo;
    private boolean healthy;
    @Default
    private AtomicInteger connections = new AtomicInteger(0);
    @Default
    private AtomicLong lastResponseTime = new AtomicLong(0);

    public String getName() {
        return this.serverInfo.name();
    }

    public String getAddress() {
        return this.serverInfo.address();
    }

    public int incrementConnections() {
        return this.connections.incrementAndGet();
    }

    public int decrementConnections() {
        return this.connections.decrementAndGet();
    }

    public void updateLastResponseTime(final Long time) {
        this.lastResponseTime.set(time);
    }

    @Override
    public String toString() {
        return "ServerInstance{" +
                "serverInfo=" + serverInfo +
                ", healthy=" + healthy +
                ", connections=" + connections +
                ", lastResponseTime=" + lastResponseTime +
                '}';
    }
}
