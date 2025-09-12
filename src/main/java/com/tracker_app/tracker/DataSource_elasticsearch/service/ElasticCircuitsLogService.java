package com.tracker_app.tracker.DataSource_elasticsearch.service;

import com.tracker_app.tracker.DataSource_elasticsearch.model.ElasticCircuitsLog;
import com.tracker_app.tracker.DataSource_elasticsearch.model.ElasticLog;
import com.tracker_app.tracker.DataSource_elasticsearch.repo.ElasticCircuitsLogRepo;
import com.tracker_app.tracker.Helper.AuthService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class ElasticCircuitsLogService {

  @Autowired
  private ElasticCircuitsLogRepo elasticCircuitsLogRepo;

  @Autowired
  private AuthService authService;

  PageRequest pageable = PageRequest.of(0, 200000);

  public List<Map<String, Object>> elasticCircuitsLogsById(String siteId) {
    Iterable<ElasticCircuitsLog> logs = elasticCircuitsLogRepo.findBySiteID(siteId);
    List<Map<String, Object>> logList = new ArrayList<>();
    logs.forEach(log -> {
      Map<String, Object> logMap = new HashMap<>();
      logMap.put("id", log.getId());
      logMap.put("timestamp", log.getTimestamp());
      // logMap.put("timestamp",
      // Instant.ofEpochMilli(log.getTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime());
      logMap.put("router", log.getRouter());
      logMap.put("siteID", log.getSiteID());
      logMap.put("mac", log.getMac());
      logMap.put("macStatus", log.getMac_status());
      logMap.put("IMSI", log.getIMSI());
      logMap.put("provider", log.getProvider());
      logMap.put("providerStatus", log.getProvider_status());
      logMap.put("publicIp", log.getPublic_IP());
      logMap.put("port", log.getPort());
      logMap.put("previous", log.getPrevious());
      logList.add(logMap);
    });
    return logList;
  }

  public List<Map<String, Object>> elasticCircuitsLogsProviderById(String siteId) {
    Iterable<ElasticCircuitsLog> logs = elasticCircuitsLogRepo.findBySiteIdProvider(siteId, pageable);
    List<Map<String, Object>> logList = new ArrayList<>();
    logs.forEach(log -> {
      Map<String, Object> logMap = new HashMap<>();
      logMap.put("id", log.getId());
      logMap.put("timestamp", log.getTimestamp());
      // logMap.put("timestamp",
      // Instant.ofEpochMilli(log.getTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime());
      logMap.put("router", log.getRouter());
      logMap.put("siteID", log.getSiteID());
      logMap.put("provider", log.getProvider());
      logMap.put("providerStatus", log.getProvider_status());
      logMap.put("publicIp", log.getPublic_IP());
      logMap.put("port", log.getPort());
      logMap.put("previous", log.getPrevious());
      logList.add(logMap);
    });
    return logList;
  }

  public List<Map<String, Object>> elasticCircuitsLogsProvider() {
    Iterable<ElasticCircuitsLog> logs = elasticCircuitsLogRepo.findProvider(pageable);
    List<Map<String, Object>> logList = new ArrayList<>();
    logs.forEach(log -> {
      Map<String, Object> logMap = new HashMap<>();
      logMap.put("id", log.getId());
      logMap.put("timestamp", log.getTimestamp());
      // logMap.put("timestamp",
      // Instant.ofEpochMilli(log.getTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime());
      logMap.put("router", log.getRouter());
      logMap.put("siteID", log.getSiteID());
      logMap.put("provider", log.getProvider());
      logMap.put("providerStatus", log.getProvider_status());
      logMap.put("publicIp", log.getPublic_IP());
      logMap.put("port", log.getPort());
      logMap.put("previous", log.getPrevious());
      logList.add(logMap);
    });
    return logList;
  }

  public List<Map<String, Object>> elasticCircuitsLogsMacById(String siteId) {
    Iterable<ElasticCircuitsLog> logs = elasticCircuitsLogRepo.findBySiteIdMac(siteId, pageable);
    List<Map<String, Object>> logList = new ArrayList<>();
    logs.forEach(log -> {
      Map<String, Object> logMap = new HashMap<>();
      logMap.put("id", log.getId());
      logMap.put("timestamp", log.getTimestamp());
      // logMap.put("timestamp",
      // Instant.ofEpochMilli(log.getTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime());
      logMap.put("router", log.getRouter());
      logMap.put("siteID", log.getSiteID());
      logMap.put("mac", log.getMac());
      logMap.put("macStatus", log.getMac_status());
      logMap.put("port", log.getPort());
      logMap.put("previous", log.getPrevious());
      logList.add(logMap);
    });
    return logList;
  }

  public List<Map<String, Object>> elasticCircuitsLogsMac() {
    Iterable<ElasticCircuitsLog> logs = elasticCircuitsLogRepo.findMac(pageable);
    List<Map<String, Object>> logList = new ArrayList<>();
    logs.forEach(log -> {
      Map<String, Object> logMap = new HashMap<>();
      logMap.put("id", log.getId());
      logMap.put("timestamp", log.getTimestamp());
      // logMap.put("timestamp",
      // Instant.ofEpochMilli(log.getTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime());
      logMap.put("router", log.getRouter());
      logMap.put("siteID", log.getSiteID());
      logMap.put("mac", log.getMac());
      logMap.put("macStatus", log.getMac_status());
      logMap.put("port", log.getPort());
      logMap.put("previous", log.getPrevious());
      logList.add(logMap);
    });
    return logList;
  }

  public List<Map<String, Object>> elasticCircuitsLogsImsiById(String siteId) {
    Iterable<ElasticCircuitsLog> logs = elasticCircuitsLogRepo.findBySiteIdNonBlankImsi(siteId, pageable);
    List<Map<String, Object>> logList = new ArrayList<>();
    logs.forEach(log -> {
      Map<String, Object> logMap = new HashMap<>();
      logMap.put("id", log.getId());
      logMap.put("timestamp", log.getTimestamp());
      // logMap.put("timestamp",
      // Instant.ofEpochMilli(log.getTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime());
      logMap.put("router", log.getRouter());
      logMap.put("siteID", log.getSiteID());
      logMap.put("imsi", log.getIMSI());
      logMap.put("macStatus", log.getMac_status());
      logMap.put("port", log.getPort());
      logMap.put("previous", log.getPrevious());
      logList.add(logMap);
    });
    return logList;
  }

  public List<Map<String, Object>> elasticCircuitsLogsImsi() {
    Iterable<ElasticCircuitsLog> logs = elasticCircuitsLogRepo.findNonBlankImsi(pageable);
    List<Map<String, Object>> logList = new ArrayList<>();
    logs.forEach(log -> {
      Map<String, Object> logMap = new HashMap<>();
      logMap.put("id", log.getId());
      logMap.put("timestamp", log.getTimestamp());
      // logMap.put("timestamp",
      // Instant.ofEpochMilli(log.getTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime());
      logMap.put("router", log.getRouter());
      logMap.put("siteID", log.getSiteID());
      logMap.put("imsi", log.getIMSI());
      logMap.put("macStatus", log.getMac_status());
      logMap.put("port", log.getPort());
      logMap.put("previous", log.getPrevious());
      logList.add(logMap);
    });
    return logList;
  }

  public List<Map<String, Object>> elasticCircuitsLogs() {
    Iterable<ElasticCircuitsLog> logs = elasticCircuitsLogRepo.findAll();
    List<Map<String, Object>> logList = new ArrayList<>();
    logs.forEach(log -> {
      Map<String, Object> logMap = new HashMap<>();
      logMap.put("id", log.getId());
      logMap.put("timestamp", log.getTimestamp());
      // logMap.put("timestamp",
      // Instant.ofEpochMilli(log.getTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime());
      logMap.put("router", log.getRouter());
      logMap.put("siteID", log.getSiteID());
      logMap.put("mac", log.getMac());
      logMap.put("macStatus", log.getMac_status());
      logMap.put("IMSI", log.getIMSI());
      logMap.put("provider", log.getProvider());
      logMap.put("providerStatus", log.getProvider_status());
      logMap.put("publicIp", log.getPublic_IP());
      logMap.put("port", log.getPort());
      logMap.put("previous", log.getPrevious());
      logList.add(logMap);
    });
    return logList;
  }

  public List<Map<String, Object>> getCircuitsMacLogTimePeriod(
      String fromDate, String toDate) throws JSONException, ParseException {
    try {

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));

      long fromTimestamp = dateFormat.parse(fromDate + " 00:00:00").getTime();
      long toTimestamp = dateFormat.parse(toDate + " 23:59:59").getTime();
      long logTimestamp = 0;
      System.out.println(fromTimestamp + " " + toTimestamp);

      List<Map<String, Object>> filteredLogs = new ArrayList<>();
      Iterable<ElasticCircuitsLog> allLogs = elasticCircuitsLogRepo.findMac(pageable);
      for (ElasticCircuitsLog log : allLogs) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String logTimestampCircuit = log.getTimestamp();
        try {
          Date date = inputFormat.parse(logTimestampCircuit);
          logTimestampCircuit = outputFormat.format(date);
          logTimestamp = dateFormat.parse(logTimestampCircuit).getTime();
        } catch (ParseException e) {
          e.printStackTrace();
        }

        // System.out.println(logTimestamp);
        if (logTimestamp >= fromTimestamp && logTimestamp <= toTimestamp) {
          // Convert ElasticLog object to a Map<String, Object>
          Map<String, Object> logMap = new HashMap<>();
          logMap.put("id", log.getId());
          logMap.put("timestamp", log.getTimestamp());
          logMap.put("router", log.getRouter());
          logMap.put("siteID", log.getSiteID());
          logMap.put("mac", log.getMac());
          logMap.put("macStatus", log.getMac_status());
          logMap.put("port", log.getPort());
          logMap.put("previous", log.getPrevious());
          filteredLogs.add(logMap);
        }
      }
      return filteredLogs;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<Map<String, Object>> getCircuitsProvLogTimePeriod(
      String fromDate, String toDate) throws JSONException, ParseException {
    try {

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));

      long fromTimestamp = dateFormat.parse(fromDate + " 00:00:00").getTime();
      long toTimestamp = dateFormat.parse(toDate + " 23:59:59").getTime();
      long logTimestamp = 0;
      System.out.println(fromTimestamp + " " + toTimestamp);

      List<Map<String, Object>> filteredLogs = new ArrayList<>();
      Iterable<ElasticCircuitsLog> allLogs = elasticCircuitsLogRepo.findProvider(pageable);
      for (ElasticCircuitsLog log : allLogs) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String logTimestampCircuit = log.getTimestamp();
        try {
          Date date = inputFormat.parse(logTimestampCircuit);
          logTimestampCircuit = outputFormat.format(date);
          logTimestamp = dateFormat.parse(logTimestampCircuit).getTime();
        } catch (ParseException e) {
          e.printStackTrace();
        }

        // System.out.println(logTimestamp);
        if (logTimestamp >= fromTimestamp && logTimestamp <= toTimestamp) {
          // Convert ElasticLog object to a Map<String, Object>
          Map<String, Object> logMap = new HashMap<>();
          logMap.put("id", log.getId());
          logMap.put("timestamp", log.getTimestamp());
          logMap.put("router", log.getRouter());
          logMap.put("siteID", log.getSiteID());
          logMap.put("mac", log.getMac());
          logMap.put("macStatus", log.getMac_status());
          logMap.put("port", log.getPort());
          logMap.put("previous", log.getPrevious());
          filteredLogs.add(logMap);
        }
      }
      return filteredLogs;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<Map<String, Object>> getCircuitsImsiLogTimePeriod(
      String fromDate, String toDate) throws JSONException, ParseException {
    try {

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));

      long fromTimestamp = dateFormat.parse(fromDate + " 00:00:00").getTime();
      long toTimestamp = dateFormat.parse(toDate + " 23:59:59").getTime();
      long logTimestamp = 0;
      System.out.println(fromTimestamp + " " + toTimestamp);

      List<Map<String, Object>> filteredLogs = new ArrayList<>();
      Iterable<ElasticCircuitsLog> allLogs = elasticCircuitsLogRepo.findNonBlankImsi(pageable);
      for (ElasticCircuitsLog log : allLogs) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String logTimestampCircuit = log.getTimestamp();
        try {
          Date date = inputFormat.parse(logTimestampCircuit);
          logTimestampCircuit = outputFormat.format(date);
          logTimestamp = dateFormat.parse(logTimestampCircuit).getTime();
        } catch (ParseException e) {
          e.printStackTrace();
        }

        // System.out.println(logTimestamp);
        if (logTimestamp >= fromTimestamp && logTimestamp <= toTimestamp) {
          // Convert ElasticLog object to a Map<String, Object>
          Map<String, Object> logMap = new HashMap<>();
          logMap.put("id", log.getId());
          logMap.put("timestamp", log.getTimestamp());
          logMap.put("router", log.getRouter());
          logMap.put("siteID", log.getSiteID());
          logMap.put("mac", log.getMac());
          logMap.put("macStatus", log.getMac_status());
          logMap.put("port", log.getPort());
          logMap.put("previous", log.getPrevious());
          filteredLogs.add(logMap);
        }
      }
      return filteredLogs;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  // public List<Map<String, Object>> elasticCircuitsLogsById(String siteId) {
  // Iterable<ElasticCircuitsLog> logs =
  // elasticCircuitsLogRepo.findBySiteID(siteId);
  // List<Map<String, Object>> logList = new ArrayList<>();
  // logs.forEach(log -> {
  // Map<String, Object> logMap = new HashMap<>();
  // logMap.put("id", log.getId());
  // logMap.put("timestamp", log.getTimestamp());
  // // logMap.put("timestamp",
  // //
  // Instant.ofEpochMilli(log.getTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime());
  // logMap.put("router", log.getRouter());
  // logMap.put("siteID", log.getSiteID());
  // logMap.put("mac1", log.getMac1());
  // logMap.put("mac1Status", log.getMac1_status());
  // logMap.put("mac2", log.getMac2());
  // logMap.put("mac2Status", log.getMac2_status());
  // logMap.put("IMSI", log.getIMSI());
  // logMap.put("IMSIStatus", log.getIMSI_status());
  // logMap.put("provider1", log.getProvider_1());
  // logMap.put("provider1Status", log.getProvider_1_status());
  // logMap.put("provider2", log.getProvider_2());
  // logMap.put("provider2Status", log.getProvider_2_status());
  // logMap.put("providerCellular", log.getProvider_Cellular());
  // logMap.put("providerCellularStatus", log.getProvider_Cellular_status());
  // logMap.put("publicIp1", log.getPublic_IP_1());
  // logMap.put("publicIp2", log.getPublic_IP_2());
  // logMap.put("publicIpCellular", log.getPublic_IP_Cellular());
  // logList.add(logMap);
  // });
  // return logList;
  // }

  // public List<Map<String, Object>> elasticCircuitsLogs() {
  // Iterable<ElasticCircuitsLog> logs = elasticCircuitsLogRepo.findAll();
  // List<Map<String, Object>> logList = new ArrayList<>();
  // logs.forEach(log -> {
  // Map<String, Object> logMap = new HashMap<>();
  // logMap.put("id", log.getId());
  // logMap.put("timestamp", log.getTimestamp());
  // // logMap.put("timestamp",
  // //
  // Instant.ofEpochMilli(log.getTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime());
  // logMap.put("router", log.getRouter());
  // logMap.put("siteID", log.getSiteID());
  // logMap.put("mac1", log.getMac1());
  // logMap.put("mac1Status", log.getMac1_status());
  // logMap.put("mac2", log.getMac2());
  // logMap.put("mac2Status", log.getMac2_status());
  // logMap.put("IMSI", log.getIMSI());
  // logMap.put("IMSIStatus", log.getIMSI_status());
  // logMap.put("provider1", log.getProvider_1());
  // logMap.put("provider1Status", log.getProvider_1_status());
  // logMap.put("provider2", log.getProvider_2());
  // logMap.put("provider2Status", log.getProvider_2_status());
  // logMap.put("providerCellular", log.getProvider_Cellular());
  // logMap.put("providerCellularStatus", log.getProvider_Cellular_status());
  // logMap.put("publicIp1", log.getPublic_IP_1());
  // logMap.put("publicIp2", log.getPublic_IP_2());
  // logMap.put("publicIpCellular", log.getPublic_IP_Cellular());
  // logList.add(logMap);
  // });
  // return logList;
  // }

}
