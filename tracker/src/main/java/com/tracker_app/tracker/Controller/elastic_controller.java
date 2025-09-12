// package com.tracker_app.tracker.Controller;

// import com.tracker_app.tracker.DataSource_elasticsearch.helper.ElasticSearchQueryBuilder;
// import com.tracker_app.tracker.DataSource_elasticsearch.model.ElasticLog;
// import com.tracker_app.tracker.DataSource_elasticsearch.repo.ElasticLogRepo;
// import com.tracker_app.tracker.DataSource_elasticsearch.service.ElasticCircuitsLogService;
// import com.tracker_app.tracker.DataSource_elasticsearch.service.ElasticLogService;
// import com.tracker_app.tracker.Helper.AuthService;
// import java.text.ParseException;
// import java.text.SimpleDateFormat;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.TimeZone;
// import org.elasticsearch.index.query.QueryBuilder;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.configurationprocessor.json.JSONException;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestHeader;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// @CrossOrigin(origins = "*")
// @RestController
// @RequestMapping(path = "/api/elastic")
// public class elastic_controller {

//   @Autowired
//   private ElasticLogService elasticLogService;

//   @Autowired
//   private ElasticCircuitsLogService elasticCircuitsLogService;

//   @Autowired
//   private ElasticLogRepo elasticLogRepo;

//   @Autowired
//   private AuthService authService;

//   @Autowired
//   private ElasticSearchQueryBuilder queryBuilder;

//   @GetMapping("/findAll")
//   List<Map<String, Object>> findAll() {
//     return elasticLogService.elasticWorkconnectLogs();
//   }

//   @GetMapping("/findAllCircuitsLogs")
//   List<Map<String, Object>> findAllCircuitsLogs() {
//     return elasticCircuitsLogService.elasticCircuitsLogs();
//   }

//   @GetMapping("/findAllCircuitsLogsBySiteId")
//   List<Map<String, Object>> findAllCircuitsLogsBySiteId(
//       @RequestParam String storeId) {
//     return elasticCircuitsLogService.elasticCircuitsLogsById(storeId);
//   }

//   @GetMapping("/findAllCircuitsLogsProviderBySiteId")
//   List<Map<String, Object>> findAllCircuitsLogsProviderBySiteId(
//       @RequestParam String storeId) {
//     return elasticCircuitsLogService.elasticCircuitsLogsProviderById(storeId);
//   }

//   @GetMapping("/findAllCircuitsLogsMacBySiteId")
//   List<Map<String, Object>> findAllCircuitsLogsMacBySiteId(
//       @RequestParam String storeId) {
//     return elasticCircuitsLogService.elasticCircuitsLogsMacById(storeId);
//   }

//   @GetMapping("/findAllCircuitsLogsImsiBySiteId")
//   List<Map<String, Object>> findAllCircuitsLogsImsiBySiteId(
//       @RequestParam String storeId) {
//     return elasticCircuitsLogService.elasticCircuitsLogsImsiById(storeId);
//   }

//   @GetMapping("/findAllCircuitsLogsProvider")
//   List<Map<String, Object>> findAllCircuitsLogsProvider() {
//     return elasticCircuitsLogService.elasticCircuitsLogsProvider();
//   }

//   @GetMapping("/findAllCircuitsLogsMac")
//   List<Map<String, Object>> findAllCircuitsLogsMac() {
//     return elasticCircuitsLogService.elasticCircuitsLogsMac();
//   }

//   @GetMapping("/findAllCircuitsLogsImsi")
//   List<Map<String, Object>> findAllCircuitsLogsImsi() {
//     return elasticCircuitsLogService.elasticCircuitsLogsImsi();
//   }

//   @GetMapping("/getCircuitsLogMacByTimePeriod")
//   List<Map<String, Object>> getCircuitsMacLogTimePeriod(
//       // @RequestHeader(value = "Authorization", required = false) String auth,
//       @RequestParam String fromDate,
//       @RequestParam String toDate) throws JSONException, ParseException {

//     return elasticCircuitsLogService.getCircuitsMacLogTimePeriod(fromDate, toDate);
//   }

//   @GetMapping("/getCircuitsLogProvByTimePeriod")
//   List<Map<String, Object>> getCircuitsProvLogTimePeriod(
//       // @RequestHeader(value = "Authorization", required = false) String auth,
//       @RequestParam String fromDate,
//       @RequestParam String toDate) throws JSONException, ParseException {

//     return elasticCircuitsLogService.getCircuitsProvLogTimePeriod(fromDate, toDate);

//   }

//   @GetMapping("/getCircuitsLogImsiByTimePeriod")
//   List<Map<String, Object>> getCircuitImsisLogTimePeriod(
//       // @RequestHeader(value = "Authorization", required = false) String auth,
//       @RequestParam String fromDate,
//       @RequestParam String toDate) throws JSONException, ParseException {

//     return elasticCircuitsLogService.getCircuitsImsiLogTimePeriod(fromDate, toDate);

//   }

//   // @PostMapping("/elkLog")
//   // public void insertLog(
//   // @RequestHeader(value = "Authorization", required = false) String auth,
//   // @RequestParam String action,
//   // @RequestParam String status,
//   // @RequestParam String service,
//   // @RequestParam String payload
//   // // @RequestBody ElasticLog elasticLog
//   // ) throws JSONException {

//   @PostMapping("/elkLog")
//   public void insertLog(
//       @RequestHeader(value = "Authorization", required = false) String auth,
//       @RequestParam String action,
//       @RequestParam String status,
//       @RequestParam String service,
//       @RequestParam String payload,
//       @RequestParam String clientIp
//   // @RequestBody ElasticLog elasticLog
//   ) throws JSONException {
//     elasticLogService.saveWorkConnectLog(
//         auth,
//         action,
//         status,
//         service,
//         payload,
//         clientIp);
//   }

//   @GetMapping("/getElasticLogByStoreId")
//   public List<ElasticLog> getElasticLogByStoreId(
//       // @RequestHeader(value = "Authorization", required = false) String auth,
//       @RequestParam String storeId
//   // @RequestBody ElasticLog elasticLog
//   ) throws JSONException {
//     List<ElasticLog> items = elasticLogRepo.findByPayloadContainingStoreId(
//         storeId);

//     return items;
//   }

//   // @GetMapping("/getElasticLogByTimePeriod")
//   // public List<Map<String, Object>> getElasticLogByStoreIdTimePeriod(
//   // @RequestHeader(value = "Authorization", required = false) String auth,
//   // @RequestParam String fromDate,
//   // @RequestParam String toDate
//   // ) throws JSONException, ParseException {
//   // try {
//   // Map<String, String> result = authService.isTokenValid(auth);
//   // if (result.get("status").equals("SUCCESS")) {
//   // List<String> roles = authService.getRoles(auth);
//   // if (roles.contains("ROLE_ACTIVITY_LOG")) {
//   // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//   // dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));

//   // long fromTimestamp = dateFormat.parse(fromDate).getTime();
//   // long toTimestamp = dateFormat.parse(toDate).getTime();

//   // List<Map<String, Object>> allLogs =
//   // elasticLogService.elasticWorkconnectLogs();
//   // List<Map<String, Object>> filteredLogs = new ArrayList<>();

//   // // Filter logs by timestamp range
//   // for (Map<String, Object> log : allLogs) {
//   // long logTimestamp = (long) log.get("timestamp");
//   // if (logTimestamp >= fromTimestamp && logTimestamp <= toTimestamp) {
//   // filteredLogs.add(log);
//   // }
//   // }

//   // return filteredLogs;
//   // }
//   // }
//   // } catch (Exception e) {
//   // e.printStackTrace();
//   // }
//   // // Return an empty list if the method doesn't return any value
//   // return [{"message" : "You're not Authorized", "status":
//   // HttpStatus.UNAUTHORIZED}];
//   // }
//   @GetMapping("/getElasticLogByTimePeriod")
//   public ResponseEntity<?> getElasticLogByStoreIdTimePeriod(
//       // @RequestHeader(value = "Authorization", required = false) String auth,
//       @RequestParam String fromDate,
//       @RequestParam String toDate) throws JSONException, ParseException {
//     try {
//       // Map<String, String> result = authService.isTokenValid(auth);
//       // if (result.get("status").equals("SUCCESS")) {
//       // List<String> roles = authService.getRoles(auth);
//       // if (roles.contains("ROLE_ACTIVITY_LOG")) {
//       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//       dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));

//       long fromTimestamp = dateFormat.parse(fromDate + " 00:00:00").getTime();
//       long toTimestamp = dateFormat.parse(toDate + " 23:59:59").getTime();
//       System.out.println(fromTimestamp + " " + toTimestamp);
//       // List<Map<String, Object>> allLogs = findAll();
//       // List<Map<String, Object>> filteredLogs = new ArrayList<>();

//       // for (Map<String, Object> log : allLogs) {
//       // long logTimestamp = (long) log.get("timestampMillisec");
//       // System.out.println(logTimestamp);
//       // if (logTimestamp >= fromTimestamp && logTimestamp <= toTimestamp) {
//       // filteredLogs.add(log);
//       // }
//       // }
//       // QueryBuilder query = queryBuilder.buildQuery(
//       // "timestampMilliSec",
//       // fromTimestamp,
//       // toTimestamp
//       // );

//       Pageable pageable = PageRequest.of(0, 100000);

//       List<Map<String, Object>> filteredLogs = new ArrayList<>();
//       Iterable<ElasticLog> allLogs = elasticLogRepo.findAll(pageable);
//       for (ElasticLog log : allLogs) {
//         long logTimestamp = log.getTimestampMilliSec();
//         // System.out.println(logTimestamp);
//         if (logTimestamp >= fromTimestamp && logTimestamp <= toTimestamp) {
//           // Convert ElasticLog object to a Map<String, Object>
//           Map<String, Object> logMap = new HashMap<>();
//           logMap.put("timestampMilliSec", log.getTimestampMilliSec());
//           logMap.put("status", log.getStatus());
//           logMap.put("user", log.getUser());
//           logMap.put("roleGroup", log.getRoleGroup());
//           logMap.put("tenant", log.getTenant());
//           logMap.put("service", log.getService());
//           logMap.put("payload", log.getPayload());
//           logMap.put("action", log.getAction());
//           logMap.put("clientIp", log.getClientIp());
//           filteredLogs.add(logMap);
//         }
//       }
//       return ResponseEntity.ok(filteredLogs);
//       // return ResponseEntity.ok().build();
//       // }
//       // }

//       // return ResponseEntity
//       // .status(HttpStatus.UNAUTHORIZED)
//       // .body(Collections.singletonMap("message", "You're not authorized"));
//     } catch (Exception e) {
//       e.printStackTrace();

//       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//     }
//   }
//   // @GetMapping("/getElasticLogByTimePeriod")
//   // public List<ElasticLog> getElasticLogByStoreIdTimePeriod(
//   // @RequestParam String fromDate,
//   // @RequestParam String toDate
//   // ) throws ParseException {
//   // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//   // dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));

//   // // Parse the fromDate and toDate strings into Timestamp objects
//   // Timestamp fromTimestamp = new Timestamp(
//   // dateFormat.parse(fromDate).getTime()
//   // );
//   // Timestamp toTimestamp = new Timestamp(dateFormat.parse(toDate).getTime());

//   // System.out.println(fromTimestamp + " " + toTimestamp);

//   // List<ElasticLog> items = elasticLogRepo.findByTimestampBetween(
//   // fromTimestamp,
//   // toTimestamp
//   // );
//   // System.out.println("elastic: " + items);

//   // return items;
//   // }

// }
