package com.bikrantj.shared.requests;

public class DeleteClientRequest {
    private String clientId;

    public DeleteClientRequest() {
    }

    public DeleteClientRequest(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
