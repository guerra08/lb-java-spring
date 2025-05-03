package org.example.lbjavaspring.store;

import lombok.extern.slf4j.Slf4j;
import org.example.lbjavaspring.data.ServerInstance;
import org.example.lbjavaspring.helper.ServerConfigHelper;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

@Configuration
@Slf4j
public class ServerStore implements KeyValueStore<ServerInstance> {

    private final ConcurrentMap<String, ServerInstance> servers;

    public ServerStore() {
        this.servers = ServerConfigHelper.loadServerConfigurations();
    }

    @Override
    public Collection<ServerInstance> getAll() {
        return this.servers.values();
    }

    @Override
    public Set<Map.Entry<String, ServerInstance>> getAllEntries() {
        return this.servers.entrySet();
    }

    @Override
    public void put(String key, ServerInstance value) {
        this.servers.put(key, value);
    }

    @Override
    public void remove(String key) {
        this.servers.remove(key);
    }

}
