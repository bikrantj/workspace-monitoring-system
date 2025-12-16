package com.bikrantj.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class GraphCardController {

    public CategoryAxis xAxis;
    public NumberAxis yAxis;
    @FXML
    private Label titleLabel;

    @FXML
    private LineChart<String, Number> chart;

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setAxisLabels(String xLabel, String yLabel) {
        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);
    }

    /**
     * Used for real API-driven graphs
     */
    public void setSeries(XYChart.Series<String, Number> series) {
        chart.getData().clear();
        chart.getData().add(series);
    }

    /**
     * Used only for placeholder/demo graphs
     */
    public void loadDemoData() {
        XYChart.Series<String, Number> demo = new XYChart.Series<>();
        demo.setName("Demo");

        demo.getData().add(new XYChart.Data<>("10:00", 5));
        demo.getData().add(new XYChart.Data<>("11:00", 9));
        demo.getData().add(new XYChart.Data<>("12:00", 4));
        demo.getData().add(new XYChart.Data<>("13:00", 12));

        setAxisLabels("Demo Time", "Demo Value");
        setSeries(demo);
    }
}
