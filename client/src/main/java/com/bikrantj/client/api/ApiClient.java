package com.bikrantj.client.api;

import com.bikrantj.shared.dto.User;
import com.bikrantj.shared.model.Client;
import com.bikrantj.shared.model.ProcessInfo;
import com.bikrantj.shared.model.Screenshot;
import com.bikrantj.shared.model.Workspace;
import com.bikrantj.shared.requests.*;
import com.bikrantj.shared.responses.ActivityPoint;
import com.bikrantj.shared.responses.ClientListView;
import com.bikrantj.shared.responses.HighRamProcessUsage;
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

    public List<ActivityPoint> getWorkspaceActivity(String workspaceId) throws ApiException {
        return http
                .getGeneric(
                        "/workspace/" + workspaceId + "/metrics/activity",
                        new TypeReference<List<ActivityPoint>>() {
                        }
                );
    }

    public List<Client> getClientsByWorkspace(String workspaceId) throws ApiException {
        System.out.println("API: Fetching clients for workspace ID: " + workspaceId);
        return http
                .getGeneric(
                        "/workspace/" + workspaceId + "/clients",
                        new TypeReference<List<Client>>() {
                        }
                );
    }


    public List<HighRamProcessUsage> getHighRamUsageProcesses(String workspaceId, String clientId) throws ApiException {
        return http
                .getGeneric(
                        "/workspace/" + workspaceId + "/client/" + clientId + "/metrics/high-ram",
                        new TypeReference<List<HighRamProcessUsage>>() {
                        }
                );


    }


    //        app.get("/workspace/{workspaceId}/client/{clientId}/metrics/activity", metricsController::getClientActivity);
    public List<ActivityPoint> getClientActivity(String workspaceId, String clientId) {
        try {
            return http
                    .getGeneric(
                            "/workspace/" + workspaceId + "/client/" + clientId + "/metrics/activity",
                            new TypeReference<List<ActivityPoint>>() {
                            }
                    );
        } catch (ApiException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<ProcessInfo> getLatestProcesses(String workspaceId, String clientId) {
        try {
            return http
                    .getGeneric(
                            "/workspace/" + workspaceId + "/client/" + clientId + "/metrics/latest-processes",
                            new TypeReference<List<ProcessInfo>>() {
                            }
                    );
        } catch (ApiException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<ClientListView> getAllAdminDevices() throws ApiException {
        return http.getGeneric(
                "/api/admin/devices",
                new TypeReference<List<ClientListView>>() {
                }
        );
    }

    public void deleteAdminDevice(DeleteClientRequest request) throws ApiException {
        http.post(
                "/api/admin/device", request,
                Void.class
        );
    }

    public Screenshot getLatestSceenshot(String workspaceId, String clientId) {
        try {
            return http
                    .get(
                            "/workspace/" + workspaceId + "/client/" + clientId + "/screenshot/latest",
                            Screenshot.class
                    );
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
    }
}
