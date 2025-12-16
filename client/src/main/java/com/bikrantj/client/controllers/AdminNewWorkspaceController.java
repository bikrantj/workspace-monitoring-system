package com.bikrantj.client.controllers;

import com.bikrantj.client.AppContext;
import com.bikrantj.client.api.ApiClient;
import com.bikrantj.client.ui.Toast;
import com.bikrantj.shared.requests.CreateWorkspaceRequest;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AdminNewWorkspaceController {
    public TextField nameField;
    public TextArea descriptionArea;
    public AnchorPane rootPane;
    ApiClient api = AppContext.getApiClient();

    public void onCreateClicked(ActionEvent actionEvent) {
        CreateWorkspaceRequest request = new CreateWorkspaceRequest(
                nameField.getText(),
                descriptionArea.getText()
        );
        try {
            api.createWorkspace(request);
            nameField.clear();
            descriptionArea.clear();
            Toast.success(rootPane, "Workspace Created: " + request.getName() + "!");
        } catch (Exception e) {
            Toast.error(rootPane, "Failed to create workspace. Workspace name already exists");
            throw new RuntimeException(e);
        }

    }

    public void onCancelClicked(ActionEvent actionEvent) {
    }

}
