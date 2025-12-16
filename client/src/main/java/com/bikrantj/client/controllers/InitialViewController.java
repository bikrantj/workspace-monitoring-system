package com.bikrantj.client.controllers;

import com.bikrantj.client.navigation.NavigationManager;
import com.bikrantj.client.navigation.Screens;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class InitialViewController {
    public TextField email;
    public PasswordField password;
    @FXML
    private Label welcomeText;


    public void onAdminButtonClick(ActionEvent actionEvent) {
//        Navigate to admin login page.
        NavigationManager.navigateTo(Screens.ADMIN_LOGIN);
    }

    public void onClientButtonClick(ActionEvent actionEvent) {
        NavigationManager.navigateTo(Screens.CLIENT_LOGIN);
    }
}
