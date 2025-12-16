package com.bikrantj.client.auth;


import com.bikrantj.shared.model.Client;

public final class ClientSession {

    private static Client currentClient;

    private ClientSession() {
    }

    public static void set(Client client) {
        currentClient = client;
    }

    public static Client get() {
        return currentClient;
    }

    public static boolean isLoggedIn() {
        return currentClient != null;
    }

    public static void clear() {
        currentClient = null;
    }
}
