package com.bikrantj.client;

import com.bikrantj.client.navigation.NavigationManager;
import com.bikrantj.client.navigation.Screens;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AppContext.initialize("http://localhost:8000");
        // Set up the primary stage
        primaryStage.setTitle("Project IV");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);

        // Initialize NavigationManager
        NavigationManager.setPrimaryStage(primaryStage);

        // Navigate to initial view
        NavigationManager.navigateTo(Screens.INITIAL_VIEW);
    }
}