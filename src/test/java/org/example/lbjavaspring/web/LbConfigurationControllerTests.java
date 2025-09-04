package org.example.lbjavaspring.web;

import org.example.lbjavaspring.core.Algorithm;
import org.example.lbjavaspring.data.Server;
import org.example.lbjavaspring.data.ServerInstance;
import org.example.lbjavaspring.store.KeyValueStore;
import org.example.lbjavaspring.store.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LbConfigurationControllerTests {

    private MockMvc mockMvc;

    private final KeyValueStore<ServerInstance> serverStore = mock(KeyValueStore.class);

    private final Store<Algorithm> algorithmStore = mock(Store.class);

    @BeforeEach
    void setup() {
        LbConfigurationController controller = new LbConfigurationController(serverStore, algorithmStore);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAlgorithmReturnsCurrentValue() throws Exception {
        given(algorithmStore.get()).willReturn(Algorithm.RESPONSE_TIME);

        mockMvc.perform(get("/config/algorithm")).andExpect(status().isOk()).andExpect(content().string("RESPONSE_TIME"));
    }

    @Test
    void setAlgorithmAcceptsValidPayload() throws Exception {
        mockMvc.perform(post("/config/algorithm").contentType(MediaType.APPLICATION_JSON).content("{\"algorithm\":\"LEAST_CONNECTIONS\"}")).andExpect(status().isOk());

        verify(algorithmStore, times(1)).set(Algorithm.LEAST_CONNECTIONS);
    }

    @Test
    void setAlgorithmRejectsInvalidPayload() throws Exception {
        mockMvc.perform(post("/config/algorithm").contentType(MediaType.APPLICATION_JSON).content("{\"algorithm\":\"NOT_AN_ALGO\"}")).andExpect(status().isBadRequest());

        verify(algorithmStore, never()).set(any());
    }

    @Test
    void addServerStoresServerInstance() throws Exception {
        mockMvc.perform(post("/config/server").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"api-1\",\"address\":\"http://localhost:7000\"}")).andExpect(status().isOk());

        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ServerInstance> instanceCaptor = ArgumentCaptor.forClass(ServerInstance.class);
        verify(serverStore).put(nameCaptor.capture(), instanceCaptor.capture());
        ServerInstance saved = instanceCaptor.getValue();

        assertEquals("api-1", nameCaptor.getValue());
        assertEquals("api-1", saved.getServer().name());
        assertEquals("http://localhost:7000", saved.getServer().address());
    }

    @Test
    void serverStatisticsReturnsDerivedValues() throws Exception {
        ServerInstance instance = ServerInstance.builder().server(Server.builder().name("api-2").address("http://localhost:7001").build()).build();
        given(serverStore.getAll()).willReturn(List.of(instance));

        mockMvc.perform(get("/config/server/statistics")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].name", is("api-2"))).andExpect(jsonPath("$[0].address", is("http://localhost:7001"))).andExpect(jsonPath("$[0].connections", is(0))).andExpect(jsonPath("$[0].lastResponseTime", is(0))).andExpect(jsonPath("$[0].healthy", is(true)));
    }
}
