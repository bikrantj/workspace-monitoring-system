module com.bikrantj.client {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.bikrantj.client to javafx.fxml;
    exports com.bikrantj.client;
}