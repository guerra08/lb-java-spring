package org.example.lbjavaspring.core;

import org.example.lbjavaspring.core.helper.HealthCheckHelper;
import org.example.lbjavaspring.data.Server;
import org.example.lbjavaspring.data.ServerInstance;
import org.example.lbjavaspring.store.ServerStore;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class HealthCheckTests {

    @Test
    void removesUnhealthyServers() {
        ServerStore serverStore = Mockito.mock(ServerStore.class);
        HealthCheckHelper helper = Mockito.mock(HealthCheckHelper.class);
        HealthCheck healthCheck = new HealthCheck(serverStore, helper);

        ServerInstance s1 = ServerInstance.builder().server(Server.builder().name("s1").address("http://localhost:8081").build()).build();

        when(serverStore.getAll()).thenReturn(List.of(s1));
        when(serverStore.getAllEntries()).thenReturn(Set.of(Map.entry("s1", s1)));
        when(helper.isServerHealthy(anyString())).thenReturn(false);

        healthCheck.healthCheck();

        verify(serverStore, times(1)).remove("s1");
    }

    @Test
    void keepsHealthyServers() {
        ServerStore serverStore = Mockito.mock(ServerStore.class);
        HealthCheckHelper helper = Mockito.mock(HealthCheckHelper.class);
        HealthCheck healthCheck = new HealthCheck(serverStore, helper);

        ServerInstance s1 = ServerInstance.builder().server(Server.builder().name("s1").address("http://localhost:8081").build()).build();

        when(serverStore.getAll()).thenReturn(List.of(s1));
        when(serverStore.getAllEntries()).thenReturn(Set.of(Map.entry("s1", s1)));
        when(helper.isServerHealthy(anyString())).thenReturn(true);

        healthCheck.healthCheck();

        verify(serverStore, never()).remove(anyString());
    }
}
