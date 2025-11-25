package com.bikrantj.client.controllers;

import com.bikrantj.client.AppContext;
import com.bikrantj.client.api.ApiClient;
import com.bikrantj.client.api.ApiException;
import com.bikrantj.client.navigation.NavigationManager;
import com.bikrantj.client.navigation.Screens;
import com.bikrantj.client.ui.Toast;
import com.bikrantj.shared.requests.RegisterRequest;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class AdminRegisterController {
    private final ApiClient api = AppContext.getApiClient();
    public TextField email;
    public TextField username;
    public PasswordField password;
    public AnchorPane rootPane;

    public void onRegisterButtonClicked(ActionEvent actionEvent) {
//        TODO: send data to api.
        RegisterRequest request = new RegisterRequest(
                username.getText(),
                password.getText(),
                email.getText()
        );

        try {
            api.registerUser(request);
            System.out.println("Registration successful!");
            Toast.success(getPane(), "Registration successful! Please login.");
            NavigationManager.navigateTo(Screens.ADMIN_LOGIN);
        } catch (ApiException e) {
            Toast.error(getPane(), "Email already exists");
            System.out.println("Registration failed: " + e.getMessage());
//            throw new RuntimeException(e);
        }

    }

    public void onLoginButtonClicked(ActionEvent actionEvent) {
        NavigationManager.navigateTo(Screens.ADMIN_LOGIN);
    }

    protected Pane getPane() {
        return (Pane) email.getScene().getRoot();
    }

    public void onBackToHomeClicked(MouseEvent mouseEvent) {
        NavigationManager.navigateTo(Screens.INITIAL_VIEW);
    }
}
