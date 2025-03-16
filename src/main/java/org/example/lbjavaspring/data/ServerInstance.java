package org.example.lbjavaspring.data;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Builder
@Getter
@ToString
public class ServerInstance {

    private Server serverInfo;
    private boolean healthy;
    @Default
    private AtomicInteger connections = new AtomicInteger(0);
    @Default
    private AtomicLong lastResponseTime = new AtomicLong(0);

    public int incrementConnections() {
        return this.connections.incrementAndGet();
    }

    public int decrementConnections() {
        return this.connections.decrementAndGet();
    }

    public void updateLastResponseTime(final Long time) {
        this.lastResponseTime.set(time);
    }
}
