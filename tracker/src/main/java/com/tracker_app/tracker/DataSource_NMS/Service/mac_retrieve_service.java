package com.tracker_app.tracker.DataSource_NMS.Service;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.tracker_app.tracker.DataSource_NMS.Entity.provider_circuits_today;
import com.tracker_app.tracker.DataSource_NMS.Helper.util;
import com.tracker_app.tracker.DataSource_NMS.Repo.provider_circuits_mindanao_repo;
import com.tracker_app.tracker.DataSource_NMS.Repo.provider_circuits_repo;
import com.tracker_app.tracker.DataSource_NMS.Repo.provider_circuits_today_repo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class mac_retrieve_service {

  @Value("${NETBOX.TOKEN}")
  private String netboxAuthToken;

  @Value("${INFRA.PASS.SERVER}")
  private String infraPass;

  @Value("${AWX.BEARER.TOKEN}")
  private String axwToken;

  @Autowired
  private provider_circuits_repo provider_circuits_repo;

  @Autowired
  private provider_circuits_today_repo provider_circuits_today_repo;

  @Autowired
  private circuit_service circuitService;

  @Autowired
  private util util;

  // @Autowired
  // private ElasticSiteSummary_service elasticSiteSummaryService;
  private String prometheusQuery =
    "https://prometheus.apolloglobal.net/api/v1/query?query=";

  // public String getIpNetbox(String siteId) {
  //   HttpClient httpClient = HttpClient.newHttpClient();

  //   try {
  //     HttpRequest request = HttpRequest
  //       .newBuilder()
  //       .uri(
  //         new URI(
  //           "https://netbox.apolloglobal.net/api/dcim/devices/?tenant=psc&name__ic=CPE-" +
  //           siteId +
  //           "&status=active"
  //         )
  //       )
  //       .header("Authorization", "Token " + netboxAuthToken)
  //       .GET()
  //       .build();

  //     HttpResponse<String> response = httpClient.send(
  //       request,
  //       HttpResponse.BodyHandlers.ofString()
  //     );
  //     try {
  //       JSONObject jsonResponse = new JSONObject(response.body());
  //       JSONArray resultArray = jsonResponse.getJSONArray("results");
  //       if (resultArray.length() > 0) {
  //         JSONObject firstResult = resultArray.getJSONObject(0);
  //         JSONObject primaryIp = firstResult.getJSONObject("primary_ip");
  //         String ip = primaryIp.getString("address");

  //         return ip.split("/")[0];
  //       } else {
  //         return "";
  //       }
  //     } catch (JSONException e) {
  //       e.printStackTrace();
  //       return "";
  //     }
  //   } catch (IOException | InterruptedException | URISyntaxException e) {
  //     e.printStackTrace();
  //     return "";
  //   }
  // }

  public String getLoopback1(String storeId) {
    HttpClient httpClient = HttpClient.newHttpClient();

    try {
      String encodedQuery = URLEncoder.encode(
        "uptime_availability_24hrs{job=\"loopback 1\", site_id=\"" +
        storeId +
        "\"}",
        StandardCharsets.UTF_8
      );

      HttpRequest request = HttpRequest
        .newBuilder()
        .uri(new URI(prometheusQuery + encodedQuery))
        .GET()
        .build();

      HttpResponse<String> response = httpClient.send(
        request,
        HttpResponse.BodyHandlers.ofString()
      );

      try {
        JSONObject jsonResponse = new JSONObject(response.body());
        JSONArray resultArray = jsonResponse
          .getJSONObject("data")
          .getJSONArray("result");

        int lastIndex = resultArray.length() - 1;
        if (lastIndex >= 0) {
          JSONObject lastResult = resultArray.getJSONObject(lastIndex);
          JSONObject metricObject = lastResult.getJSONObject("metric");
          String instanceValue = metricObject.getString("instance");

          return instanceValue;
        } else {
          return "No data found";
        }
      } catch (JSONException e) {
        e.printStackTrace();
        return "Error parsing JSON response";
      }
    } catch (IOException | InterruptedException | URISyntaxException e) {
      e.printStackTrace();
      return "Error in HTTP request";
    }
  }

  public String getLoopback2(String storeId) {
    HttpClient httpClient = HttpClient.newHttpClient();

    try {
      String encodedQuery = URLEncoder.encode(
        "uptime_availability_24hrs{job=\"loopback 2\", site_id=\"" +
        storeId +
        "\"}",
        StandardCharsets.UTF_8
      );

      HttpRequest request = HttpRequest
        .newBuilder()
        .uri(new URI(prometheusQuery + encodedQuery))
        .GET()
        .build();

      HttpResponse<String> response = httpClient.send(
        request,
        HttpResponse.BodyHandlers.ofString()
      );

      try {
        JSONObject jsonResponse = new JSONObject(response.body());
        JSONArray resultArray = jsonResponse
          .getJSONObject("data")
          .getJSONArray("result");

        int lastIndex = resultArray.length() - 1;
        if (lastIndex >= 0) {
          JSONObject lastResult = resultArray.getJSONObject(lastIndex);
          JSONObject metricObject = lastResult.getJSONObject("metric");
          String instanceValue = metricObject.getString("instance");

          return instanceValue;
        } else {
          return "No data found";
        }
      } catch (JSONException e) {
        e.printStackTrace();
        return "Error parsing JSON response";
      }
    } catch (IOException | InterruptedException | URISyntaxException e) {
      e.printStackTrace();
      return "Error in HTTP request";
    }
  }

  public String getLoopback0(String storeId) {
    HttpClient httpClient = HttpClient.newHttpClient();

    try {
      String encodedQuery = URLEncoder.encode(
        "uptime_availability_24hrs{job=\"loopback 0\", site_id=\"" +
        storeId +
        "\"}",
        StandardCharsets.UTF_8
      );

      HttpRequest request = HttpRequest
        .newBuilder()
        .uri(new URI(prometheusQuery + encodedQuery))
        .GET()
        .build();

      HttpResponse<String> response = httpClient.send(
        request,
        HttpResponse.BodyHandlers.ofString()
      );

      try {
        JSONObject jsonResponse = new JSONObject(response.body());
        JSONArray resultArray = jsonResponse
          .getJSONObject("data")
          .getJSONArray("result");

        int lastIndex = resultArray.length() - 1;
        if (lastIndex >= 0) {
          JSONObject lastResult = resultArray.getJSONObject(lastIndex);
          JSONObject metricObject = lastResult.getJSONObject("metric");
          String instanceValue = metricObject.getString("instance");

          return instanceValue;
        } else {
          return "No data found";
        }
      } catch (JSONException e) {
        e.printStackTrace();
        return "Error parsing JSON response";
      }
    } catch (IOException | InterruptedException | URISyntaxException e) {
      e.printStackTrace();
      return "Error in HTTP request";
    }
  }

  public String parseMacAndGtwIp(String storeId) {
    String password = infraPass;

    String ip = util.getIpNetbox(storeId);

    StringBuilder commandBuilder = new StringBuilder();
    try {
      if (
        util.isInRange(
          util.ipToInt(ip),
          util.ipToInt("10.9.32.0"),
          util.ipToInt("10.9.255.255")
        )
      ) {
        System.out.println(
          "STORE ID: " + storeId + ", IP: " + ip + ", MIKROTIK"
        );
        commandBuilder
          .append("cd run_remote_command")
          .append(" && python3 mikrotik_run_remote_command.py --ip ")
          .append(ip)
          .append(" --host ")
          .append(storeId)
          .append(" && cd ../parse_scripts")
          .append(" && python3 parse_mikrotik_vanilla.py ")
          .append(storeId);
      } else if (
        util.isInRange(
          util.ipToInt(ip),
          util.ipToInt("10.9.0.0"),
          util.ipToInt("10.9.31.255")
        )
      ) {
        System.out.println("STORE ID: " + storeId + ", IP: " + ip + ", RUIJIE");
        commandBuilder
          .append("cd run_remote_command")
          .append(" && python3 ruijie_run_remote_command.py --ip ")
          .append(ip)
          .append(" --host ")
          .append(storeId)
          .append(" && cd ../parse_scripts")
          .append(" && python3 parse_ruijie_vanilla.py ")
          .append(storeId);
      }

      String command = commandBuilder.toString();
      JSch jsch = new JSch();
      try {
        Session session = jsch.getSession("infra", "202.60.10.150", 22);
        // session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(60000);

        // Execute command
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);
        channelExec.setInputStream(null);
        channelExec.connect(60000);

        InputStream inputStream = channelExec.getInputStream();
        // Read command output
        try (
          BufferedReader reader = new BufferedReader(
            new InputStreamReader(inputStream)
          )
        ) {
          StringBuilder output = new StringBuilder();
          String line;

          while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
          }

          // Process the output as needed
          String commandOutput = output.toString().trim(); // Trim to remove leading/trailing whitespaces

          // Return the command output
          return commandOutput;
        } finally {
          // Close resources
          channelExec.disconnect();
          session.disconnect();
        }
      } catch (JSchException e) {
        e.printStackTrace();
        return "Error occurred during SSH command execution.";
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "Error occurred during outside of SHH command execution.";
    }
  }

  public String pingStore(String storeId) {
    String password = infraPass;

    String ip = util.getIpNetbox(storeId);

    StringBuilder commandBuilder = new StringBuilder();
    try {
      if (
        util.isInRange(
          util.ipToInt(ip),
          util.ipToInt("10.9.32.0"),
          util.ipToInt("10.9.255.255")
        )
      ) {
        System.out.println(
          "STORE ID: " + storeId + ", IP: " + ip + ", MIKROTIK"
        );
        commandBuilder
          .append("cd run_remote_command")
          .append(" && python3 mikrotik_run_ping_command.py --ip ")
          .append(ip)
          .append(" --host ")
          .append(storeId);
      } else if (
        util.isInRange(
          util.ipToInt(ip),
          util.ipToInt("10.9.0.0"),
          util.ipToInt("10.9.31.255")
        )
      ) {
        System.out.println("STORE ID: " + storeId + ", IP: " + ip + ", RUIJIE");
        commandBuilder
          .append("cd run_remote_command")
          .append(" && python3 ruijie_run_ping_command.py --ip ")
          .append(ip)
          .append(" --host ")
          .append(storeId);
      }

      String command = commandBuilder.toString();
      JSch jsch = new JSch();
      try {
        Session session = jsch.getSession("infra", "202.60.10.150", 22);
        // session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(60000);

        // Execute command
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);
        channelExec.setInputStream(null);
        channelExec.connect(60000);

        InputStream inputStream = channelExec.getInputStream();
        // Read command output
        try (
          BufferedReader reader = new BufferedReader(
            new InputStreamReader(inputStream)
          )
        ) {
          StringBuilder output = new StringBuilder();
          String line;

          while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
          }

          // Process the output as needed
          String commandOutput = output.toString().trim(); // Trim to remove leading/trailing whitespaces

          // Return the command output
          return commandOutput;
        } finally {
          // Close resources
          channelExec.disconnect();
          session.disconnect();
        }
      } catch (JSchException e) {
        e.printStackTrace();
        return "Error occurred during SSH command execution.";
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "Error occurred during outside of SHH command execution.";
    }
  }

  public String pingLoopbackIps(String loopback) {
    String password = infraPass;

    StringBuilder commandBuilder = new StringBuilder();
    try {
      commandBuilder
        .append("cd /home/infra/run_remote_command ")
        .append("; python3 ping_loopbacks.py")
        .append(" --loNum ")
        .append(loopback);
      // .append(" --lo1 ")
      // .append(lo1)
      // .append(" --lo2 ")
      // .append(lo2);

      String command = commandBuilder.toString();
      System.out.println(command);
      JSch jsch = new JSch();
      try {
        Session session = jsch.getSession("infra", "202.60.10.150", 22);
        // session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(60000);

        // Execute command
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);
        channelExec.setInputStream(null);
        channelExec.connect(60000);

        InputStream inputStream = channelExec.getInputStream();
        // Read command output
        try (
          BufferedReader reader = new BufferedReader(
            new InputStreamReader(inputStream)
          )
        ) {
          StringBuilder output = new StringBuilder();
          String line;

          while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
          }

          // Process the output as needed
          String commandOutput = output.toString().trim(); // Trim to remove leading/trailing whitespaces

          // Return the command output
          return commandOutput;
        }
      } catch (JSchException e) {
        e.printStackTrace();
        return "Error occurred during SSH command execution.";
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "Error occurred during outside of SHH command execution.";
    }
  }

  public String traceRoute(String storeId, String circuitType) {
    String password = infraPass;
    String ip = util.getIpNetbox(storeId);
    StringBuilder commandBuilder = new StringBuilder();
    try {
      if (
        util.isInRange(
          util.ipToInt(ip),
          util.ipToInt("10.9.32.0"),
          util.ipToInt("10.9.255.255")
        )
      ) {
        commandBuilder
          .append("cd /home/infra/run_remote_command ")
          .append("; python3 mikrotik_traceroute.py")
          .append(" --ip ")
          .append(ip)
          .append(" --ct ")
          .append(circuitType);
      } else if (
        util.isInRange(
          util.ipToInt(ip),
          util.ipToInt("10.9.0.0"),
          util.ipToInt("10.9.31.255")
        )
      ) {
        commandBuilder
          .append("cd /home/infra/run_remote_command ")
          .append("; python3 ruijie_traceroute.py")
          .append(" --ip ")
          .append(ip)
          .append(" --ct ")
          .append(circuitType);
      }

      String command = commandBuilder.toString();
      System.out.println(command);
      JSch jsch = new JSch();
      try {
        Session session = jsch.getSession("infra", "202.60.10.150", 22);
        // session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(60000);

        // Execute command
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);
        channelExec.setInputStream(null);
        channelExec.connect(60000);

        InputStream inputStream = channelExec.getInputStream();
        // Read command output
        try (
          BufferedReader reader = new BufferedReader(
            new InputStreamReader(inputStream)
          )
        ) {
          StringBuilder output = new StringBuilder();
          String line;

          while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
          }

          // Process the output as needed
          String commandOutput = output.toString().trim(); // Trim to remove leading/trailing whitespaces

          // Return the command output
          return commandOutput;
        }
      } catch (JSchException e) {
        e.printStackTrace();
        return "Error occurred during SSH command execution.";
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "Error occurred during outside of SHH command execution.";
    }
  }

  public String executeTroubleshootPlaybook(String storeId, String troubleshootType) {
    String newStoreId = "CPE-" + storeId;
    HttpClient httpClient = HttpClient.newHttpClient();
    Integer playbookId = null;

    if ("ipArp".equalsIgnoreCase(troubleshootType)) {
      playbookId = 159;
    } else if ("dhcpClient".equalsIgnoreCase(troubleshootType)) {
      playbookId = 160;
    } else if ("interfaceStatus".equalsIgnoreCase(troubleshootType)) {
      playbookId = 161;
    } else if ("ipAddress".equalsIgnoreCase(troubleshootType)) {
      playbookId = 162;
    } else if ("ipRoute".equalsIgnoreCase(troubleshootType)) {
      playbookId = 163;
    } else {
      return null;
    }
    String token = axwToken;
    String url = "https://awx.apolloglobal.net/api/v2/job_templates/" + playbookId + "/launch/";
    String requestBody =
      "{\n" +
      " \"job_template\": \"" + playbookId + "\",\n" +
      " \"limit\": \"" + newStoreId + "\"\n" +
      "}";

    try {
      HttpRequest request = HttpRequest
        .newBuilder()
        .uri(new URI(url))
        .header("Authorization", "Bearer " + token)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .build();

      HttpResponse<String> response = httpClient.send(
        request,
        HttpResponse.BodyHandlers.ofString()
      );

      String responseBody = response.body();
      String jobId = extractJobValue(responseBody);
      return jobId;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return "";
  }

  public String parseTroubleshootPlaybook(
    String jobId,
    String storeId,
    String troubleshootingType
) {
    System.out.println("JOB ID: " + jobId);
    HttpClient httpClient = HttpClient.newHttpClient();
    String url = "https://awx.apolloglobal.net/api/v2/jobs/" + jobId + "/stdout/?format=txt";

    try {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(url))
            .header("Authorization", "Bearer " + axwToken)
            .header("Content-Type", "application/json")
            .GET()
            .build();

        String responseBody = null;

        int retries = 60; // Retry up to 60 times (10 minutes max)
        while (retries-- > 0) {
            try {
                System.out.println("Checking for Play Recap...");
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.body() == null) {
                  throw new RuntimeException("AWX response is empty.");
                }
                responseBody = response.body().replaceAll("&quot;", "");
                if (responseBody.toLowerCase().contains("play recap")) {
                    break;
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Error while polling AWX: " + e.getMessage());
                throw new RuntimeException("Error polling AWX job output.", e);
            }
            Thread.sleep(10000); // Wait 10s before retry
        }
        if (retries <= 0) {
          throw new RuntimeException("Timeout: Play recap not found after multiple retries.");
        }
        String output = null;
        output = extractOutputFromRaw(responseBody, troubleshootingType);

        if (output != null) {
          System.out.println("parsed troubleshoot playbook output: " + output);
          return output;
        }

        throw new RuntimeException("No relevant data found.");

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("An error occurred while parsing the playbook output." + e.getMessage()); // Throw runtime exception
    }
  }

  public String extractOutputFromRaw(String responseBody, String type) {
    StringBuilder output = new StringBuilder();
    boolean start = false;

    for (String line : responseBody.split("\n")) {
        String header1;
        String header2;
        String header3;
        
        line = line.trim().replace("\"", ""); 
        line = line.replace(",", "");

        switch (type) {
          case "ipArp":
            header1 = "ADDRESS";
            header2 = "MAC-ADDRESS";
            header3 = "INTERFACE";
            break;
          case "dhcpClient":
            header1 = "USE";
            header2 = "ADD-DEFAULT-ROUTE";
            header3 = "ADDRESS";
              break;
          case "interfaceStatus":
            header1 = "NAME";
            header2 = "ACTUAL-MTU";
            header3 = "L2MTU";
              break;
          case "ipAddress":
            header1 = "ADDRESS";
            header2 = "NETWORK";
            header3 = "INTERFACE";
              break;
          case "ipRoute":
            header1 = "DST-ADDRESS";
            header2 = "PREF-SRC";
            header3 = "GATEWAY";
              break;
          default:
            throw new IllegalArgumentException("Unsupported type: " + type);
        }

        if (!start && line.toUpperCase().contains(header1) &&
            line.toUpperCase().contains(header2) &&
            line.toUpperCase().contains(header3)) {
            start = true;
            output.append(line).append("\n");  
            continue;
        }

        if (start && line.trim().equals("]")) {
            break;
        }

        if (start) {
            if (!line.trim().isEmpty()) {
                output.append(line).append("\n"); 
            }
        }
    }

    return output.toString();
  }

  public String executePublicIpPlaybook(String storeId, String routerType) {
    String lo0 = getLoopback0(storeId);
    String lo1 = getLoopback1(storeId);
    String lo2 = getLoopback2(storeId);
    System.out.println(storeId + "," + lo0 + "," + lo1 + "," + lo2);
    HttpClient httpClient = HttpClient.newHttpClient();
    String token = axwToken;
    String url = "https://awx.apolloglobal.net/api/v2/job_templates/67/launch/";
    String requestBody =
      "{\"job_template\": \"67\",\"ask_variables_on_launch\": true,\"extra_vars\": \"---\\nsite_id: " +
      storeId +
      " \\nlo0: " +
      lo0 +
      " \\nlo1: " +
      lo1 +
      " \\nlo2: " +
      lo2 +
      " \\nrouter_type: " +
      routerType +
      "\"}";

    try {
      HttpRequest request = HttpRequest
        .newBuilder()
        .uri(new URI(url))
        .header("Authorization", "Bearer " + token)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .build();

      HttpResponse<String> response = httpClient.send(
        request,
        HttpResponse.BodyHandlers.ofString()
      );

      String responseBody = response.body();
      // System.out.println("Response: " + responseBody);

      // Parse and handle the response as needed
      // ...
      String jobId = extractJobValue(responseBody);
      return jobId;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return "";
  }

  public String parsePublicIpPlaybook(
    String jobId,
    String storeId
  ) {
    System.out.println("JOB ID: " + jobId);
    HttpClient httpClient = HttpClient.newHttpClient();
    String token = axwToken;
    String url =
      "https://awx.apolloglobal.net/api/v2/jobs/" + jobId + "/stdout/";

    try {
      HttpRequest request = HttpRequest
        .newBuilder()
        .uri(new URI(url))
        .header("Authorization", "Bearer " + token)
        .header("Content-Type", "application/json")
        .GET()
        .build();

      String responseBody;

      // Check if Playbook is done running
      while (true) {
        try {
          System.out.println("Checking for Play Recap.");
          HttpResponse<String> response = httpClient.send(
            request,
            HttpResponse.BodyHandlers.ofString()
          );
          responseBody = response.body().replaceAll("&quot;", "").toLowerCase();
          if (responseBody.contains("play recap")) {
            break;
          }
        } catch (IOException | InterruptedException e) {
          System.err.println("Error sending request: " + e.getMessage());
        }
        Thread.sleep(10000);
      }
      String backup = "NO IP";
      String primary = "NO IP";
      String primaryProvider = "NO PROVIDER";
      String backupProvider = "NO PROVIDER";
      // System.out.println(responseBody);
      String[] lines = responseBody.split("\n");
      backup =
        Arrays
          .stream(lines)
          .filter(line -> line.contains("lo2_pub_ip.stdout:"))
          .map(line -> line.split(":")[1].trim().replaceAll("[^0-9.]", ""))
          .findFirst()
          .orElse(null);
      primary =
        Arrays
          .stream(lines)
          .filter(line -> line.contains("lo1_pub_ip.stdout:"))
          .map(line -> line.split(":")[1].trim().replaceAll("[^0-9.]", ""))
          .findFirst()
          .orElse(null);
      System.out.println("'" + primary + "' '" + backup + "'");
      System.out.println("primary: " + primary);
      System.out.println("backup: " + backup);
      if (util.isValidIPAddress(primary)) {
        primaryProvider = provider_circuits_repo.findProviderByIp(primary);
        System.out.println("primaryProv: " + primaryProvider);
      } else {
        primary = "No Data";
      }

      if (util.isValidIPAddress(backup)) {
        backupProvider = provider_circuits_repo.findProviderByIp(backup);
        System.out.println("backupProv: " + backupProvider);
      } else {
        backup = "No Data";
      }
      provider_circuits_today pct = provider_circuits_today_repo.findBySiteId(
        storeId
      );

      if (pct != null) {
        pct.setPublicIpPrimary(primary);
        pct.setPublicIpBackup(backup);
        pct.setProviderPrimary(primaryProvider);
        pct.setProviderBackup(backupProvider);

        provider_circuits_today_repo.save(pct);
      }

      String result = "public_ip_primary: " + primary + "\n" + "provider_primary: " + primaryProvider + "\n\n" + "public_ip_backup: " + backup + "\n" + "provider_backup: " + backupProvider;

      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public Map<String, Object> parsePublicIpPlaybookRuijie() {
    Map<String, Object> ipMap = new HashMap<>();
    ipMap.put("public_ip_primary", "No");
    ipMap.put("public_ip_backup", "Yet");
    ipMap.put("provider_primary", "Playbook");
    ipMap.put("provider_backup", "Ruijie");
    return ipMap;
  }

  private String extractJobValue(String responseBody) {
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
        return jobValue;
      }
    }

    return null;
  }

  public String enableSNMP(String storeId) {
    String password = infraPass;
    String ip = util.getIpNetbox(storeId);
    StringBuilder commandBuilder = new StringBuilder();
    try {
      if (
        util.isInRange(
          util.ipToInt(ip),
          util.ipToInt("10.9.32.0"),
          util.ipToInt("10.9.255.255")
        )
      ) {
        commandBuilder
          .append("cd /home/infra/run_remote_command ")
          .append("; python3 mikrotik_enable_snmp.py")
          .append(" --st ")
          .append(storeId);
      } else if (
        util.isInRange(
          util.ipToInt(ip),
          util.ipToInt("10.9.0.0"),
          util.ipToInt("10.9.31.255")
        )
      ) {
        commandBuilder
          .append("cd /home/infra/run_remote_command ")
          .append("; python3 ruijie_enable_snmp.py")
          .append(" --st ")
          .append(storeId);
      }

      String command = commandBuilder.toString();
      System.out.println(command);
      JSch jsch = new JSch();
      try {
        Session session = jsch.getSession("infra", "202.60.10.150", 22);
        // session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(60000);

        // Execute command
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);
        channelExec.setInputStream(null);
        channelExec.connect(60000);

        InputStream inputStream = channelExec.getInputStream();
        // Read command output
        try (
          BufferedReader reader = new BufferedReader(
            new InputStreamReader(inputStream)
          )
        ) {
          StringBuilder output = new StringBuilder();
          String line;

          while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
          }

          // Process the output as needed
          String commandOutput = output.toString().trim(); // Trim to remove leading/trailing whitespaces

          // Return the command output
          return commandOutput;
        }
      } catch (JSchException e) {
        e.printStackTrace();
        return "Error occurred during SSH command execution.";
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "Error occurred during outside of SHH command execution.";
    }
  }

  public String discoverSNMP(String storeId) {
    String password = infraPass;

    StringBuilder commandBuilder = new StringBuilder();
    try {
      commandBuilder
        .append("cd /home/infra/parse_scripts ")
        .append("; python3 snmp_serial_individual.py")
        .append(" --st ")
        .append(storeId);

      String command = commandBuilder.toString();
      System.out.println(command);
      JSch jsch = new JSch();
      try {
        Session session = jsch.getSession("infra", "202.60.10.150", 22);
        // session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(60000);

        // Execute command
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);
        channelExec.setInputStream(null);
        channelExec.connect(60000);

        InputStream inputStream = channelExec.getInputStream();
        // Read command output
        try (
          BufferedReader reader = new BufferedReader(
            new InputStreamReader(inputStream)
          )
        ) {
          StringBuilder output = new StringBuilder();
          String line;

          while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
          }

          // Process the output as needed
          String commandOutput = output.toString().trim(); // Trim to remove leading/trailing whitespaces

          // Return the command output
          return commandOutput;
        }
      } catch (JSchException e) {
        e.printStackTrace();
        return "Error occurred during SSH command execution.";
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "Error occurred during outside of SHH command execution.";
    }
  }
}
