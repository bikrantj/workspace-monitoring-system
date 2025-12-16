module com.bikrantj.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.bikrantj.shared;
    requires org.controlsfx.controls;
    requires javafx.graphics;
    requires java.desktop;
    requires com.github.oshi;


    opens com.bikrantj.client to javafx.fxml;
    exports com.bikrantj.client;
    exports com.bikrantj.client.controllers;
    exports com.bikrantj.client.api;
    opens com.bikrantj.client.controllers to javafx.fxml;
}