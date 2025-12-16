package com.bikrantj.client.controllers;

import com.bikrantj.client.AppContext;
import com.bikrantj.client.api.ApiClient;
import com.bikrantj.client.api.ApiException;
import com.bikrantj.client.auth.TokenManager;
import com.bikrantj.client.auth.UserSession;
import com.bikrantj.client.navigation.NavigationManager;
import com.bikrantj.client.navigation.Screens;
import com.bikrantj.client.ui.Toast;
import com.bikrantj.shared.requests.LoginRequest;
import com.bikrantj.shared.responses.LoginResponse;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class AdminLoginController {
    public TextField email;
    public PasswordField password;
    public AnchorPane rootPane;
    ApiClient api = AppContext.getApiClient();

    public void onLoginButtonClicked(ActionEvent actionEvent) {
        LoginRequest request = new LoginRequest(
                email.getText(),
                password.getText()
        );
        try {
            LoginResponse response = api.loginUser(request);

            UserSession.setUser(response.user(), response.token());

            password.clear();
            TokenManager.saveToken(response.token());
            NavigationManager.navigateTo(Screens.DASHBOARD);
            Toast.success(rootPane, "Welcome back, " + response.user().username() + "!");

//            Save the token
        } catch (ApiException e) {
            Toast.error(rootPane, "Invalid email or password");
        }
    }

    public void onRegisterButtonClicked(ActionEvent actionEvent) {
        NavigationManager.navigateTo(Screens.ADMIN_REGISTER);
    }

    public void onBackToHomeClicked(MouseEvent mouseEvent) {
        NavigationManager.navigateTo(Screens.INITIAL_VIEW);
    }

    protected Pane getPane() {
        return (Pane) email.getScene().getRoot();
    }
}
