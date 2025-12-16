package com.bikrantj.client.auth;

import com.bikrantj.shared.model.Client;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ClientPersistence {

    private static final Path CLIENT_FILE = Paths.get(
            System.getProperty("user.home"),
            ".workspace-monitor",
            "client.json"
    );

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ClientPersistence() {
    }

    public static void save(Client client) {
        try {
            Files.createDirectories(CLIENT_FILE.getParent());
            MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValue(CLIENT_FILE.toFile(), client);
        } catch (IOException e) {
            System.err.println("Failed to save client info: " + e.getMessage());
        }
    }

    public static Client load() {
        try {
            if (Files.exists(CLIENT_FILE)) {
                return MAPPER.readValue(CLIENT_FILE.toFile(), Client.class);
            }
        } catch (IOException e) {
            System.err.println("Failed to read client info: " + e.getMessage());
        }
        return null;
    }

    public static void clear() {
        try {
            Files.deleteIfExists(CLIENT_FILE);
        } catch (IOException e) {
            System.err.println("Failed to delete client info");
        }
    }
}
