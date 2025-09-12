package com.tracker_app.tracker.Helper;

import com.tracker_app.tracker.DataSource_WorkConnect.Entity.sites_summary;
import com.tracker_app.tracker.DataSource_WorkConnect.Entity.tenant_lookup;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.sites_summary_repo;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.tenant_lookup_repo;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class atis_helper {

  @Autowired
  private tenant_lookup_repo tenant_repo;

  public Object get_site_assets(String atis_url, String site_id)
    throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response;
    HttpRequest request;
    System.out.println("get site assets function");
    request =
      HttpRequest
        .newBuilder()
        .uri(URI.create(atis_url + "/api/products/assets/?site_id=" + site_id))
        .GET()
        .build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString());
    System.out.println(response.body());
    if (
      response.statusCode() == 204 ||
      response.statusCode() == 201 ||
      response.statusCode() == 200
    ) {
      return response.body();
    } else {
      return "ERROR " + response.body();
    }
  }

  public Object get_site_interface(String atis_url, String site_id)
    throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response;
    HttpRequest request;

    request =
      HttpRequest
        .newBuilder()
        .uri(URI.create(atis_url + "/api/interface/?site_id=" + site_id))
        .GET()
        .build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (
      response.statusCode() == 204 ||
      response.statusCode() == 201 ||
      response.statusCode() == 200
    ) {
      return response.body();
    } else {
      return "ERROR " + response.body();
    }
  }

  public Object get_site_circuits(String atis_url, String site_id)
    throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response;
    HttpRequest request;

    request =
      HttpRequest
        .newBuilder()
        .uri(URI.create(atis_url + "/api/circuits/?site_id=" + site_id))
        .GET()
        .build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (
      response.statusCode() == 204 ||
      response.statusCode() == 201 ||
      response.statusCode() == 200
    ) {
      return response.body();
    } else {
      return "ERROR " + response.body();
    }
  }

  public Object get_circuit_providers(String atis_url)
    throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response;
    HttpRequest request;

    request =
      HttpRequest
        .newBuilder()
        .uri(URI.create(atis_url + "/api/circuits/provider_list"))
        .GET()
        .build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (
      response.statusCode() == 204 ||
      response.statusCode() == 201 ||
      response.statusCode() == 200
    ) {
      return response.body();
    } else {
      return "ERROR " + response.body();
    }
  }

  public Object get_all_islandGroups(String atis_url)
    throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response;
    HttpRequest request;

    request =
      HttpRequest
        .newBuilder()
        .uri(URI.create(atis_url + "/api/site/IslandGroups"))
        .GET()
        .build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (
      response.statusCode() == 204 ||
      response.statusCode() == 201 ||
      response.statusCode() == 200
    ) {
      return response.body();
    } else {
      return "ERROR " + response.body();
    }
  }

  public Object get_all_regions(String atis_url)
    throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response;
    HttpRequest request;

    request =
      HttpRequest
        .newBuilder()
        .uri(URI.create(atis_url + "/api/site/Regions"))
        .GET()
        .build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (
      response.statusCode() == 204 ||
      response.statusCode() == 201 ||
      response.statusCode() == 200
    ) {
      return response.body();
    } else {
      return "ERROR " + response.body();
    }
  }

  public Object get_all_site_status(String atis_url)
    throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response;
    HttpRequest request;

    request =
      HttpRequest
        .newBuilder()
        .uri(URI.create(atis_url + "/api/site/Status"))
        .GET()
        .build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (
      response.statusCode() == 204 ||
      response.statusCode() == 201 ||
      response.statusCode() == 200
    ) {
      return response.body();
    } else {
      return "ERROR " + response.body();
    }
  }

  public Object get_site_tenants(String atis_url)
    throws IOException, InterruptedException, JSONException {
    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response;
    HttpRequest request;

    request =
      HttpRequest
        .newBuilder()
        .uri(URI.create(atis_url + "/api/customer/"))
        .GET()
        .build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (
      response.statusCode() == 204 ||
      response.statusCode() == 201 ||
      response.statusCode() == 200
    ) {
      new Thread(() -> {
        save_tenant(response.body());
      })
        .start();
      return response.body();
    } else {
      return "ERROR " + response.body();
    }
  }

  public void save_tenant(String payload) {
    try {
      JSONArray tenants = new JSONArray(payload);
      for (int i = 0; i < tenants.length(); i++) {
        JSONObject tenant = tenants.getJSONObject(i);
        try {
          tenant_lookup tenanz = tenant_repo
            .findById(tenant.getLong("id"))
            .get();
          tenanz.setName(tenant.getString("name"));
          tenant_repo.save(tenanz);
        } catch (Exception e) {
          tenant_lookup tenanz = new tenant_lookup();
          tenanz.setId(tenant.getLong("id"));
          tenanz.setName(tenant.getString("name"));
          tenanz.setSlug(
            tenant.getString("name").toLowerCase().replace(" ", "_")
          );
          tenant_repo.save(tenanz);
        }
      }
    } catch (Exception e) {
      //TODO: handle exception
    }
  }
}
