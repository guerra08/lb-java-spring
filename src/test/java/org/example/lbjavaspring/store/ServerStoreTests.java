package org.example.lbjavaspring.store;

import org.example.lbjavaspring.data.Server;
import org.example.lbjavaspring.data.ServerInstance;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class ServerStoreTests {

    @Test
    void loadsInitialServersAndSupportsPutRemove() {
        ServerStore store = new ServerStore();

        Collection<ServerInstance> initial = store.getAll();
        assertNotNull(initial);
        assertFalse(initial.isEmpty(), "ServerStore should load initial servers from configuration");

        String name = "test-server";
        ServerInstance instance = ServerInstance.builder().server(Server.builder().name(name).address("http://localhost:9999").build()).build();

        store.put(name, instance);
        assertTrue(store.getAll().stream().anyMatch(s -> name.equals(s.getServer().name())));

        store.remove(name);
        assertFalse(store.getAll().stream().anyMatch(s -> name.equals(s.getServer().name())));
    }
}
