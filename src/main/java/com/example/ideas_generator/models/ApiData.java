package com.example.ideas_generator.models;

public class ApiData {
    private String description;
    private String url;
    private String apiName;

    public ApiData(String apiName, String description, String url) {
        this.description = description;
        this.url = url;
        this.apiName = apiName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    @Override
    public String toString() {
        return "<<" + this.apiName + "-- " + this.description + ">>";
    }
}