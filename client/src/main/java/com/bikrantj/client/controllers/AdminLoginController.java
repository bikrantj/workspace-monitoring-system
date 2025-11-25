package com.bikrantj.client.controllers;

import com.bikrantj.client.navigation.NavigationManager;
import com.bikrantj.client.navigation.Screens;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class AdminLoginController {
    public TextField email;
    public PasswordField password;
    public AnchorPane rootPane;

    public void onLoginButtonClicked(ActionEvent actionEvent) {
    }

    public void onRegisterButtonClicked(ActionEvent actionEvent) {
        NavigationManager.navigateTo(Screens.ADMIN_REGISTER);
    }

    public void onBackToHomeClicked(MouseEvent mouseEvent) {
        NavigationManager.navigateTo(Screens.INITIAL_VIEW);
    }
}
