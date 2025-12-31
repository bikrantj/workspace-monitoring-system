package com.bikrantj.client.controllers;

import com.bikrantj.client.auth.UserSession;
import com.bikrantj.client.navigation.ContentNavigationManager;
import com.bikrantj.client.navigation.Screens;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMainController implements Initializable {
    @FXML
    private BorderPane mainBorderPane;

    public void onCreateWorkspaceClicked(ActionEvent actionEvent) throws IOException {
//        loadView("/com/bikrantj/client/admin-create-workspace.fxml");
        ContentNavigationManager.navigateTo(Screens.NEW_WORKSPACE, null);
    }

    public void onDashboardClicked(ActionEvent actionEvent) throws IOException {
        ContentNavigationManager.navigateTo(Screens.DASHBOARD_CONTENT, null);
    }

    public void onLogoutClicked(MouseEvent mouseEvent) {
        UserSession.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ContentNavigationManager.initialize(mainBorderPane);
    }

    public void onDeviceManagementClicked(ActionEvent actionEvent) {
        ContentNavigationManager.navigateTo(Screens.DEVICE_MANAGEMENT, null);
    }
}
