package com.bikrantj.client.auth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TokenManager {
    private static final Path TOKEN_FILE = Paths.get(
            System.getProperty("user.home"), ".workspace-monitor", "auth.token"
    );

    public static void saveToken(String token) {
        try {
            Files.createDirectories(TOKEN_FILE.getParent());
            Files.writeString(TOKEN_FILE, token.strip());
        } catch (IOException e) {
            System.err.println("Failed to save token: " + e.getMessage());
        }
    }

    public static String getToken() {
        try {
            if (Files.exists(TOKEN_FILE)) {
                return Files.readString(TOKEN_FILE).strip();
            }
        } catch (IOException e) {
            System.err.println("Failed to read token: " + e.getMessage());
        }
        return null;
    }

    public static void clearToken() {
        try {
            Files.deleteIfExists(TOKEN_FILE);
        } catch (IOException e) {
            System.err.println("Failed to delete token");
        }
    }
}