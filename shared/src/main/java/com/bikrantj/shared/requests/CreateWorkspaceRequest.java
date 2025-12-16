package com.bikrantj.shared.requests;

public class CreateWorkspaceRequest {
    private String name;
    private String description;
    private String uniqueId;

    public CreateWorkspaceRequest() {
    }

    public CreateWorkspaceRequest(String name, String description, String uniqueId) {
        this.name = name;
        this.description = description;
        this.uniqueId = uniqueId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
