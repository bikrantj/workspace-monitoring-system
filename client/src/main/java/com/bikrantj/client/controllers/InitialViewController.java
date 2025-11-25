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

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void onAdminButtonClick(ActionEvent actionEvent) {
//        Navigate to admin login page.
        NavigationManager.navigateTo(Screens.ADMIN_LOGIN);
    }
}
