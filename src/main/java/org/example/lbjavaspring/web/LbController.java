package org.example.lbjavaspring.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lbjavaspring.core.LoadBalancer;
import org.example.lbjavaspring.data.Request;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/lb")
@RequiredArgsConstructor
@Slf4j
public class LbController {

    private final LoadBalancer<Request, ResponseEntity<String>> httpLoadBalancer;

    @RequestMapping(path = "/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.HEAD})
    public ResponseEntity<String> proxy(final HttpServletRequest request) throws IOException {
        final String pathOnly = request.getServletPath().replace("/lb", "");
        final String query = request.getQueryString();
        final String path = query == null ? pathOnly : pathOnly + "?" + query;
        final HttpMethod method = HttpMethod.valueOf(request.getMethod());
        final byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
        final Request lbRequest = Request.builder().path(path).method(method).headers(extractHeaders(request)).body(body).build();
        log.info("Load balancing {} request :: {}", lbRequest.method(), lbRequest.path());
        try {
            return httpLoadBalancer.handle(lbRequest);
        } catch (IllegalStateException ex) {
            log.warn("Unable to handle request due to load balancer state: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service Unavailable: " + ex.getMessage());
        }
    }

    private HttpHeaders extractHeaders(final HttpServletRequest request) {
        final HttpHeaders headers = new HttpHeaders();
        request.getHeaderNames().asIterator().forEachRemaining(header -> headers.add(header, request.getHeader(header)));
        return headers;
    }


}
