package com.bikrantj.client.controllers;

import com.bikrantj.client.AppContext;
import com.bikrantj.client.api.ApiClient;
import com.bikrantj.shared.requests.DeleteClientRequest;
import com.bikrantj.shared.responses.ClientListView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DeviceManagementController implements Initializable {

    @FXML
    private FlowPane deviceContainer;

    private ApiClient apiClient;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // however you already inject / access ApiClient
        this.apiClient = AppContext.getApiClient();

        loadDevices();
    }

    private void loadDevices() {

        deviceContainer.getChildren().clear();

        new Thread(() -> {
            try {
                List<ClientListView> devices = apiClient.getAllAdminDevices();
                Platform.runLater(() -> renderDevices(devices));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void renderDevices(List<ClientListView> devices) {

        for (ClientListView device : devices) {
            deviceContainer.getChildren().add(createDeviceCard(device));
        }
    }

    private VBox createDeviceCard(ClientListView device) {

        VBox card = new VBox(10);
        card.setPrefWidth(280);
        card.setStyle("""
                    -fx-background-color: white;
                    -fx-background-radius: 14;
                    -fx-border-radius: 14;
                    -fx-border-color: #e0e6ed;
                    -fx-border-width: 1;
                    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 16, 0, 0, 4);
                """);

        card.setPadding(new javafx.geometry.Insets(16));

        /* ---------- Top Row (Status + Delete) ---------- */
        HBox topRow = new HBox();
        topRow.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label statusBadge = statusBadge(device.getStatus());

        Button deleteBtn = new Button("X");
        deleteBtn.setStyle("""
                
                    -fx-text-fill: #e53e3e;
                    -fx-font-size: 14;
                    -fx-cursor: hand;
                """);

        deleteBtn.setOnAction(e -> confirmDelete(device));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topRow.getChildren().addAll(statusBadge, spacer, deleteBtn);

        /* ---------- Device Name ---------- */
        Label deviceName = new Label(device.getClientName());
        deviceName.setStyle("""
                    -fx-font-size: 18;
                    -fx-font-weight: bold;
                    -fx-text-fill: #2d3748;
                """);

        /* ---------- Info ---------- */
        Label workspace = info("Workspace", device.getWorkspaceName());
        Label ip = info("IP", device.getIpAddress());
        Label os = info("OS", device.getOsInfo());

        String lastSeenText =
                (device.getLastHeartbeat() == null || device.getLastHeartbeat().isBlank())
                        ? "—"
                        : device.getLastHeartbeat();

        Label lastSeen = info("Last Seen", lastSeenText);

        card.getChildren().addAll(
                topRow,
                deviceName,
                workspace,
                ip,
                os,
                lastSeen
        );

        return card;
    }


    private Label info(String key, String value) {
        Label label = new Label(key + ": " + value);
        label.setStyle("""
                    -fx-text-fill: #4a5568;
                    -fx-font-size: 13;
                """);
        return label;
    }

    private void confirmDelete(ClientListView device) {

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Remove device \"" + device.getClientName() + "\"?"
        );

        alert.showAndWait().ifPresent(btn -> {
            if (btn == javafx.scene.control.ButtonType.OK) {
                deleteDevice(device);
            }
        });
    }

    private void deleteDevice(ClientListView device) {

        new Thread(() -> {
            try {
                DeleteClientRequest request = new DeleteClientRequest();
                request.setClientId(device.getClientId());
                apiClient.deleteAdminDevice(request);
                Platform.runLater(this::loadDevices);
            } catch (Exception e) {
                System.out.println("Failed to delete device: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private Label statusBadge(String status) {

        Label badge = new Label(status.toUpperCase());
        badge.setStyle("""
                    -fx-padding: 4 10 4 10;
                    -fx-font-size: 11;
                    -fx-font-weight: bold;
                    -fx-background-radius: 20;
                    -fx-text-fill: white;
                """);

        if ("online".equalsIgnoreCase(status)) {
            badge.setStyle(badge.getStyle() + """
                        -fx-background-color: #38a169;
                    """);
        } else {
            badge.setStyle(badge.getStyle() + """
                        -fx-background-color: #e53e3e;
                    """);
        }

        return badge;
    }

}
