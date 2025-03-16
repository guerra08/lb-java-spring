package org.example.lbjavaspring.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lbjavaspring.core.LoadBalancer;
import org.example.lbjavaspring.data.Request;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lb")
@RequiredArgsConstructor
@Slf4j
public class LbController {

    private final LoadBalancer httpLoadBalancer;

    @GetMapping("/**")
    public ResponseEntity<String> get(final HttpServletRequest request) {
        final String path = request.getServletPath().replace("/lb", "");
        final Request lbRequest = Request.builder()
                .path(path)
                .method(HttpMethod.GET)
                .headers(extractHeaders(request))
                .build();
        log.info("Load balancing Request :: {}", lbRequest);
        return httpLoadBalancer.handle(lbRequest);
    }

    private HttpHeaders extractHeaders(final HttpServletRequest request) {
        final HttpHeaders headers = new HttpHeaders();
        request.getHeaderNames().asIterator().forEachRemaining(header -> headers.add(header, request.getHeader(header)));
        return headers;
    }

}
