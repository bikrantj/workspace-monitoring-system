package com.bikrantj.client.controllers;

import com.bikrantj.client.auth.ClientPersistence;
import com.bikrantj.client.auth.ClientSession;
import com.bikrantj.client.navigation.NavigationManager;
import com.bikrantj.client.navigation.Screens;
import javafx.event.ActionEvent;

public class ClientDashboardController {
    public void onLogoutClicked(ActionEvent actionEvent) {
        ClientPersistence.clear();
        ClientSession.clear();
        NavigationManager.navigateTo(Screens.INITIAL_VIEW);
    }
}
