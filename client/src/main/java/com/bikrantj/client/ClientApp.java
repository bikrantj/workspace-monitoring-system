package com.bikrantj.client;

import com.bikrantj.client.api.ApiException;
import com.bikrantj.client.auth.TokenManager;
import com.bikrantj.client.auth.UserSession;
import com.bikrantj.client.navigation.NavigationManager;
import com.bikrantj.client.navigation.Screens;
import com.bikrantj.shared.dto.User;
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
        String savedToken = TokenManager.getToken();
        if (savedToken != null && !savedToken.isBlank()) {
            try {
                User profile = AppContext.getApiClient().getCurrentUser();
                UserSession.setUser(profile, savedToken);
//                TODO: Show different screen
                NavigationManager.navigateTo(Screens.ADMIN_LOGIN);
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