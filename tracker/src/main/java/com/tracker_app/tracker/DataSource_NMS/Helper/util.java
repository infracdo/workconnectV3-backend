package com.tracker_app.tracker.DataSource_NMS.Helper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javassist.bytecode.ExceptionTable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class util {

  @Value("${NETBOX.TOKEN}")
  private String netboxAuthToken;

  @Value("${AWX.BEARER.TOKEN}")
  private String axwToken;

  public int ipToInt(String ipAddress) {
    String[] parts = ipAddress.split("\\.");
    int result = 0;
    for (String part : parts) {
      result = (result << 8) | Integer.parseInt(part);
    }
    return result;
  }

  public boolean isInRange(int ipToCheck, int start, int end) {
    return ipToCheck >= start && ipToCheck <= end;
  }

  public boolean isValidIPAddress(String ip) {
    String zeroTo255 = "(\\d{1,2}|(0|1)\\" + "d{2}|2[0-4]\\d|25[0-5])";

    String regex =
      zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;

    Pattern p = Pattern.compile(regex);

    if (ip == null) {
      return false;
    }

    Matcher m = p.matcher(ip);

    return m.matches();
  }

  public void createHostAWX(String storeId, String ansibleHost) {
    HttpClient httpClient = HttpClient.newHttpClient();
    try {
      String requestBody =
        "{\n" +
        "    \"name\": \"CPE-" +
        storeId +
        "\",\n" +
        "    \"description\": \"\",\n" +
        "    \"enabled\": true,\n" +
        "    \"instance_id\": \"\",\n" +
        "    \"variables\": \"ansible_host: " +
        ansibleHost +
        "\"\n" +
        "}";
      // String requestBody =
      //   "{\n" +
      //   "    \"name\": \"CPE-ST-5475" +
      //   "\",\n" +
      //   "    \"description\": \"\",\n" +
      //   "    \"enabled\": true,\n" +
      //   "    \"instance_id\": \"\",\n" +
      //   "    \"variables\": \"ansible_host: 10.8.208.20" +
      //   "\"\n" +
      //   "}";

      HttpRequest request = HttpRequest
        .newBuilder()
        .uri(
          new URI("https://awx.apolloglobal.net/api/v2/inventories/41/hosts/")
        )
        .header("Authorization", "Bearer " + axwToken)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .build();

      HttpResponse<String> response = httpClient.send(
        request,
        HttpResponse.BodyHandlers.ofString()
      );
      System.out.println(response.body());
    } catch (IOException | InterruptedException | URISyntaxException e) {
      e.printStackTrace();
    }
  }

  public String getIpNetbox(String siteId) {
    HttpClient httpClient = HttpClient.newHttpClient();

    try {
      HttpRequest request = HttpRequest
        .newBuilder()
        .uri(
          new URI(
            "https://netbox.apolloglobal.net/api/dcim/devices/?tenant=psc&name__ic=CPE-" +
            siteId +
            "&status=active"
          )
        )
        .header("Authorization", "Token " + netboxAuthToken)
        .GET()
        .build();

      HttpResponse<String> response = httpClient.send(
        request,
        HttpResponse.BodyHandlers.ofString()
      );
      try {
        JSONObject jsonResponse = new JSONObject(response.body());
        JSONArray resultArray = jsonResponse.getJSONArray("results");
        if (resultArray.length() > 0) {
          JSONObject firstResult = resultArray.getJSONObject(0);
          JSONObject primaryIp = firstResult.getJSONObject("primary_ip");
          String ip = primaryIp.getString("address");

          return ip.split("/")[0];
        } else {
          return "nonexistent";
        }
      } catch (JSONException e) {
        e.printStackTrace();
        return "";
      }
    } catch (IOException | InterruptedException | URISyntaxException e) {
      e.printStackTrace();
      return "";
    }
  }

  public String getStoreStatus(String siteId) {
    HttpClient httpClient = HttpClient.newHttpClient();

    try {
      HttpRequest request = HttpRequest
        .newBuilder()
        .uri(
          new URI(
            "https://netbox.apolloglobal.net/api/dcim/sites/?tenant=psc&name=" +
            siteId
          )
        )
        .header("Authorization", "Token " + netboxAuthToken)
        .GET()
        .build();

      HttpResponse<String> response = httpClient.send(
        request,
        HttpResponse.BodyHandlers.ofString()
      );
      try {
        JSONObject jsonResponse = new JSONObject(response.body());
        JSONArray resultArray = jsonResponse.getJSONArray("results");
        if (resultArray.length() > 0) {
          JSONObject firstResult = resultArray.getJSONObject(0);
          JSONObject storeCustomFields = firstResult.getJSONObject(
            "custom_fields"
          );
          System.out.println(storeCustomFields);
          if (storeCustomFields.has("Store Status")) {
            return storeCustomFields.getString("Store Status");
          } else {
            return "";
          }
        } else {
          return "";
        }
      } catch (JSONException e) {
        e.printStackTrace();
        return "";
      }
    } catch (IOException | InterruptedException | URISyntaxException e) {
      e.printStackTrace();
      return "";
    }
  }

  public int extractJobValue(String responseBody) {
    String jobKey = "\"id\":";
    int jobIndex = responseBody.indexOf(jobKey);

    if (jobIndex != -1) {
      int startIndex = jobIndex + jobKey.length();
      int endIndex = responseBody.indexOf(",", startIndex);
      if (endIndex == -1) {
        endIndex = responseBody.indexOf("}", startIndex);
      }

      if (endIndex != -1) {
        String jobValue = responseBody.substring(startIndex, endIndex).trim();
        int jobId = Integer.parseInt(jobValue);
        return jobId;
      }
    }

    return 0;
  }

  public int launchJob(int templateId, String requestBody) {
    HttpClient httpClient = HttpClient.newHttpClient();
    String url =
      "https://awx.apolloglobal.net/api/v2/job_templates/" +
      templateId +
      "/launch/";
    try {
      HttpRequest request = HttpRequest
        .newBuilder()
        .uri(new URI(url))
        .header("Authorization", "Bearer " + axwToken)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .build();

      HttpResponse<String> response = httpClient.send(
        request,
        HttpResponse.BodyHandlers.ofString()
      );

      String responseBody = response.body();
      return extractJobValue(responseBody);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }
}
