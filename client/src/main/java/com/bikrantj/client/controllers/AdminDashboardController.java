package com.bikrantj.client.controllers;

import com.bikrantj.client.AppContext;
import com.bikrantj.client.api.ApiException;
import com.bikrantj.client.navigation.ContentNavigationManager;
import com.bikrantj.client.navigation.Screens;
import com.bikrantj.shared.model.Workspace;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class AdminDashboardController implements Initializable {
    public AnchorPane mainBorderPane;
    public FlowPane workspaceContainer;
    private List<Workspace> workspaces;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Loading Admin Dashboard...");
        workspaces = new ArrayList<>();
        try {
            workspaces = AppContext.getApiClient().getWorkspace();

            renderWorkspace();
        } catch (ApiException e) {
            System.out.println("[AdminDashboardController] Failed to load workspaces: " + e.getMessage());
//            throw new RuntimeException(e);
        }
        // Initialization logic if needed
    }

    private void renderWorkspace() {
        workspaceContainer.getChildren().clear();
        for (Workspace ws : workspaces) {
            System.out.println("Rendering Workspace: " + ws.getName() + " (ID: " + ws.getUniqueId() + ")");
            workspaceContainer.getChildren().add(createWorkspaceCard(ws));
            // Here you would create UI components for each workspace and add them to workspaceContainer
        }
    }

    private VBox createWorkspaceCard(Workspace workspace) {
        VBox card = new VBox(10);
        card.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 12;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);
                -fx-padding: 20;
                  -fx-cursor: hand;
                """);

        Label nameLabel = new Label(workspace.getName());
        nameLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        Label descLabel = new Label(workspace.getDescription());
        descLabel.setStyle("-fx-text-fill: #718096;");


        card.getChildren().addAll(nameLabel, descLabel);

        card.setOnMouseClicked(event -> openWorkspace(workspace.getId()));

        return card;
    }

    private void openWorkspace(String workspaceId) {
        Map<String, Object> params = new HashMap<>();

        params.put("workspaceId", workspaceId);
        ContentNavigationManager.navigateTo(Screens.WORKSPACE, params);
    }

}
