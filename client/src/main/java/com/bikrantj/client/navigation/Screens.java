package com.bikrantj.client.navigation;

public enum Screens {
    //    Common views
    INITIAL_VIEW("/com/bikrantj/client/initial-view.fxml"),

    //    Client views
    CLIENT_LOGIN("/com/bikrantj/client/client-login.fxml"),
    CLIENT_DASHBOARD("/com/bikrantj/client/client-dashboard.fxml"),

    //    Admin views
    ADMIN_LOGIN("/com/bikrantj/client/admin-login.fxml"),
    NEW_WORKSPACE("/com/bikrantj/client/admin-new-workspace.fxml"),

    ADMIN_REGISTER("/com/bikrantj/client/admin-register.fxml"),
    DASHBOARD_CONTENT("/com/bikrantj/client/admin-dashboard-content.fxml"),
    DASHBOARD("/com/bikrantj/client/admin-dashboard.fxml"),
    WORKSPACE("/com/bikrantj/client/workspace.fxml");

    private final String fxmlPath;

    Screens(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }
}