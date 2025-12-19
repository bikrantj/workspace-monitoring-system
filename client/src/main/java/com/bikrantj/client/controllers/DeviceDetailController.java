package com.bikrantj.client.controllers;

import com.bikrantj.client.AppContext;
import com.bikrantj.client.navigation.ContentNavigationManager;
import com.bikrantj.client.navigation.Screens;
import com.bikrantj.shared.model.ProcessInfo;
import com.bikrantj.shared.responses.ActivityPoint;
import com.bikrantj.shared.responses.HighRamProcessUsage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

public class DeviceDetailController {

    @FXML
    private FlowPane clientActivityGraphContainer;
    @FXML
    private TableView<ProcessInfo> processesTable;
    @FXML
    private TableColumn<ProcessInfo, Double> memoryColumn;
    @FXML
    private FlowPane highRamContainer;

    @FXML
    private VBox latestProcessesContainer;

    public void initialize() {
        processesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        try {


            String workspaceId =
                    (String) ContentNavigationManager.getParameter("workspaceId");
            String clientId =
                    (String) ContentNavigationManager.getParameter("clientIdentifier");

            loadLatestProcesses(workspaceId, clientId);

            List<HighRamProcessUsage> data =
                    AppContext.getApiClient()
                            .getHighRamUsageProcesses(workspaceId, clientId);


            for (HighRamProcessUsage p : data) {
                try {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource(
                                    "/com/bikrantj/client/high-ram-process-card.fxml"
                            )
                    );

                    highRamContainer.getChildren().add(loader.load());
                    HighRamProcessCardController controller = loader.getController();
                    controller.setData(p);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            loadClientActivityGraph(workspaceId, clientId);
        } catch (Exception e) {

        }
    }

    private void loadLatestProcesses(String workspaceId, String clientId) {
        try {
            List<ProcessInfo> processes =
                    AppContext.getApiClient().getLatestProcesses(workspaceId, clientId);

            // Fully type-safe; no warnings
            processesTable.getItems().setAll(processes);

            // Clear any existing sort order and apply default sorting
            processesTable.getSortOrder().clear();
            memoryColumn.setSortType(TableColumn.SortType.DESCENDING);
            processesTable.getSortOrder().add(memoryColumn);

            // Optional: trigger initial sort
            processesTable.sort();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadClientActivityGraph(
            String workspaceId,
            String clientId
    ) {
        try {
            List<ActivityPoint> points =
                    AppContext.getApiClient()
                            .getClientActivity(workspaceId, clientId);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Client Activity");

            for (ActivityPoint p : points) {
                series.getData().add(
                        new XYChart.Data<>(p.getTime(), p.getCount())
                );
            }

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/bikrantj/client/graph-card.fxml")
            );

            clientActivityGraphContainer.getChildren().add(loader.load());

            GraphCardController controller = loader.getController();
            controller.setTitle("Most Active Hours");
            controller.setAxisLabels("Time (HH:mm)", "Snapshots Count");
            controller.setSeries(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        // Retrieve the current parameters
        String workspaceId = (String) ContentNavigationManager.getParameter("workspaceId");
        String clientIdentifier = (String) ContentNavigationManager.getParameter("clientIdentifier");

        // If parameters might be missing (unlikely), provide defaults or handle gracefully
        if (workspaceId == null || clientIdentifier == null) {
            // Optional: log or show error; for robustness
            return;
        }

        // Re-navigate to the same screen with the same parameters
        Map<String, Object> params = Map.of(
                "workspaceId", workspaceId,
                "clientIdentifier", clientIdentifier
        );

        ContentNavigationManager.navigateTo(Screens.DEVICE_DETAIL, params);
    }
}
