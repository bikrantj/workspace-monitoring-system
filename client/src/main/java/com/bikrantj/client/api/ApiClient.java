package com.bikrantj.client.api;

import com.bikrantj.shared.dto.User;
import com.bikrantj.shared.model.Client;
import com.bikrantj.shared.model.Workspace;
import com.bikrantj.shared.requests.*;
import com.bikrantj.shared.responses.LoginResponse;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class ApiClient {
    private final HttpService http;

    public ApiClient(String baseUrl) {
        this.http = new HttpService(baseUrl);
    }

    public void registerUser(RegisterRequest request) throws ApiException {
        http.post("/auth/register", request, Void.class);
    }

    public LoginResponse loginUser(LoginRequest request) throws ApiException {
        return http.post("/auth/login", request, LoginResponse.class);
    }

    public User getCurrentUser() throws ApiException {
        return http.get("/auth/me", User.class);
    }

    public void createWorkspace(CreateWorkspaceRequest request) throws ApiException {
        http.post("/workspace/create", request, Void.class);
    }

    public Client clientLogin(CreateClientRequest request) throws ApiException {
        return http.post("/client/login", request, Client.class);
    }

    public List<Workspace> getWorkspace() throws ApiException {
        return http.getGeneric("/workspace", new TypeReference<List<Workspace>>() {
        });
    }

    public void sendMonitoringSnapshot(MonitoringPayload payload) throws ApiException {
        http.post("/monitoring/ingest", payload, Void.class);
    }
}
