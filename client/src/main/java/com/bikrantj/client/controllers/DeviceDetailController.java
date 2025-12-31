package com.bikrantj.client.controllers;

import com.bikrantj.client.AppContext;
import com.bikrantj.client.navigation.ContentNavigationManager;
import com.bikrantj.client.navigation.Screens;
import com.bikrantj.shared.model.ProcessInfo;
import com.bikrantj.shared.model.Screenshot;
import com.bikrantj.shared.responses.ActivityPoint;
import com.bikrantj.shared.responses.HighRamProcessUsage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class DeviceDetailController {

    public Label screenshotTimestampLabel;
    @FXML
    private ImageView latestScreenshotView;
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
            loadLatestSceeenshot(workspaceId, clientId);
            latestScreenshotView.setOnMouseClicked(event -> {
                if (latestScreenshotView.getImage() != null) {
                    showImagePreview(latestScreenshotView.getImage());
                }
            });
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
            System.out.println("[DeviceDetailController] Initialization error: " + e.getMessage());

        }
    }

    private void showImagePreview(Image image) {

        // Create ImageView for preview
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        // Wrap in StackPane so it centers properly
        StackPane root = new StackPane(imageView);
        root.setStyle("-fx-background-color: black;");

        // Create Scene
        Scene scene = new Scene(root);

        // Bind image size to window size
        imageView.fitWidthProperty().bind(scene.widthProperty());
        imageView.fitHeightProperty().bind(scene.heightProperty());

        // Create new Stage
        Stage stage = new Stage();
        stage.setTitle("Screenshot Preview");
        stage.setScene(scene);

        // Make it modal (blocks background window)
        stage.initModality(Modality.APPLICATION_MODAL);

        // Start maximized
        stage.setMaximized(true);

        // Close on ESC
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });

        stage.show();
    }

    private void loadLatestSceeenshot(String workspaceId, String clientId) {
        try {
            Screenshot screenshot =
                    AppContext.getApiClient()
                            .getLatestSceenshot(workspaceId, clientId);

            if (screenshot == null || screenshot.getFilePath() == null) {
                return;
            }
            screenshotTimestampLabel.setText(screenshot.getCaptureTime());

            String imageUrl = "http://localhost" + screenshot.getFilePath();
            System.out.println("Loading screenshot from URL: " + imageUrl);
            Image image = new Image(imageUrl, true);
            latestScreenshotView.setImage(image);

        } catch (Exception e) {
            e.printStackTrace();
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
