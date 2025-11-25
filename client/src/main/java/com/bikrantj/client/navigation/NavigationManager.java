package com.bikrantj.client.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NavigationManager {
    private static final Map<String, Object> parameters = new HashMap<>();
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void navigateTo(Screens screen) {
        navigateTo(screen, null);
    }

    public static void navigateTo(Screens screen, Map<String, Object> params) {
        try {
            // Clear previous parameters
            parameters.clear();

            // Add new parameters if provided
            if (params != null) {
                parameters.putAll(params);
            }

            // Load the FXML
            FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource(screen.getFxmlPath()));
            Parent root = loader.load();

            // Set the scene
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load screen: " + screen, e);
        }
    }

    public static Map<String, Object> getParameters() {
        return new HashMap<>(parameters);
    }

    public static <T> T getParameter(String key) {
        return (T) parameters.get(key);
    }

    public static <T> T getParameter(String key, T defaultValue) {
        return parameters.containsKey(key) ? (T) parameters.get(key) : defaultValue;
    }
}