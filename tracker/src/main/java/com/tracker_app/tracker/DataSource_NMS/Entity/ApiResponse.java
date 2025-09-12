package com.tracker_app.tracker.DataSource_NMS.Entity;

public class ApiResponse {

  private String apiUrl = "https://catfact.ninja/fact?max_length=140";

  public String getApiUrl() {
    return apiUrl;
  }

  public void setApiUrl(String apiUrl) {
    this.apiUrl = apiUrl;
  }
}
