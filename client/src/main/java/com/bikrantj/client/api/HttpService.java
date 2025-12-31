package com.bikrantj.client.api; // Change to your package

import com.bikrantj.client.auth.TokenManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpService {

    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String baseUrl;

    public HttpService(String baseUrl) {
        this.baseUrl = baseUrl;

        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        this.mapper = new ObjectMapper();
        // Optional: for LocalDate, LocalDateTime support
        // mapper.findAndRegisterModules();
    }

    // ========================== PUBLIC METHODS ==========================

    // For single objects (Class<T>)
    public <T> T get(String endpoint, Class<T> responseType) throws ApiException {
        HttpRequest request = buildGetRequest(endpoint);
        return send(request, responseType);
    }

    public <T> T post(String endpoint, Object requestBody, Class<T> responseType) throws ApiException {
        HttpRequest request = buildPostRequest(endpoint, requestBody);
        return send(request, responseType);
    }

    // For List<T>, Map, etc. (TypeReference<T>)
    public <T> T getGeneric(String endpoint, TypeReference<T> typeRef) throws ApiException {
        HttpRequest request = buildGetRequest(endpoint);
        return send(request, typeRef);
    }

    public <T> T postGeneric(String endpoint, Object requestBody, TypeReference<T> typeRef) throws ApiException {
        HttpRequest request = buildPostRequest(endpoint, requestBody);
        return send(request, typeRef);
    }

    // ========================== PRIVATE HELPERS ==========================

    private HttpRequest buildGetRequest(String endpoint) {
        var builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .timeout(Duration.ofSeconds(30))
                .header("Accept", "application/json")
                .GET();


        addAuthHeader(builder);
        return builder.build();
    }

    private HttpRequest buildPostRequest(String endpoint, Object body) throws ApiException {
        try {
            String json = mapper.writeValueAsString(body);
            var builder = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + endpoint))
                    .timeout(Duration.ofSeconds(30))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json));
            addAuthHeader(builder);
            return builder.build();
        } catch (Exception e) {
            throw new ApiException("Failed to serialize request body", "Failed to serialize request body");
        }
    }

    // Overload 1: Class<T> → convert to JavaType then call main send()
    private <T> T send(HttpRequest request, Class<T> responseType) throws ApiException {
        JavaType javaType = mapper.getTypeFactory().constructType(responseType);
        return send(request, javaType);
    }

    // Overload 2: TypeReference<T> → convert to JavaType then call main send()
    private <T> T send(HttpRequest request, TypeReference<T> typeRef) throws ApiException {
        JavaType javaType = mapper.getTypeFactory().constructType(typeRef);
        return send(request, javaType);
    }

    // MAIN send() method – only one that actually talks to the server
    private <T> T send(HttpRequest request, JavaType javaType) throws ApiException {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            int statusCode = response.statusCode();

            if (statusCode >= 200 && statusCode < 300) {
                if (body == null || body.trim().isEmpty()) {
                    return null;
                }
                return mapper.readValue(body, javaType);
            } else {
                // Server responded with error status (400, 401, 403, 404, 500, etc.)
                String errorBody = body != null && !body.trim().isEmpty() ? body.trim() : "Error " + response.statusCode();
                throw new ApiException(errorBody, "");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException("Request interrupted", "The request was interrupted");

        } catch (Exception e) {
            // Network-level failure (no response from server at all)
            String msg = e.getMessage() != null ? e.getMessage() : "Unknown network error";
            throw new ApiException("Request failed", msg);
        }
    }

    private void addAuthHeader(HttpRequest.Builder builder) {
        String token = TokenManager.getToken();
        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }
    }
}