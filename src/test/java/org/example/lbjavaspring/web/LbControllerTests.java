package org.example.lbjavaspring.web;

import org.example.lbjavaspring.core.LoadBalancer;
import org.example.lbjavaspring.data.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LbControllerTests {

    private MockMvc mockMvc;
    private final LoadBalancer<String> loadBalancer = mock(LoadBalancer.class);

    @BeforeEach
    void setup() {
        LbController controller = new LbController(loadBalancer);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void proxiesRequestAndReturnsDelegateResponse() throws Exception {
        when(loadBalancer.handle(any(Request.class))).thenReturn(ResponseEntity.ok("hello"));

        mockMvc.perform(get("/lb/api/hello").queryParam("q", "1")).andExpect(status().isOk()).andExpect(content().string("hello"));

        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(loadBalancer, times(1)).handle(captor.capture());
    }

    @Test
    void returns503WhenLoadBalancerUnavailable() throws Exception {
        when(loadBalancer.handle(any(Request.class))).thenThrow(new IllegalStateException("no servers"));

        mockMvc.perform(get("/lb/down")).andExpect(status().isServiceUnavailable()).andExpect(content().string(org.hamcrest.Matchers.containsString("Service Unavailable")));
    }
}
