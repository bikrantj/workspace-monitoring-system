package com.bikrantj.client.controllers;

import com.bikrantj.client.AppContext;
import com.bikrantj.client.api.ApiException;
import com.bikrantj.client.navigation.Screens;
import com.bikrantj.shared.model.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;

import java.util.List;

public class DeviceListController {

    @FXML
    private FlowPane deviceContainer;

    public void loadDevices(String workspaceId) {
        System.out.println("Running load devices for workspace ID: " + workspaceId);
        try {

            List<Client> clients =
                    AppContext.getApiClient()
                            .getClientsByWorkspace(workspaceId);

            for (Client client : clients) {
                System.out.println("Clients found: " + client.getClientName());
                try {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource(Screens.DEVICE_CARD.getFxmlPath())
                    );

                    deviceContainer.getChildren().add(loader.load());

                    DeviceCardController controller =
                            loader.getController();

                    controller.setData(client, workspaceId);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (ApiException e) {
            System.err.println("[DeviceListController] Failed to load clients: " + e.getMessage());
        }
    }
}
