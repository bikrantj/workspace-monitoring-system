package com.bikrantj.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Objects;

public class AdminMainController {
    @FXML
    private BorderPane mainBorderPane;

    public void onCreateWorkspaceClicked(ActionEvent actionEvent) throws IOException {
        Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/bikrantj/client/admin-new-workspace.fxml")));
        mainBorderPane.setCenter(view);
    }

    public void onDashboardClicked(ActionEvent actionEvent) {
    }
}
