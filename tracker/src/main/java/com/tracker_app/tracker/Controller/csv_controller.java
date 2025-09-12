package com.tracker_app.tracker.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import com.tracker_app.tracker.DataSource_NMS.Entity.provider_circuit;
import com.tracker_app.tracker.DataSource_NMS.Entity.store_data;
import com.tracker_app.tracker.DataSource_NMS.Repo.provider_circuits_repo;
import com.tracker_app.tracker.DataSource_NMS.Repo.store_data_repo;
// import com.tracker_app.tracker.DataSource_elasticsearch.service.ElasticSiteSummary_service;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class csv_controller {

  // @Autowired
  // ElasticSiteSummary_service elk_service;

  @Autowired
  private store_data_repo store_data_repo;

  @Autowired
  private provider_circuits_repo provider_circuits_repo;

  @GetMapping("/download-csv")
  public Object downloadCsv(HttpServletResponse response) throws Exception {
    // String csvUrl =
    // "http://localhost:7500/api/atis/site/v2/list?site_id=&siteName=&tenant=Philippine
    // Seven Corporation&no_links_up=&circuit_provider=&siteStatus=&region=&group=";
    // String netboxUrl =
    // "http://localhost:7500/api/atis/site/v2/list?site_id=&siteName=&tenant=&no_links_up=&circuit_provider=&siteStatus=&region=&group=";

    // HttpHeaders header = new HttpHeaders();
    // header.set("Authorization", "Token
    // e87e0916bdc7236b037746808c1c3b3da8fcba25");
    // header.setContentType(MediaType.APPLICATION_JSON);

    // String requestBody = "";

    // HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, header);

    // RestTemplate restTemplate = new RestTemplate();
    // ResponseEntity<String> responseEntity = restTemplate.exchange(csvUrl,
    // HttpMethod.GET, requestEntity,
    // String.class);

    // String responseBody = responseEntity.getBody();

    String responseBody = "";
    List<Map<String, Object>> result = getMockData(); // REPLACED ELASTICSEARCH CALL WITH MOCK
    System.out.println(result.toString());

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      responseBody = objectMapper.writeValueAsString(result);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    System.out.println(responseBody);

    try {
      // Your JSON response as a string
      // Read JSON as JsonNode
      JsonNode jsonNode = objectMapper.readTree(responseBody);

      // Create CSV file
      try (CSVWriter writer = new CSVWriter(new FileWriter("output.csv"))) {
        // Create a StringBuilder to hold CSV content
        StringBuilder csvContent = new StringBuilder();

        // Write headers
        String[] headers = {
            "site_id",
            "site_name",
            "tenant",
            "address",
            "network_status",
            "site_status",
            "loop_back_0_ip",
            "primary_ip",
            "backup_ip",
            "third_ip",
            "backup_provider",
            "primary_provider",
            "third_provider",
        };
        csvContent.append(String.join(",", headers)).append("\n");

        // Write data
        Iterator<JsonNode> iterator = jsonNode.iterator();
        while (iterator.hasNext()) {
          JsonNode node = iterator.next();
          try {
            String[] data = {
                getNodeTextValue(node, "site_id"),
                "\"" + getNodeTextValue(node, "name") + "\"", // Enclose in double-quotes
                "\"" + getNodeTextValue(node, "tenant") + "\"", // Enclose in double-quotes
                "\"" + getNodeTextValue(node, "address") + "\"", // Enclose in double-quotes
                getNodeTextValue(node, "network_status"),
                getNodeTextValue(node, "status"), // Assuming "status" corresponds to "site_status"
                getNodeTextValue(node, "loop_back_0_ip"),
                getNodeTextValue(node, "primary_ip"),
                getNodeTextValue(node, "backup_ip"),
                getNodeTextValue(node, "third_ip"),
                getNodeTextValue(node, "backup_provider"),
                getNodeTextValue(node, "primary_provider"),
                getNodeTextValue(node, "third_provider"),
            };
            csvContent.append(String.join(",", data)).append("\n");
          } catch (Exception e) {
            // Handle exceptions for individual fields
            e.printStackTrace();
          }
        }

        // Set response headers
        response.setContentType("text/csv");
        response.setHeader(
            "Content-Disposition",
            "attachment; filename=output.csv");

        // Write CSV content to response
        response.getWriter().write(csvContent.toString());
        response.getWriter().flush();
      } catch (IOException e) {
        e.printStackTrace();
        // Handle exception
      }
    } catch (IOException e) {
      e.printStackTrace();
      // Handle exception
    }

    return responseBody;
  }

  private String getNodeTextValue(JsonNode node, String fieldName) {
    try {
      JsonNode fieldNode = node.get(fieldName);
      return fieldNode != null ? fieldNode.asText() : "";
    } catch (Exception e) {
      // Handle specific exception for the field if needed
      e.printStackTrace();
      return "";
    }
  }

  @GetMapping("/getStoreDataCsv")
  public void csvProm(HttpServletResponse response) {
    System.out.println("CSV Export Requested...");

    // Print Site_ID and Value[1] for each MetricResult
    try (
        CSVWriter csvWriter = new CSVWriter(new FileWriter("store_data.csv"))) {
      StringBuilder csvContent = new StringBuilder();
      String siteId = new String();
      String store_status = new String();
      String single_circuit = new String();
      String wireless_only = new String();
      String primary_provider = new String();
      String backup_provider = new String();
      String lo1health = new String();
      String lo2health = new String();
      Double lo1value = Double.valueOf(0.0);
      Double lo2value = Double.valueOf(0.0);
      String router = new String();
      String primary_gateway = new String();
      String primary_mac = new String();
      String backup_gateway = new String();
      String backup_mac = new String();
      String primary_packet_loss = new String();
      String primary_min_rtt = new String();
      String primary_avg_rtt = new String();
      String primary_max_rtt = new String();
      String backup_packet_loss = new String();
      String backup_min_rtt = new String();
      String backup_avg_rtt = new String();
      String backup_max_rtt = new String();
      String[] headers = {
          "Site ID",
          "Status",
          "Router",
          "Single-Circuit",
          "Wireless Only",
          "Primary Circuit Status",
          "Loopback 1 Uptime",
          "Primary Provider",
          "Primary Gateway",
          "Primary Mac",
          "Backup Circuit Status",
          "Loopback 2 Uptime",
          "Backup Provider",
          "Backup Gateway",
          "Backup Mac",
          "Primary Packet Loss",
          "Primary Min RTT",
          "Primary Avg RTT",
          "Primary Max RTT",
          "Backup Packet Loss",
          "Backup Min RTT",
          "Backup Avg RTT",
          "Backup Max RTT",
      };
      csvContent.append(String.join(",", headers)).append("\n");

      Iterable<store_data> circuits = store_data_repo.findAll();
      Iterable<provider_circuit> recentCircuit = provider_circuits_repo.getLatestData();

      try {
        for (store_data circuit : circuits) {
          siteId = circuit.getSiteId();
          store_status = circuit.getStatus();
          single_circuit = circuit.getSingle_circuit();
          wireless_only = circuit.getWireless_only();
          primary_provider = circuit.getPrimary_provider();
          backup_provider = circuit.getBackup_provider();
          lo1health = circuit.getPrimary_circuit_status();
          lo2health = circuit.getBackup_circuit_status();
          lo1value = circuit.getLoopback_1();
          lo2value = circuit.getLoopback_2();
          router = circuit.getRouter();

          for (provider_circuit recentData : recentCircuit) {
            if (siteId.equalsIgnoreCase(recentData.getSiteId())) {
              if (recentData.getRouter().equalsIgnoreCase("mikrotik")) {
                primary_gateway = recentData.getModemGtwPort2();
                primary_mac = recentData.getMacPort2();
                backup_gateway = recentData.getModemGtwPort1();
                backup_mac = recentData.getMacPort1();
                primary_packet_loss = recentData.getPacketLossPort2();
                primary_min_rtt = recentData.getMinRttPort2();
                primary_avg_rtt = recentData.getAvgRttPort2();
                primary_max_rtt = recentData.getMaxRttPort2();
                backup_packet_loss = recentData.getPacketLossPort1();
                backup_min_rtt = recentData.getMinRttPort1();
                backup_avg_rtt = recentData.getAvgRttPort1();
                backup_max_rtt = recentData.getMaxRttPort1();
              }
              if (recentData.getRouter().equalsIgnoreCase("ruijie")) {
                primary_gateway = recentData.getModemGtwVlan10();
                primary_mac = recentData.getMacVlan10();
                backup_gateway = recentData.getModemGtwVlan20();
                backup_mac = recentData.getMacCellular();
                primary_packet_loss = recentData.getPacketLossVlan10();
                primary_min_rtt = recentData.getMinRttVlan10();
                primary_avg_rtt = recentData.getAvgRttVlan10();
                primary_max_rtt = recentData.getMaxRttVlan10();
                backup_packet_loss = recentData.getPacketLossCellular();
                backup_min_rtt = recentData.getMinRttCellular();
                backup_avg_rtt = recentData.getAvgRttCellular();
                backup_max_rtt = recentData.getMaxRttCellular();
              }
            }
          }
          String Stringlo1value = Double.toString(lo1value);
          String Stringlo2value = Double.toString(lo2value);
          String[] data = {
              siteId,
              store_status,
              router,
              single_circuit,
              wireless_only,
              lo1health,
              Stringlo1value,
              primary_provider,
              primary_gateway,
              primary_mac,
              lo2health,
              Stringlo2value,
              backup_provider,
              backup_gateway,
              backup_mac,
              primary_packet_loss,
              primary_min_rtt,
              primary_avg_rtt,
              primary_max_rtt,
              backup_packet_loss,
              backup_min_rtt,
              backup_avg_rtt,
              backup_max_rtt,
          };

          csvContent.append(String.join(",", data)).append("\n");
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      response.setContentType("text/csv");

      response.setHeader(
          "Content-Disposition",
          "attachment; filename=store_data.csv");

      response.getWriter().write(csvContent.toString());
      response.getWriter().flush();

      System.out.println("CSV Ready!");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  // public String processJson(String jsonInput) throws Exception {
  // // Step 1: Add Surrounding Braces
  // // jsonInput = "[" + jsonInput + "]";

  // // Step 2: Replace Commas with Colons
  // jsonInput = jsonInput.replaceAll("=", ":");

  // // Step 3: Enclose Keys and Values in Quotes
  // jsonInput = encloseInQuotes(jsonInput);

  // // Use Jackson to convert the processed string to a List of Map
  // ObjectMapper objectMapper = new ObjectMapper();
  // List<Map<String, Object>> jsonData = objectMapper.readValue(jsonInput,
  // new TypeReference<List<Map<String, Object>>>() {
  // });

  // // Now jsonData contains the processed JSON data as a List of Map
  // // Do whatever you need with the data

  // // If needed, convert it back to JSON string
  // String processedJsonString = objectMapper.writeValueAsString(jsonData);

  // return processedJsonString;
  // }

  // private String encloseInQuotes(String input) {
  // // Use regex to enclose keys and values in quotes
  // // This regex handles fields with commas, optional quotes, and optional
  // commas
  // Pattern pattern = Pattern.compile("(\\w+)\\s*=\\s*\"?([^,}]+)\"?,?");
  // Matcher matcher = pattern.matcher(input);

  // StringBuffer result = new StringBuffer();
  // while (matcher.find()) {
  // matcher.appendReplacement(result, "\"" + matcher.group(1) + "\":\"" +
  // matcher.group(2) + "\",");
  // }
  // matcher.appendTail(result);

  // return result.toString();
  // }

  private List<Map<String, Object>> getMockData() {
      List<Map<String, Object>> mockData = new ArrayList<>();
      Map<String, Object> siteData = new HashMap<>();
      siteData.put("site_id", "1");
      siteData.put("name", "Mock Site 1");
      siteData.put("tenant", "Mock Tenant 1");
      siteData.put("address", "123 Mock St");
      siteData.put("network_status", "Active");
      siteData.put("status", "Active");
      siteData.put("loop_back_0_ip", "10.0.0.1");
      siteData.put("primary_ip", "192.168.0.1");
      siteData.put("backup_ip", "192.168.0.2");
      siteData.put("third_ip", "192.168.0.3");
      siteData.put("backup_provider", "Provider A");
      siteData.put("primary_provider", "Provider B");
      siteData.put("third_provider", "Provider C");
      mockData.add(siteData);

      return mockData;
  }
}
