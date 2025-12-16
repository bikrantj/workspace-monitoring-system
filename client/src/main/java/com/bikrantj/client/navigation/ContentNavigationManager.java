package com.bikrantj.client.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ContentNavigationManager {

    private static final Map<String, Object> parameters = new HashMap<>();
    private static BorderPane mainBorderPane;

    public static void initialize(BorderPane borderPane) {
        mainBorderPane = borderPane;
    }

    public static void navigateTo(Screens screen, Map<String, Object> params) {
        try {
            parameters.clear();
            if (params != null) {
                parameters.putAll(params);
            }

            Parent view = FXMLLoader.load(
                    Objects.requireNonNull(ContentNavigationManager.class.getResource(screen.getFxmlPath()))
            );

            mainBorderPane.setCenter(view);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load content: " + screen.getFxmlPath(), e);
        }
    }

    public static Object getParameter(String key) {
        return parameters.get(key);
    }
}
