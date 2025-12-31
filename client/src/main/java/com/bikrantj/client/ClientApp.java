package com.bikrantj.client;

import com.bikrantj.client.api.ApiException;
import com.bikrantj.client.auth.ClientPersistence;
import com.bikrantj.client.auth.ClientSession;
import com.bikrantj.client.auth.TokenManager;
import com.bikrantj.client.auth.UserSession;
import com.bikrantj.client.clientruntime.runtime.ClientRuntime;
import com.bikrantj.client.navigation.NavigationManager;
import com.bikrantj.client.navigation.Screens;
import com.bikrantj.shared.dto.User;
import com.bikrantj.shared.model.Client;
import com.bikrantj.shared.requests.CreateClientRequest;
import com.bikrantj.shared.utils.Constants;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ClientApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        AppContext.initialize(Constants.API_BASE_URL);

        primaryStage.setTitle("Project IV");

        StackPane appRoot = new StackPane();
        Scene scene = new Scene(appRoot);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        NavigationManager.initialize(appRoot);

        if (tryRestoreClientSession()) {
            return;
        }

        if (tryRestoreAdminSession()) {
            return;
        }

        NavigationManager.navigateTo(Screens.INITIAL_VIEW);
    }

    private boolean tryRestoreAdminSession() {

        String savedToken = TokenManager.getToken();
        if (savedToken == null || savedToken.isBlank()) {
            return false;
        }

        try {
            User profile = AppContext.getApiClient().getCurrentUser();
            if (profile == null) {
                throw new ApiException("Invalid token", "Invalid Token");
            }

            UserSession.setUser(profile, savedToken);
            NavigationManager.navigateTo(Screens.DASHBOARD);
            return true;

        } catch (ApiException e) {
            System.out.println("Admin auto-login failed");
            TokenManager.clearToken();
            return false;
        }
    }


    private boolean tryRestoreClientSession() {

        Client savedClient = ClientPersistence.load();
        if (savedClient == null) {
            System.out.println("No saved client session found");
            return false;
        }

        try {
            CreateClientRequest request = new CreateClientRequest(
                    savedClient.getWorkspaceId(),
                    savedClient.getClientName(),
                    savedClient.getClientIdentifier(),
                    savedClient.getOsInfo(),
                    savedClient.getLastIpAddress()
            );

            Client loggedInClient =
                    AppContext.getApiClient().clientLogin(request);

            ClientSession.set(loggedInClient);
            System.out.println("Restored client session for: " + loggedInClient.getClientName());
            ClientRuntime.start();

            NavigationManager.navigateTo(Screens.CLIENT_DASHBOARD);
            return true;

        } catch (Exception e) {

            System.out.println("Failed to restore client session: " + e.getMessage());
            ClientPersistence.clear();
            return false;
        }
    }


}