package com.bikrantj.client.controllers;

import com.bikrantj.client.navigation.NavigationManager;
import com.bikrantj.client.navigation.Screens;
import com.bikrantj.client.utils.DeviceInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
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
        macAddress.setText(DeviceInfo.getMacAddress());
    }

    public void onLoginButtonClicked(ActionEvent actionEvent) {
        // Handle client login logic
    }

    public void onBackToHomeClicked(MouseEvent mouseEvent) {
        // Handle navigation back to home
        NavigationManager.navigateTo(Screens.INITIAL_VIEW);
    }
}
