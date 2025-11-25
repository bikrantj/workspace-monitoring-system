package com.bikrantj.client;

import com.bikrantj.client.api.ApiClient;

public class AppContext {
    private static ApiClient apiClient;

    public static void initialize(String baseUrl) {
        apiClient = new ApiClient(baseUrl);
    }

    public static ApiClient getApiClient() {
        return apiClient;
    }
}
