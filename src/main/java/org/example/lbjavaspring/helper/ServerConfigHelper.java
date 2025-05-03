package org.example.lbjavaspring.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.example.lbjavaspring.data.Server;
import org.example.lbjavaspring.data.ServerInstance;
import org.example.lbjavaspring.exception.ConfigurationException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.noNullElements;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ServerConfigHelper {

    private static final String path = "config/servers.txt";
    public static final String SEPARATOR = ",";

    public static ConcurrentMap<String, ServerInstance> loadServerConfigurations() {
        // Load server configurations from a file or database
        // Let's do it over a file for simplicity
        // File format: server name,server address
        try (var br = Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource(path).toURI()))) {
            return br.lines()
                    .map(line -> line.split(SEPARATOR))
                    .collect(
                            ConcurrentHashMap::new,
                            (map, arr) -> map.put(arr[0].trim(), getServerInstance(arr)),
                            ConcurrentMap::putAll
                    );
        } catch (Exception e) {
            log.error("Error loading server configurations", e);
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    private static ServerInstance getServerInstance(final @NonNull String[] identifyingDetails) {
        isTrue(identifyingDetails.length == 2, "identifyingDetails size is expected to be 2");
        noNullElements(identifyingDetails, "identifyingDetails can't have null elements");

        return ServerInstance.builder()
                .server(Server.builder().name(identifyingDetails[0].trim()).address(identifyingDetails[1].trim()).build())
                .build();
    }
}
