package com.bikrantj.client.api;

import com.bikrantj.shared.dto.User;
import com.bikrantj.shared.requests.CreateWorkspaceRequest;
import com.bikrantj.shared.requests.LoginRequest;
import com.bikrantj.shared.requests.RegisterRequest;
import com.bikrantj.shared.responses.LoginResponse;

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
}
