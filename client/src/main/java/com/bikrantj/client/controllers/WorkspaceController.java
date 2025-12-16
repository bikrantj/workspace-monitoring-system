package com.bikrantj.client.controllers;

import com.bikrantj.client.navigation.ContentNavigationManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class WorkspaceController implements Initializable {

    @FXML
    private Label workspaceLabel;

    private String workspaceId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // intentionally empty

        String workspaceId =
                (String) ContentNavigationManager.getParameter("workspaceId");

        workspaceLabel.setText("Workspace ID: " + workspaceId);

        if (workspaceId != null) {
            render(workspaceId);
        } else {
            workspaceLabel.setText("No workspace ID provided");
        }
    }


    private void render(String workspaceId) {
        workspaceLabel.setText("Workspace ID: " + workspaceId);
    }
}
