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
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;

public class ClientApp extends Application {
    private Stage primaryStage;
    private TrayIcon trayIcon;
    private boolean isMinimizedToTray = false;

    public static void main(String[] args) {
        launch(args);
    }

    private void setupSystemTray() {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray is not supported on this platform.");
            return;
        }
        System.out.println("System tray initialization started");

        // Create popup menu for the tray icon
        PopupMenu popup = getPopupMenu();

        // Tray icon image (replace with your own icon if desired)
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png"));
        // If you don't have an icon yet, you can use:
        // Image image = Toolkit.getDefaultToolkit().createImage("https://via.placeholder.com/32");

        trayIcon = new TrayIcon(image, "Workspace Monitoring System", popup);
        trayIcon.setImageAutoSize(true);

        // Add double-click listener to restore window ONLY when minimized to tray
        trayIcon.addActionListener(e -> {
            if (isMinimizedToTray) {
                Platform.runLater(this::restoreWindow);
            }
        });

        try {
            SystemTray.getSystemTray().add(trayIcon);
            updateTrayTooltip();
        } catch (AWTException e) {
            System.err.println("Failed to add tray icon: " + e.getMessage());
        }
    }

    private void restoreWindow() {
        if (primaryStage == null || !isMinimizedToTray) return;

        Platform.runLater(() -> {
            primaryStage.show();
            primaryStage.setIconified(false);
            primaryStage.toFront();
            primaryStage.requestFocus();

            isMinimizedToTray = false;
            updateTrayTooltip();
        });
    }

    private void updateTrayTooltip() {
        if (trayIcon != null) {
            if (isMinimizedToTray) {
                trayIcon.setToolTip("Workspace Monitoring System - Running in background");
            } else {
                trayIcon.setToolTip("Workspace Monitoring System - Application is open");
            }
        }
    }

    private PopupMenu getPopupMenu() {
        PopupMenu popup = new PopupMenu();

        // Restore item - only enable when minimized to tray
        MenuItem restoreItem = new MenuItem("Restore");
        restoreItem.addActionListener(e -> Platform.runLater(this::restoreWindow));
        popup.add(restoreItem);

        popup.addSeparator();

        // Exit item
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> {
            Platform.runLater(() -> {
                removeTrayIcon();
                // Properly close the JavaFX application
                if (primaryStage != null) {
                    primaryStage.close();
                }
                Platform.exit();
                System.exit(0);
            });
        });
        popup.add(exitItem);

        return popup;
    }

    private void minimizeToTray() {
        if (primaryStage == null) return;

        isMinimizedToTray = true;
        primaryStage.hide();
        updateTrayTooltip();

        // Show notification
        if (trayIcon != null) {
            trayIcon.displayMessage(
                    "Workspace Monitoring",
                    "Application is running in background",
                    TrayIcon.MessageType.INFO
            );
        }
    }

    private void removeTrayIcon() {
        if (SystemTray.isSupported() && trayIcon != null) {
            SystemTray tray = SystemTray.getSystemTray();
            tray.remove(trayIcon);
            trayIcon = null;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(false);
        this.primaryStage = primaryStage;
        AppContext.initialize(Constants.API_BASE_URL);

        primaryStage.setTitle("Project IV");

        StackPane appRoot = new StackPane();
        Scene scene = new Scene(appRoot);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);

        // Handle window close request
        primaryStage.setOnCloseRequest(event -> {
            // Don't actually close, just minimize to tray
            event.consume();
            minimizeToTray();
        });

        // Handle window iconified event (minimize button)
        primaryStage.iconifiedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // User clicked minimize button - minimize to tray instead
                Platform.runLater(() -> {
                    primaryStage.setIconified(false);
                    minimizeToTray();
                });
            }
        });

        // Initialize navigation
        NavigationManager.initialize(appRoot);


        // Show the window
        primaryStage.show();

        // Setup system tray
        setupSystemTray();

        // Restore sessions
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

            Client loggedInClient = AppContext.getApiClient().clientLogin(request);
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

    @Override
    public void stop() {
        // Clean up when application stops
        removeTrayIcon();
    }
}