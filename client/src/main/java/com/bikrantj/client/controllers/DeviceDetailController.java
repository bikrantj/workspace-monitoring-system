package com.bikrantj.client.controllers;

import com.bikrantj.client.AppContext;
import com.bikrantj.client.navigation.ContentNavigationManager;
import com.bikrantj.shared.responses.HighRamProcessUsage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;

import java.util.List;

public class DeviceDetailController {

    @FXML
    private FlowPane highRamContainer;

    public void initialize() {
        try {


            String workspaceId =
                    (String) ContentNavigationManager.getParameter("workspaceId");
            String clientId =
                    (String) ContentNavigationManager.getParameter("clientIdentifier");

            List<HighRamProcessUsage> data =
                    AppContext.getApiClient()
                            .getHighRamUsageProcesses(workspaceId, clientId);

            System.out.println("High RAM Usage Processes for clientId: " + clientId + " in workspaceId: " + workspaceId);
            for (HighRamProcessUsage process : data) {
                System.out.println("Process Name: " + process.getProcessName() +
                        ", PID: " + process.getProcessName() +
                        ", RAM Usage (MB): " + process.getAvgMemoryUsage());
            }

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
        } catch (Exception e) {

        }
    }
}
