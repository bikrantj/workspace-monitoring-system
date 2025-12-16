package com.bikrantj.client;

import com.bikrantj.client.api.ApiException;
import com.bikrantj.client.auth.TokenManager;
import com.bikrantj.client.auth.UserSession;
import com.bikrantj.client.navigation.NavigationManager;
import com.bikrantj.client.navigation.Screens;
import com.bikrantj.shared.dto.User;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ClientApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AppContext.initialize("http://localhost:8000");
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        // Set up the primary stage
        primaryStage.setTitle("Project IV");
        // Create an empty root container
        StackPane appRoot = new StackPane();
        Scene scene = new Scene(appRoot);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();


        // Initialize NavigationManager
        NavigationManager.initialize(appRoot);
        String savedToken = TokenManager.getToken();
        if (savedToken != null && !savedToken.isBlank()) {
            try {
                User profile = AppContext.getApiClient().getCurrentUser();
                if (profile != null) {
                    UserSession.setUser(profile, savedToken);
//                TODO: Show different screen
                    NavigationManager.navigateTo(Screens.DASHBOARD);

                } else {
                    throw new ApiException("Invalid token");
                }
            } catch (ApiException e) {
                // Navigate to initial view
                System.out.println("Not logged in");
                NavigationManager.navigateTo(Screens.INITIAL_VIEW);
            }
        } else {
            NavigationManager.navigateTo(Screens.INITIAL_VIEW);
        }


    }
}