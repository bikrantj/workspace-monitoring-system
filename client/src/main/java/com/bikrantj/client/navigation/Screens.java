package com.bikrantj.client.navigation;

public enum Screens {
    //    Common views
    INITIAL_VIEW("/com/bikrantj/client/initial-view.fxml"),

    //    Client views
    CLIENT_LOGIN("/com/bikrantj/client/client-login.fxml"),

    //    Admin views
    ADMIN_LOGIN("/com/bikrantj/client/admin-login.fxml"),
    ADMIN_REGISTER("/com/bikrantj/client/admin-register.fxml"),
    DASHBOARD("/com/bikrantj/client/dashboard.fxml");

    private final String fxmlPath;

    Screens(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }
}