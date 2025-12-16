package com.bikrantj.client.auth;

import com.bikrantj.client.navigation.NavigationManager;
import com.bikrantj.client.navigation.Screens;
import com.bikrantj.shared.dto.User;

public class UserSession {
    private static User currentUser;
    private static String token;

    public static void setUser(User user, String jwtToken) {
        currentUser = user;
        token = jwtToken;
        TokenManager.saveToken(jwtToken);  // persist
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static String getToken() {
        return token;
    }

    public static boolean isLoggedIn() {
        return currentUser != null && token != null;
    }

    public static void clear() {
        currentUser = null;
        token = null;
        TokenManager.clearToken();
        NavigationManager.navigateTo(Screens.INITIAL_VIEW);
    }
}