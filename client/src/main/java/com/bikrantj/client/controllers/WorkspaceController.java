package com.bikrantj.client.controllers;

import com.bikrantj.client.AppContext;
import com.bikrantj.client.navigation.ContentNavigationManager;
import com.bikrantj.client.navigation.Screens;
import com.bikrantj.shared.responses.ActivityPoint;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WorkspaceController implements Initializable {

    public VBox deviceListContainer;
    @FXML
    private FlowPane graphContainer;

    @FXML
    private Label workspaceLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String workspaceId =
                (String) ContentNavigationManager.getParameter("workspaceId");
        if (workspaceId == null) {
            workspaceLabel.setText("No workspace ID provided");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(Screens.DEVICE_LIST.getFxmlPath())
            );
            deviceListContainer.getChildren().add(loader.load());

            DeviceListController controller = loader.getController();
            controller.loadDevices(workspaceId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        workspaceLabel.setText("Workspace ID: " + workspaceId);

        loadActivityCard(workspaceId);

        // Placeholder cards
//        addDemoCard("Most Used Applications");
//        addDemoCard("Idle vs Active Time");
//        addDemoCard("Screenshot Frequency");
//        addDemoCard("Processes Over Time");
    }

    /**
     * REAL GRAPH
     */
    private void loadActivityCard(String workspaceId) {
        try {
            List<ActivityPoint> points =
                    AppContext.getApiClient().getWorkspaceActivity(workspaceId);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Workspace Activity");

            for (ActivityPoint p : points) {
                series.getData().add(
                        new XYChart.Data<>(p.getTime(), p.getCount())
                );
            }

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bikrantj/client/graph-card.fxml")
            );

            graphContainer.getChildren().add(loader.load());

            GraphCardController controller = loader.getController();
            controller.setTitle("Most Active Hours");
            controller.setSeries(series);
            controller.setAxisLabels("Time (HH:mm)", "Activity Count");

//            Showing tooltip
            for (XYChart.Data<String, Number> data : series.getData()) {
                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        Tooltip.install(
                                newNode,
                                new Tooltip(
                                        "Time: " + data.getXValue() +
                                                "\nActivity: " + data.getYValue()
                                )
                        );
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Failed to load activity card: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * DEMO GRAPH
     */
    private void addDemoCard(String title) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/com/bikrantj/client/graph-card.fxml"));

            graphContainer.getChildren().add(loader.load());

            GraphCardController controller = loader.getController();
            controller.setTitle(title);
            controller.loadDemoData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
