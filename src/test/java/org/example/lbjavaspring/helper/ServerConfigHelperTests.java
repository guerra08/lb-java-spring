package org.example.lbjavaspring.helper;

import org.example.lbjavaspring.data.Server;
import org.example.lbjavaspring.data.ServerInstance;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;

class ServerConfigHelperTests {

    @Test
    void testLoadServerConfigurations() {
        ConcurrentMap<String, ServerInstance> serverConfigurations = ServerConfigHelper.loadServerConfigurations();

        assertNotNull(serverConfigurations, "Server configurations should not be null");
        assertFalse(serverConfigurations.isEmpty(), "Server configurations should not be empty");

        // Assuming the file contains at least one known server configuration for testing
        assertTrue(serverConfigurations.containsKey("server1"), "Server configurations should contain 'server1'");
        Server server = serverConfigurations.get("server1").getServer();
        assertEquals("server1", server.name(), "Server name should be 'server1'");
        assertEquals("http://localhost:8081", server.address(), "Server address should be 'http://localhost:8081'");
    }

}