package com.bikrantj.client.controllers;

import com.bikrantj.client.AppContext;
import com.bikrantj.client.api.ApiException;
import com.bikrantj.client.auth.ClientPersistence;
import com.bikrantj.client.auth.ClientSession;
import com.bikrantj.client.clientruntime.runtime.ClientRuntime;
import com.bikrantj.client.navigation.NavigationManager;
import com.bikrantj.client.navigation.Screens;
import com.bikrantj.client.ui.Toast;
import com.bikrantj.client.utils.DeviceInfo;
import com.bikrantj.shared.model.Client;
import com.bikrantj.shared.requests.CreateClientRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;


public class ClientLoginController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField workspaceId;

    @FXML
    private TextField macAddress;

    @FXML
    private TextField deviceName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deviceName.setText(DeviceInfo.getDeviceName());
        workspaceId.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText() != null) {
                change.setText(change.getText().toUpperCase());
            }
            return change;
        }));
    }

    public void onLoginButtonClicked(ActionEvent actionEvent) {
        // Handle client login logic
        CreateClientRequest request = new CreateClientRequest(
                workspaceId.getText(),
                deviceName.getText(),
                DeviceInfo.getMacAddress(),
                DeviceInfo.getOsInfo(),
                DeviceInfo.getIpAddress()
        );
        try {
            Client client = AppContext.getApiClient().clientLogin(request);

            ClientSession.set(client);
            ClientPersistence.save(client);

            System.out.println("Before starting ClientRuntime" + client.getId());

            ClientRuntime.start();
            NavigationManager.navigateTo(Screens.CLIENT_DASHBOARD);

//            TODO: Store client info in a file and ClientSession and try to auto-login next time
            Toast.success(rootPane, "Successfully joined workspace!");
        } catch (ApiException e) {
            System.out.println("[ClientLoginController] Failed to join workspace: " + e.getMessage());
            Toast.error(rootPane, "Failed to join workspace!");

        }
    }

    public void onBackToHomeClicked(MouseEvent mouseEvent) {
        // Handle navigation back to home
        NavigationManager.navigateTo(Screens.INITIAL_VIEW);
    }
}
