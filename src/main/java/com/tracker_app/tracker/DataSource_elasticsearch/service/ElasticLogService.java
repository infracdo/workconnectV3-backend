package com.tracker_app.tracker.DataSource_elasticsearch.service;

import com.tracker_app.tracker.DataSource_elasticsearch.model.ElasticLog;
import com.tracker_app.tracker.DataSource_elasticsearch.repo.ElasticLogRepo;
import com.tracker_app.tracker.Helper.AuthService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ElasticLogService {

  @Autowired
  private ElasticLogRepo elasticLogRepo;

  @Autowired
  private AuthService authService;

  public Iterable<ElasticLog> getWorkConnectv2Log() {
    return elasticLogRepo.findAll();
  }

  private void insertWorkConnectv2Log(ElasticLog elasticLog) {
    System.out.println(elasticLog);
    elasticLogRepo.save(elasticLog);
  }

  public ElasticLog updateWorkConnectv2Log(ElasticLog elasticLog, String id) {
    ElasticLog elastic = elasticLogRepo.findById(id).get();
    elastic.setStatus(elasticLog.getStatus());
    return elastic;
  }

  public void deleteWorkConnectv2Log(String id) {
    elasticLogRepo.deleteById(id);
  }

  public void saveWorkConnectLog(
    String auth,
    String action,
    String status,
    String service,
    String payload,
    String clientIp
  ) {
    ZoneId zoneId = ZoneId.of("Asia/Manila");
    long timestampMilliSec = Instant
      .now()
      .atZone(zoneId)
      .toInstant()
      .toEpochMilli();

    String user = authService.getUser(auth);
    System.out.println(user);
    // String name = authService.getName(auth);
    List<String> roles = authService.getRoles(auth);

    authService.isTokenValid(auth);

    ElasticLog log = new ElasticLog();
    log.setTimestampMilliSec(timestampMilliSec);
    // log.setTimestamp(timeStamp);
    log.setUser(user);
    // log.setName(name);
    log.setRoleGroup(roles);
    log.setTenant("PSC");
    // user defined
    log.setClientIp(clientIp);
    log.setStatus(status);
    log.setService(service);
    log.setPayload(payload);
    log.setAction(action);
    insertWorkConnectv2Log(log);
  }

  // public List<Map<String, Object>> elasticWorkconnectLogs() {
  //   Iterable<ElasticLog> logs = elasticLogRepo.findAll();
  //   List<Map<String, Object>> logList = new ArrayList<>();
  //   logs.forEach(log -> {
  //     Map<String, Object> logMap = new HashMap<>();
  //     logMap.put("id", log.getId());
  //     // logMap.put("timestamp", log.getTimestamp());
  //     logMap.put("timestamp", log.getTimestampMilliSec());
  //     logMap.put("status", log.getStatus());
  //     logMap.put("user", log.getUser());
  //     logMap.put("roleGroup", log.getRoleGroup());
  //     logMap.put("tenant", log.getTenant());
  //     logMap.put("service", log.getService());
  //     logMap.put("payload", log.getPayload());
  //     logMap.put("action", log.getAction());
  //     logList.add(logMap);
  //   });
  //   // Return the list of maps
  //   return logList;
  // }
  public List<Map<String, Object>> elasticWorkconnectLogs() {
    // Iterable<ElasticLog> logs = elasticLogRepo.findAll();
    List<Map<String, Object>> logList = new ArrayList<>();
    Pageable pageable = PageRequest.of(0, 100000); // Adjust the page size as needed
    Page<ElasticLog> logsPage = elasticLogRepo.findAll(pageable);

    logsPage.forEach(log -> {
      Map<String, Object> logMap = new HashMap<>();
      logMap.put("id", log.getId());
      // logMap.put("timestamp", log.getTimestamp());
      logMap.put("timestampMilliSec", log.getTimestampMilliSec());
      logMap.put("status", log.getStatus());
      logMap.put("user", log.getUser());
      logMap.put("roleGroup", log.getRoleGroup());
      logMap.put("tenant", log.getTenant());
      logMap.put("service", log.getService());
      logMap.put("payload", log.getPayload());
      logMap.put("action", log.getAction());
      logMap.put("clientIp", log.getClientIp());
      logList.add(logMap);
    });
    return logList;
  }
}
