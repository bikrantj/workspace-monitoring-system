package com.bikrantj.client.controllers;

import com.bikrantj.client.AppContext;
import com.bikrantj.client.api.ApiClient;
import com.bikrantj.client.ui.Toast;
import com.bikrantj.shared.requests.CreateWorkspaceRequest;
import com.bikrantj.shared.utils.IdGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminNewWorkspaceController implements Initializable {
    public TextField nameField;
    public TextArea descriptionArea;
    public AnchorPane rootPane;
    public TextField workspaceIdField;
    ApiClient api = AppContext.getApiClient();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String workspaceId = IdGenerator.generateWorkspaceId();
        workspaceIdField.setText(workspaceId);
    }

    public void onCreateClicked(ActionEvent actionEvent) {
        CreateWorkspaceRequest request = new CreateWorkspaceRequest(
                nameField.getText(),
                descriptionArea.getText(),
                workspaceIdField.getText()
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
