package com.tracker_app.tracker.DataSource_NMS.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker_app.tracker.DataSource_Atis.Repo.atis_repo;
import com.tracker_app.tracker.DataSource_NMS.Entity.circuit;
import com.tracker_app.tracker.DataSource_NMS.Entity.mikrotik;
import com.tracker_app.tracker.DataSource_NMS.Entity.ruijie;
import com.tracker_app.tracker.DataSource_NMS.Entity.store_data;
import com.tracker_app.tracker.DataSource_NMS.Repo.circuit_repo;
import com.tracker_app.tracker.DataSource_NMS.Repo.mikrotik_repo;
import com.tracker_app.tracker.DataSource_NMS.Repo.provider_circuits_mindanao_repo;
import com.tracker_app.tracker.DataSource_NMS.Repo.provider_circuits_today_repo;
import com.tracker_app.tracker.DataSource_NMS.Repo.ruijie_repo;
import com.tracker_app.tracker.DataSource_NMS.Repo.store_count_today_repo;
import com.tracker_app.tracker.DataSource_NMS.Repo.store_data_repo;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class circuit_service {

  // nms
  @Autowired
  private circuit_repo circuit_repo;

  @Autowired
  private atis_repo atis_repo;

  @Autowired
  private store_data_repo store_data_repo;

  @Autowired
  private provider_circuits_today_repo provider_circuits_today_repo;

  @Autowired
  private provider_circuits_mindanao_repo providerCircuitsMindanaoRepo;

  @Autowired
  private store_count_today_repo store_count_today_repo;

  public Double thresholdValue = 0.95;

  // Stores

  public double totalStores = getTotalStoresCount();
  public double temporarilyClosed = getTemporarilyClosedCount();
  public double totalMikrotikStores = getMikrotikStoresCount();
  public double totalRuijiStores = getRuijieStoresCount();

  // Down Store

  public int totalDownStores = getAllDownStoresSiteId().size();
  public int totalDownMikrotik = getMikroktikDownStoresSiteId().size();
  public int totalDownRuiji = getRuijieDownStoresSiteId().size();

  // Problematic Stores

  // Problematic Primary Circuits

  // Problematic Backup Circuits

  public Map<String, Object> countAllNms() {
    Map<String, Object> nms = new HashMap<String, Object>();

    // Problematic store
    int problematicStores0_7d = store_data_repo
      .getProblematicStore0up7Days()
      .size();
    int problematicStoresLess95_7d = store_data_repo
      .getProblematicStore95up7Days(thresholdValue)
      .size();
    int problematicStoresLess95_24hrs = store_data_repo
      .getProblematicStore95up24h(thresholdValue)
      .size();
    // primary
    int problematicPrimary0_7d = store_data_repo
      .getPrimaryCircuit0up7Days()
      .size();
    int problematicPrimary100_7d = store_data_repo
      .getPrimaryCircuit100up7Days()
      .size();
    int problematicPrimaryLess95_7d = store_data_repo
      .getPrimaryCircuit95up7Days(thresholdValue)
      .size();
    int problematicPrimaryLess95_24hrs = store_data_repo
      .getPrimaryCircuit95up24h(thresholdValue)
      .size();
    // backup
    int problematicBackup0_7d = store_data_repo
      .getBackupCircuit0up7Days()
      .size();
    int problematicBackup100_7d = store_data_repo
      .getBackupCircuit100up7Days()
      .size();
    int problematicBackupLess95_7d = store_data_repo
      .getBackupCircuit95up7Days(thresholdValue)
      .size();
    int problematicBackupLess95_24hrs = store_data_repo
      .getBackupCircuit95up24h(thresholdValue)
      .size();

    // problematic stores
    nms.put("problematicStores0_7d", problematicStores0_7d);
    nms.put("problematicStoresLess95_7d", problematicStoresLess95_7d);
    nms.put("problematicStoresLess95_24hrs", problematicStoresLess95_24hrs);
    // problematic primary
    nms.put("problematicPrimary0_7d", problematicPrimary0_7d);
    nms.put("primary100_7d", problematicPrimary100_7d);
    nms.put("problematicPrimaryLess95_7d", problematicPrimaryLess95_7d);
    nms.put("problematicPrimaryLess95_24hrs", problematicPrimaryLess95_24hrs);
    nms.put(
      "primaryCircuitNoInternet",
      store_data_repo.getPrimaryCircuitNoInternetTableList().size()
    );
    nms.put(
      "problematicPrimaryNoProvider",
      store_data_repo.getPrimaryCircuitNoProviderTableList().size()
    );
    // problematic backup
    nms.put("problematicBackup0_7d", problematicBackup0_7d);
    nms.put("backup100_7d", problematicBackup100_7d);
    nms.put("problematicBackupLess95_7d", problematicBackupLess95_7d);
    nms.put("problematicBackupLess95_24hrs", problematicBackupLess95_24hrs);
    nms.put(
      "backupCircuitNoInternet",
      store_data_repo.getBackupCircuitNoInternetTableList().size()
    );
    nms.put(
      "problematicBackupNoProvider",
      store_data_repo.getBackupCircuitNoProviderTableList().size()
    );
    // others
    nms.put("primaryWireless", circuit_repo.getPrimaryWirelessSiteIds().size());

    nms.put("backupWired", circuit_repo.getBackupWiredSiteIds().size());
    nms.put(
      "problematicWirelessOnly",
      store_data_repo.getWirelessOnlyTableList().size()
    );
    nms.put(
      "problematicSingleCircuit",
      store_data_repo.getSingleCircuitTableList().size()
    );
    // store connectivity
    nms.put("dual_circuit_7d", store_data_repo.getDualCircuit7D().size());
    nms.put("primary_only_7d", store_data_repo.getPrimaryOnly7D().size());
    nms.put("backup_only_7d", store_data_repo.getBackupOnly7D().size());
    // store health status
    nms.put("store_healthy_7d", store_data_repo.getHealhyLoopback0_7D().size());
    nms.put("store100_7d", store_data_repo.getLoopback0_100uptime7D().size());
    return nms;
  }

  public Map<String, Object> countPrometheusData() {
    Map<String, Object> prometheusData = new HashMap<String, Object>();
    // Stores
    double totalStores = getTotalStoresCount();
    double temporarilyClosed = getTemporarilyClosedCount();
    double totalMikrotikStores = getMikrotikStoresCount();
    double totalRuijiStores = getRuijieStoresCount();
    // Down Store
    int totalDownStores = getAllDownStoresSiteId().size();
    // int totalDownMikrotik = getMikroktikDownStoresSiteId().size();
    // int totalDownRuiji = getRuijieDownStoresSiteId().size();
    int zabbixDownStores = store_count_today_repo.getZabbixDownStores();
    // stores
    prometheusData.put("totalStores", totalStores);
    prometheusData.put("temporarilyClosed", temporarilyClosed);
    prometheusData.put("totalMikrotikStores", totalMikrotikStores);
    prometheusData.put("totalRuijiStores", totalRuijiStores);

    // down stores
    prometheusData.put("totalDownStores", totalDownStores);
    // prometheusData.put("totalDownRuiji", totalDownRuiji);
    // prometheusData.put("totalDownMikrotik", totalDownMikrotik);
    // Zabbix
    prometheusData.put("zabbixDownStores", zabbixDownStores);

    return prometheusData;
  }

  public Map<String, Object> countMindanao() {
    Map<String, Object> mindanaoStores = new HashMap<String, Object>();

    int allMindanaoStores = providerCircuitsMindanaoRepo
      .getMindanaoList()
      .size();
    int downMindanaoStores = providerCircuitsMindanaoRepo
      .getSpecificMindanaoList(getAllDownStoresSiteId())
      .size();
    int mindanaoDualCircuit = providerCircuitsMindanaoRepo
      .getMindanaoDualCircuit()
      .size();
    int mindanaoPrimaryOnly = providerCircuitsMindanaoRepo
      .getMindanaoPrimaryOnly()
      .size();
    int mindanaoBackupOnly = providerCircuitsMindanaoRepo
      .getMindanaoBackupOnly()
      .size();

    int mindanaoDownStore7D = providerCircuitsMindanaoRepo
      .getDownStore_7D()
      .size();

    int mindanaoProblematic7D = providerCircuitsMindanaoRepo
      .getProblematicStore_7D()
      .size();

    int mindanaoHealthyStore7D = providerCircuitsMindanaoRepo
      .getHealhyStore7D()
      .size();

    int mindanaoExcellent7D = providerCircuitsMindanaoRepo
      .getStore100uptime7D()
      .size();

    mindanaoStores.put("allMindanaoStores", allMindanaoStores);
    mindanaoStores.put("downMindanaoStores", downMindanaoStores);
    // Store Connectivity
    mindanaoStores.put("mindanaoDualCircuit", mindanaoDualCircuit);
    mindanaoStores.put("mindanaoPrimaryOnly", mindanaoPrimaryOnly);
    mindanaoStores.put("mindanaoBackupOnly", mindanaoBackupOnly);
    // Store health Status
    mindanaoStores.put("mindanaoDownStore7D", mindanaoDownStore7D);
    mindanaoStores.put("mindanaoProblematic7D", mindanaoProblematic7D);
    mindanaoStores.put("mindanaoHealthyStore7D", mindanaoHealthyStore7D);
    mindanaoStores.put("mindanaoExcellent7D", mindanaoExcellent7D);
    return mindanaoStores;
  }

  public Map<String, Object> countProviderCircuitsToday() {
    Map<String, Object> mindanaoStores = new HashMap<String, Object>();

    int providerCircuitsToday = provider_circuits_today_repo
      .getProviderCircuitsToday()
      .size();
    int dualCircuit7D = provider_circuits_today_repo.getDualCircuit7D().size();
    int primaryOnly7D = provider_circuits_today_repo.getPrimaryOnly7D().size();
    int backupOnly7D = provider_circuits_today_repo.getBackupOnly7D().size();
    int thirdOnly7D = provider_circuits_today_repo.getThirdOnly7D().size();
    int dualCircuit24H = provider_circuits_today_repo
      .getDualCircuit24H()
      .size();
    int primaryOnly24H = provider_circuits_today_repo
      .getPrimaryOnly24H()
      .size();
    int backupOnly24H = provider_circuits_today_repo.getBackupOnly24H().size();
    int thirdOnly24H = provider_circuits_today_repo.getThirdOnly24H().size();
    // Store Health Status
    int down7d = provider_circuits_today_repo.getDownStore_7D().size();
    int problematic7d = provider_circuits_today_repo
      .getProblematicStore_7D()
      .size();
    int healthy7d = provider_circuits_today_repo.getHealhyStore7D().size();
    int excellenct7d = provider_circuits_today_repo
      .getStore100uptime7D()
      .size();

    int down24h = provider_circuits_today_repo.getDownStore_24H().size();
    int problematic24h = provider_circuits_today_repo
      .getProblematicStore_24H()
      .size();
    int healthy24h = provider_circuits_today_repo.getHealhyStore24H().size();
    int excellenct24h = provider_circuits_today_repo
      .getStore100uptime24H()
      .size();

    String timeStamp = provider_circuits_today_repo.getLatestTimeStampPCT();

    mindanaoStores.put("providerCircuitsToday", providerCircuitsToday);
    mindanaoStores.put("dualCircuit7D", dualCircuit7D);
    mindanaoStores.put("primaryOnly7D", primaryOnly7D);
    mindanaoStores.put("backupOnly7D", backupOnly7D);
    mindanaoStores.put("thirdOnly7D", thirdOnly7D);
    mindanaoStores.put("dualCircuit24H", dualCircuit24H);
    mindanaoStores.put("primaryOnly24H", primaryOnly24H);
    mindanaoStores.put("backupOnly24H", backupOnly24H);
    mindanaoStores.put("thirdOnly24H", thirdOnly24H);
    mindanaoStores.put("down7d", down7d);
    mindanaoStores.put("problematic7d", problematic7d);
    mindanaoStores.put("healthy7d", healthy7d);
    mindanaoStores.put("excellent7d", excellenct7d);
    mindanaoStores.put("down24h", down24h);
    mindanaoStores.put("problematic24h", problematic24h);
    mindanaoStores.put("healthy24h", healthy24h);
    mindanaoStores.put("excellent24h", excellenct24h);
    mindanaoStores.put("timeStamp", timeStamp);

    return mindanaoStores;
  }

  String allStoreQuery = "lo_status{job=\"loopback 0\", site_tenant=\"PSC\"}";
  String mikrotikQuery =
    "lo_status{job=\"loopback 0\", site_status='active', site_tenant=\"PSC\", instance=~\"10\\\\.9\\\\.(3[2-9]|4[0-9]|5[0-9]|6[0-9]|7[0-9]|8[0-9]|9[0-9]|1[0-9][0-9])\\\\..*|10\\\\.10\\\\..*\"}";
  String ruijieQuery =
    "lo_status{job=\"loopback 0\", site_status='active', site_tenant=\"PSC\", instance=~\"10\\\\.9\\\\.(0|1?[0-9]|2?[0-9]|30| 31)\\\\..*\"}";

  Double thresholdPercentage = 0.95;

  // Stores
  public double getTotalStoresCount() {
    return getApiValue("count(" + allStoreQuery + ") OR vector(0)");
  }

  public double getTemporarilyClosedCount() {
    return getApiValue(
      "count(lo_status{job=\"loopback 0\", site_status!='active', site_tenant=\"PSC\"}) OR vector(0)"
    );
  }

  public double getMikrotikStoresCount() {
    return getApiValue("count(" + mikrotikQuery + ")");
  }

  public double getRuijieStoresCount() {
    return getApiValue("count(" + ruijieQuery + ")");
  }

  // List of Store Site Ids
  public List<String> getAllDownStoresSiteId() {
    return getSiteIds(
      "lo_status{job=\"loopback 0\",site_status=\"active\",site_tenant=\"PSC\"} == 0"
    );
  }

  public List<String> getAllDownPrimarySiteId() {
    return getSiteIds(
      "lo_status{job=\"loopback 1\",site_status=\"active\",site_tenant=\"PSC\"} == 0"
    );
  }

  public List<String> getAllDownBackupSiteId() {
    return getSiteIds(
      "lo_status{job=\"loopback 2\",site_status=\"active\",site_tenant=\"PSC\"} == 0"
    );
  }

  public List<String> getMikroktikDownStoresSiteId() {
    return getSiteIds(mikrotikQuery + " == 0");
  }

  public List<String> getRuijieDownStoresSiteId() {
    return getSiteIds(ruijieQuery + " == 0");
  }

  // Problematic Stores List
  public List<String> problematic0_7dList() {
    return getSiteIds(
      "uptime_availability{job=\"loopback 0\", site_status=\"active\", site_tenant=\"PSC\"} == 0"
    );
  }

  public List<String> problematic95_7dList() {
    return getSiteIds(
      "uptime_availability{job=\"loopback 0\", site_status=\"active\", site_tenant=\"PSC\"} < " +
      thresholdPercentage
    );
  }

  public List<String> problematic95_24hList() {
    return getSiteIds(
      "uptime_availability_24hrs{job=\"loopback 0\", site_status=\"active\", site_tenant=\"PSC\"} < " +
      thresholdPercentage
    );
  }

  // Primary Store List
  public List<String> primary0_7dList() {
    return getSiteIds(
      "uptime_availability{job=\"loopback 1\", site_status=\"active\", site_tenant=\"PSC\"} == 0"
    );
  }

  public List<String> primary95_7dList() {
    return getSiteIds(
      "uptime_availability{job=\"loopback 1\", site_status=\"active\", site_tenant=\"PSC\"} < " +
      thresholdPercentage
    );
  }

  public List<String> primary95_24hList() {
    return getSiteIds(
      "uptime_availability_24hrs{job=\"loopback 1\", site_status=\"active\", site_tenant=\"PSC\"} < " +
      thresholdPercentage
    );
  }

  // Backup Store List
  public List<String> backup0_7dList() {
    return getSiteIds(
      "uptime_availability{job=\"loopback 2\", site_status=\"active\", site_tenant=\"PSC\"} == 0"
    );
  }

  public List<String> backup95_7dList() {
    return getSiteIds(
      "uptime_availability{job=\"loopback 2\", site_status=\"active\", site_tenant=\"PSC\"} < " +
      thresholdPercentage
    );
  }

  public List<String> backup95_24hList() {
    return getSiteIds(
      "uptime_availability_24hrs{job=\"loopback 2\", site_status=\"active\", site_tenant=\"PSC\"} < " +
      thresholdPercentage
    );
  }

  public String getLoopbackIp(String storeId, Integer loopbackNum) {
    return getIpInstanceProm(
      "lo_status{job=\"loopback " +
      loopbackNum +
      "\", site_id= \"" +
      storeId +
      "\"}"
    );
  }

  // Reusable Function
  public double getApiValue(String apiQuryValue) {
    HttpClient httpClient = HttpClient.newHttpClient();

    try {
      String encodedQuery = URLEncoder.encode(
        apiQuryValue,
        StandardCharsets.UTF_8
      );
      HttpRequest request = HttpRequest
        .newBuilder()
        .uri(
          new URI(
            "https://prometheus.apolloglobal.net/api/v1/query?query=" +
            encodedQuery
          )
        )
        .GET()
        .build();

      HttpResponse<String> response = httpClient.send(
        request,
        HttpResponse.BodyHandlers.ofString()
      );

      // Parse the JSON response

      try {
        JSONObject jsonResponse = new JSONObject(response.body());
        JSONArray resultArray = jsonResponse
          .getJSONObject("data")
          .getJSONArray("result");

        int lastIndex = resultArray.length() - 1;

        // Check if the array is not empty before accessing the last element
        if (lastIndex >= 0) {
          String value = resultArray
            .getJSONObject(lastIndex)
            .getJSONArray("value")
            .getString(1);

          double newVal = Double.parseDouble(value);
          return Math.round(newVal * 100.0) / 100.0;
        } else {
          // Handle the case where the "result" array is empty
          return 0;
        }
      } catch (JSONException e) {
        // Handle the exception or rethrow it as an unchecked exception
        e.printStackTrace();
        return 0;
      }
    } catch (IOException | InterruptedException | URISyntaxException e) {
      // Handle exceptions
      e.printStackTrace(); // You might want to log the exception or perform some other error handling
      return 0; // or throw an exception, depending on your requirements
    }
  }

  public String getIpInstanceProm(String apiQuryValue) {
    HttpClient httpClient = HttpClient.newHttpClient();

    try {
      String encodedQuery = URLEncoder.encode(
        apiQuryValue,
        StandardCharsets.UTF_8
      );
      HttpRequest request = HttpRequest
        .newBuilder()
        .uri(
          new URI(
            "https://prometheus.apolloglobal.net/api/v1/query?query=" +
            encodedQuery
          )
        )
        .GET()
        .build();

      HttpResponse<String> response = httpClient.send(
        request,
        HttpResponse.BodyHandlers.ofString()
      );

      // Parse the JSON response

      try {
        JSONObject jsonResponse = new JSONObject(response.body());
        JSONArray resultArray = jsonResponse
          .getJSONObject("data")
          .getJSONArray("result");

        // Check if the array is not empty before accessing the last element
        if (resultArray.length() > 0) {
          JSONObject resultObject = resultArray.getJSONObject(0);
          JSONObject metricObject = resultObject.getJSONObject("metric");
          return metricObject.getString("instance");
        } else {
          // Handle the case where the "result" array is empty
          return "";
        }
      } catch (JSONException e) {
        // Handle the exception or rethrow it as an unchecked exception
        e.printStackTrace();
        return "";
      }
    } catch (IOException | InterruptedException | URISyntaxException e) {
      // Handle exceptions
      e.printStackTrace(); // You might want to log the exception or perform some other error handling
      return ""; // or throw an exception, depending on your requirements
    }
  }

  public List<String> getSiteIds(String apiQuery) {
    Set<String> uniqueSiteIds = new HashSet<>();

    try {
      String encodedQuery = URLEncoder.encode(apiQuery, StandardCharsets.UTF_8);
      HttpRequest request = HttpRequest
        .newBuilder()
        .uri(
          new URI(
            "https://prometheus.apolloglobal.net/api/v1/query?query=" +
            encodedQuery
          )
        )
        .GET()
        .build();

      HttpResponse<String> response = HttpClient
        .newHttpClient()
        .send(request, HttpResponse.BodyHandlers.ofString());

      JSONObject jsonResponse = new JSONObject(response.body());
      JSONArray resultArray = jsonResponse
        .getJSONObject("data")
        .getJSONArray("result");

      // Extract unique "site_id" values and add them to the set
      for (int i = 0; i < resultArray.length(); i++) {
        JSONObject resultObject = resultArray.getJSONObject(i);
        JSONObject metricObject = resultObject.getJSONObject("metric");
        String siteId = metricObject.getString("site_id");
        uniqueSiteIds.add(siteId);
      }

      // Convert the set to a list before returning
      return new ArrayList<>(uniqueSiteIds);
    } catch (
      IOException | InterruptedException | URISyntaxException | JSONException e
    ) {
      System.out.println("An error occurred: " + e.getMessage());
    }

    return new ArrayList<>();
  }

  public Map<String, Object> getSiteDetailsPromNew(String siteId) {
    Map<String, Object> site = new ConcurrentHashMap<>();

    site.put("site_id", siteId);
    try {
      double loopback0 = getApiValue(
        "uptime_availability{job=\"loopback 0\", site_id=\"" + siteId + "\"} "
      );
      double loopback1 = getApiValue(
        "uptime_availability{job=\"loopback 1\", site_id=\"" + siteId + "\"} "
      );
      double loopback2 = getApiValue(
        "uptime_availability{job=\"loopback 2\", site_id=\"" + siteId + "\"} "
      );
      double loopback0_24h = getApiValue(
        "uptime_availability_24hrs{job=\"loopback 0\", site_id=\"" +
        siteId +
        "\"} "
      );
      double loopback1_24h = getApiValue(
        "uptime_availability_24hrs{job=\"loopback 1\", site_id=\"" +
        siteId +
        "\"} "
      );
      double loopback2_24h = getApiValue(
        "uptime_availability_24hrs{job=\"loopback 2\", site_id=\"" +
        siteId +
        "\"} "
      );
      String storeDescription = atis_repo.findNameBySiteId(siteId);

      site.put("store_description", storeDescription);
      site.put("loopback_0_24h", loopback0_24h);
      site.put("loopback_1_24h", loopback1_24h);
      site.put("loopback_2_24h", loopback2_24h);
      site.put("loopback_0", loopback0);
      site.put("loopback_1", loopback1);
      site.put("loopback_2", loopback2);
      try {
        if (store_data_repo.existsBySiteId(siteId)) {
          List<Object[]> circuitAndProviderDetails = store_data_repo.findCircuitAndProviderDetailsBySiteId(
            siteId
          );

          Object[] result = circuitAndProviderDetails.get(0);
          String primaryCircuitStatus = (String) result[0];
          String primaryCircuitProvider = (String) result[1];
          String backupCircuitStatus = (String) result[2];
          String backupCircuitProvider = (String) result[3];

          site.put("primary_provider", primaryCircuitProvider);
          site.put("primary_circuit_status", primaryCircuitStatus);
          site.put("backup_provider", backupCircuitProvider);
          site.put("backup_circuit_status", backupCircuitStatus);
        } else {
          System.out.println(siteId + " Store Id not in DB!");
          site.put("primary_provider", "Not in DB");
          site.put("primary_circuit_status", "Not in DB");
          site.put("backup_provider", "Not in DB");
          site.put("backup_circuit_status", "Not in DB");
        }
      } catch (Exception e) {
        System.out.println(siteId + " has null values in DB! " + e);
        site.put("primary_provider", null);
        site.put("primary_circuit_status", null);
        site.put("backup_provider", null);
        site.put("backup_circuit_status", null);
      }
    } catch (Exception e) {
      System.out.println(
        "An error occurred in Get Details from Prom: " +
        siteId +
        " Message: " +
        e
      );
    }

    return site;
  }

  public List<Map<String, Object>> getListofSiteDetails(
    List<String> listSiteId
  ) {
    return listSiteId
      .parallelStream()
      .map(this::getSiteDetailsPromNew)
      .collect(Collectors.toList());
  }

  public List<Map<String, Object>> getStoreDataForSiteIds(
    List<String> siteIds
  ) {
    // System.out.println("Number of SiteIds: " + siteIds.size());

    // Fetch existing store data
    List<Map<String, Object>> existingStoreData = store_data_repo.getStoreDataForSiteIds(
      siteIds
    );

    // System.out.println("Number of Store Data: " + existingStoreData.size());

    // Create a new list to store the result
    List<Map<String, Object>> resultList = new ArrayList<>(existingStoreData);

    try {
      // Iterate over siteIds and update resultList
      siteIds.forEach(siteId -> {
        boolean found = existingStoreData
          .stream()
          .anyMatch(data -> siteId.equals(data.get("site_id")));
        if (!found) {
          System.out.println("Site ID " + siteId + " not Found!");
          Map<String, Object> noDataEntry = new HashMap<>();
          noDataEntry.put("site_id", siteId);
          noDataEntry.put("store_description", "NO DATA");
          noDataEntry.put("primary_provider", "NO DATA");
          noDataEntry.put("primary_circuit_status", "NO DATA");
          noDataEntry.put("backup_provider", "NO DATA");
          noDataEntry.put("backup_circuit_status", "NO DATA");
          noDataEntry.put("loopback_0_24h", "NO DATA");
          noDataEntry.put("loopback_1_24h", "NO DATA");
          noDataEntry.put("loopback_2_24h", "NO DATA");
          noDataEntry.put("loopback_0", "NO DATA");
          noDataEntry.put("loopback_1", "NO DATA");
          noDataEntry.put("loopback_2", "NO DATA");
          // resultList.add(noDataEntry);
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }

    return resultList;
  }
}
