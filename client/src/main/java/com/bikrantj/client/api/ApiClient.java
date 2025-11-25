package com.bikrantj.client.api;

import com.bikrantj.shared.requests.RegisterRequest;

public class ApiClient {
    private final HttpService http;

    public ApiClient(String baseUrl) {
        this.http = new HttpService(baseUrl);
    }

    public void registerUser(RegisterRequest request) throws ApiException {
        http.post("/auth/register", request, Void.class);
    }
}
