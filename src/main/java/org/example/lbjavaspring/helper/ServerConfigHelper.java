package org.example.lbjavaspring.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lbjavaspring.data.Server;
import org.example.lbjavaspring.data.ServerInstance;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ServerConfigHelper {

    private static final String path = "config/servers.txt";

    public static ConcurrentMap<String, ServerInstance> loadServerConfigurations() {
        // Load server configurations from a file or database
        // Let's do it over a file for simplicity
        // File format: serverName,serverAddress
        try (var br = Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource(path).toURI()))) {
            return br.lines()
                    .map(line -> line.split(","))
                    .collect(
                            ConcurrentHashMap::new,
                            (map, arr) -> map.put(arr[0], getServerInstance(arr)),
                            ConcurrentMap::putAll
                    );
        } catch (Exception e) {
            log.error("Error loading server configurations", e);
            throw new RuntimeException(e);
        }
    }

    private static ServerInstance getServerInstance(String[] identifyingDetails) {
        return ServerInstance.builder()
                .serverInfo(Server.builder().name(identifyingDetails[0]).address(identifyingDetails[1]).build())
                .build();
    }
}
