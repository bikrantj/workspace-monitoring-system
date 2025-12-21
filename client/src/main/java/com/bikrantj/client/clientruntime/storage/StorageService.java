package com.bikrantj.client.clientruntime.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;


public class StorageService {
    private static final Path BASE_DIR = Paths.get(
            "C:", "xampp", "htdocs", "screenshots"
    );

    public static String saveScreenshot(String workspaceId, String clientId, Instant timestamp, byte[] imageData) throws IOException {
        Path dir = BASE_DIR.resolve((Paths.get(workspaceId, clientId)));

        Files.createDirectories(dir);

        String fileName = timestamp.toEpochMilli() + ".png";
        Path filePath = dir.resolve(fileName);

        Files.write(filePath, imageData);

        String absolutePath = "/screenshots/"
                + workspaceId + "/"
                + clientId + "/"
                + fileName;
        System.out.println("Screenshot saved at" + "http://localhost" + absolutePath);


        return absolutePath;
    }


}
