package com.tracker_app.tracker.DataSource_NMS.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tracker_app.tracker.DataSource_NMS.Helper.util;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class executable_playbook_service {

  @Value("${AWX.BEARER.TOKEN}")
  private String axwToken;

  @Autowired
  private mac_retrieve_service mac_retrieve_service;

  @Autowired
  private util util;

  public int storeOnBoarding(String storeId, int managementIpJobId) {
    int jobTemplate = 62;
    String ip = getIpFromGTV2(managementIpJobId);
    String requestBody =
      "{\n" +
      "  \"job_template\": \"" +
      jobTemplate +
      "\",\n" +
      "  \"ask_variables_on_launch\": \"true\",\n" +
      "  \"extra_vars\": \"---\\nsite_id: " +
      storeId +
      " \\nmgmt_ip: " +
      ip +
      " \\nsite_status: active\"\n" +
      "}";
    System.out.println(requestBody);
    return util.launchJob(jobTemplate, requestBody);
  }

  public int storeOffBoarding(String storeId) {
    int jobTemplate = 64;

    String ip = util.getIpNetbox(storeId);
    String storeStatus = util.getStoreStatus(storeId);
    // if (ip.equals("nonexistent") && storeStatus.equals("")) {
    //   return 404;
    // }
    System.out.println(
      "Offboard Details: \n" +
      "Store Id: " +
      storeId +
      " Ip: " +
      ip +
      " Store Status: " +
      storeStatus
    );
    String requestBody =
      "{\n" +
      "  \"job_template\": \"" +
      jobTemplate +
      "\",\n" +
      "  \"ask_variables_on_launch\": \"true\",\n" +
      "  \"extra_vars\": \"---\\nsite_id: " +
      storeId +
      " \\nmgmt_ip: " +
      ip +
      " \\nsite_status: " +
      storeStatus +
      "\"\n" +
      "}";
    System.out.println(requestBody);
    return util.launchJob(jobTemplate, requestBody);
  }

  public int storeOperationsPlaybook(String storeId, String operationPlaybookType) {
    int jobTemplate;
    
    switch (operationPlaybookType) {
      case "addDhcpPort1DualCircuit":
        jobTemplate = 129;
        break;
      case "addDhcpPort1SingleCircuitBackup":
        jobTemplate = 131;
        break;
      case "addNetwatch":
        jobTemplate = 128;
        break;
      case "removeStaticRoutes":
        jobTemplate = 132;
        break;
      case "routeOmnishelfPrimary":
        jobTemplate = 138;
        break;
      case "cardTerminal":
        jobTemplate = 149;
        break;
      case "fixSameCircuitFound":
        jobTemplate = 125;
        break;
      default:
        throw new IllegalArgumentException("Unknown operation playbook type: " + operationPlaybookType);
    }

    System.out.println(
      "Operations Playbook Execution Details: \n" +
      "Store Id: " + storeId + "\n" +
      "Operatin Playbook Type: " + operationPlaybookType + "\n" +
      "Job Template: " + jobTemplate
    );
    
    String requestBody =
      "{\n" +
      " \"job_template\": \"" + jobTemplate + "\",\n" +
      " \"limit\": \"" + storeId + "\"\n" +
      "}";
    System.out.println(requestBody);
    return util.launchJob(jobTemplate, requestBody);
  }

  public int managementVpn(String storeId, String ansibleHost) {
    int jobTemplate = 59;

    String requestBody =
      "{\n" +
      "    \"job_template\": \"" +
      jobTemplate +
      "\",\n" +
      "    \"ask_variables_on_launch\": \"true\",\n" +
      "    \"limit\": \"CPE-" +
      storeId +
      "\",\n" +
      "    \"extra_vars\": \"---\\npreflight_ip: " +
      ansibleHost +
      "\"\n" +
      "}";
    System.out.println(requestBody);
    return util.launchJob(jobTemplate, requestBody);
  }

  public int ruijieV3(String storeId, String ansibleHost) {
    int jobTemplate = 70;
    System.out.println(storeId + " " + ansibleHost);
    String requestBody =
      "{\n" +
      "    \"job_template\": \"" +
      jobTemplate +
      "\",\n" +
      "    \"ask_variables_on_launch\": \"true\",\n" +
      "    \"limit\": \"CPE-" +
      storeId +
      "\",\n" +
      "    \"extra_vars\": \"---\\npreflight_ip: " +
      ansibleHost +
      "\"\n" +
      "}";
    System.out.println(requestBody);
    return util.launchJob(jobTemplate, requestBody);
  }

  public int gtv3(String storeId, String ansibleHost) {
    int jobTemplate = 60;
    // util.createHostAWX(storeId, ansibleHost);
    String requestBody =
      "{\n" +
      "    \"job_template\": \"" +
      jobTemplate +
      "\",\n" +
      "    \"ask_variables_on_launch\": \"true\",\n" +
      "    \"limit\": \"CPE-" +
      storeId +
      "\"\n" +
      "}";
    // return 1;
    System.out.println(requestBody);
    return util.launchJob(jobTemplate, requestBody);
  }

  public int storeUpdateStatus(String storeId, String storeStatus) {
    int jobTemplate = 63;

    String ip = util.getIpNetbox(storeId);

    String requestBody =
      "{\n" +
      "  \"job_template\": \"" +
      jobTemplate +
      "\",\n" +
      "  \"ask_variables_on_launch\": \"true\",\n" +
      "  \"extra_vars\": \"---\\nsite_id: " +
      storeId +
      " \\nmgmt_ip: " +
      ip +
      " \\nsite_status: " +
      storeStatus +
      "\"\n" +
      "}";
    System.out.println(requestBody);
    return util.launchJob(jobTemplate, requestBody);
  }

  public String checkPlaybookStatus(int jobId) {
    HttpClient httpClient = HttpClient.newHttpClient();

    String url =
      "https://awx.apolloglobal.net/api/v2/jobs/" +
      jobId +
      "/stdout/?format=txt";

    try {
      HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(url))
        .header("Authorization", "Bearer " + axwToken)
        .header("Content-Type", "application/json")
        .GET()
        .build();
      String responseBody;

      while (true) {
        try {
          System.out.println("Checking for Play Recap.");
          HttpResponse<String> response = httpClient.send(
            request,
            HttpResponse.BodyHandlers.ofString()
          );
          String rawResponseBody = response.body();
          responseBody = response.body().toLowerCase();

          if (responseBody.contains("play recap")) {
            System.out.println("Job ID: " + jobId + " Playbook Finished.");
            int failedCount = parseCount(responseBody, "failed");

            // Parse 'unreachable' value
            int unreachableCount = parseCount(responseBody, "unreachable");

            if (failedCount > 0 || unreachableCount > 0) {
              return "error";
            } else if (failedCount == 0 || unreachableCount == 0) { // if no errors
              // check if responsebody is for card terminal
              if (responseBody.contains("card-terminal") && responseBody.contains("display ssid and password")) {
                System.out.println("found card terminal ssid and password for " + jobId);
                // parse the SSID and Password
                // return responseBody;
                return parseTerminalCreds(rawResponseBody);
              }

              System.out.println("Playbook done running with job id: " + jobId);
              return "success";
            }
          }
        } catch (IOException | InterruptedException e) {
          System.err.println("Error sending request: " + e.getMessage());
        }
        Thread.sleep(10000);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  private int parseCount(String responseBody, String keyword) {
    // Parse 'failed' or 'unreachable' count
    Pattern pattern = Pattern.compile(keyword + "=(\\d+)");
    Matcher matcher = pattern.matcher(responseBody);
    if (matcher.find()) {
      String countValue = matcher.group(1);
      return Integer.parseInt(countValue);
    }
    return 0;
  }

  private String parseTerminalCreds(String responseBody) {
    // Parse 'failed' or 'unreachable' count
    Pattern pattern = Pattern.compile("\"msg\":\\s*\"(SSID: .*?Generated Password: [^\"]+)\"", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    Matcher matcher = pattern.matcher(responseBody);
    if (matcher.find()) {
      return matcher.group(1); // returns the SSID and Password string
    }
    return responseBody; // if failed to parse, return the original response
  }

  public String getIpFromGTV2(int jobId) {
    HttpClient httpClient = HttpClient.newHttpClient();
    String token = axwToken;
    String url =
      "https://awx.apolloglobal.net/api/v2/jobs/" +
      jobId +
      "/stdout/?format=txt";

    try {
      HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(url))
        .header("Authorization", "Bearer " + token)
        .header("Content-Type", "application/json")
        .GET()
        .build();

      String responseBody;

      HttpResponse<String> response = httpClient.send(
        request,
        HttpResponse.BodyHandlers.ofString()
      );
      responseBody = response.body();
      System.out.println(responseBody);

      Pattern pattern = Pattern.compile("(\"\\d+\\.\\d+\\.\\d+\\.\\d+\")");
      Matcher matcher = pattern.matcher(responseBody);
      String ipAddress = "";
      while (matcher.find()) {
        ipAddress = matcher.group(1).replace("\"", ""); // IP address
        System.out.println("Found IP Address: " + ipAddress);
      }

      return ipAddress;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public String checkPlaybookStatusAPI(int jobId) {
    String url = "https://awx.apolloglobal.net/api/v2/jobs/" + jobId;
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + axwToken);

    RestTemplate restTemplate = new RestTemplate();
    while (true) {
      try {
        RequestEntity<Void> requestEntity = new RequestEntity<>(
          headers,
          HttpMethod.GET,
          new URI(url)
        );
        ResponseEntity<String> responseEntity = restTemplate.exchange(
          requestEntity,
          String.class
        );
        HttpStatus statusCode = responseEntity.getStatusCode();
        String responseBody = responseEntity.getBody();

        if (statusCode.is2xxSuccessful()) {
          JSONObject jsonObject = new JSONObject(responseBody);
          String status = jsonObject.getString("status");
          if (!status.equals("running")) {
            if (status.equals("successful")) {
              return "success";
            } else if (status.equals("failed")) {
              return "failed";
            }
          }
        } else {
          return "Failed to fetch data: " + responseBody;
        }
      } catch (JSONException e) {
        e.printStackTrace();
      } catch (URISyntaxException e) {
        return "Invalid URL: " + url;
      }

      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
