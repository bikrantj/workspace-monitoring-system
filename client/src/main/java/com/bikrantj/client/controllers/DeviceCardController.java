package com.bikrantj.client.controllers;

import com.bikrantj.client.navigation.ContentNavigationManager;
import com.bikrantj.client.navigation.Screens;
import com.bikrantj.shared.model.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class DeviceCardController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label osLabel;

    @FXML
    private Label statusLabel;
    @FXML
    private VBox root;

    private Client client;
    private String workspaceId;

    public void setData(Client client, String workspaceId) {
        this.client = client;
        this.workspaceId = workspaceId;

        nameLabel.setText(client.getClientName());
        osLabel.setText(client.getOsInfo());
        System.out.println("Client Status: " + client.getStatus());
        statusLabel.setText(client.getStatus().name());

        Map<String, Object> params = new HashMap<>();

        params.put("workspaceId", workspaceId);
        params.put("clientIdentifier", client.getId());

        root.setOnMouseClicked(e -> {
            ContentNavigationManager.navigateTo(
                    Screens.DEVICE_DETAIL,
                    params
            );
        });
    }

}
