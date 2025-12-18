package com.bikrantj.client.controllers;

import com.bikrantj.shared.responses.HighRamProcessUsage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HighRamProcessCardController {

    @FXML
    private Label processNameLabel;
    @FXML
    private Label ramLabel;
    @FXML
    private Label cpuLabel;

    public void setData(HighRamProcessUsage data) {

        processNameLabel.setText(data.getProcessName());
        ramLabel.setText(
                "Avg RAM: " + (data.getAvgMemoryUsage() / (1024)) + " MB"
        );
        cpuLabel.setText(
                "Avg CPU: " + String.format("%.2f", data.getAvgCpuUsage()) + "%"
        );
    }
}
