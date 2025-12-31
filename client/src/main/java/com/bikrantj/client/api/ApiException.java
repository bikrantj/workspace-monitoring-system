package com.bikrantj.client.api;


public class ApiException extends Exception {
    private final String serverMessage;

    public ApiException(String message, String serverMessage) {
        super(message);
        this.serverMessage = serverMessage;
    }

    public String getServerMessage() {
        return serverMessage;
    }
}