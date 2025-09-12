package com.tracker_app.tracker.Controller;

import com.tracker_app.tracker.DataSource_NMS.Entity.mikrotik;
import com.tracker_app.tracker.DataSource_NMS.Entity.ruijie;
import com.tracker_app.tracker.DataSource_NMS.Repo.mikrotik_repo;
import com.tracker_app.tracker.DataSource_NMS.Repo.ruijie_repo;
import com.tracker_app.tracker.DataSource_WorkConnect.Entity.accounts_and_cookies;
import com.tracker_app.tracker.DataSource_WorkConnect.Entity.attendance;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.accounts_and_cookiesRepo;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.attendance_Repo;
// import com.tracker_app.tracker.DataSource_elasticsearch.service.DC_Active_service;
// import com.tracker_app.tracker.DataSource_elasticsearch.service.Index_service;
import com.tracker_app.tracker.Helper.GPXHelper;
//import com.tracker_app.tracker.Helper.kafka_helper;
import com.tracker_app.tracker.Helper.mail_service;
import com.tracker_app.tracker.Helper.rt_helper_rest1;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api")
public class api_controller {

  @Autowired
  private accounts_and_cookiesRepo AccountCookieRepo;

  @Autowired
  private attendance_Repo AttendanceRepo;

  @Autowired
  private GPXHelper helpergpx;

  @Autowired
  private mikrotik_repo mikrotik_repo;

  @Autowired
  private ruijie_repo ruijie_repo;

  @Autowired
  rt_helper_rest1 rt = new rt_helper_rest1();

  // @Autowired
  // kafka_helper kafka = new kafka_helper();

  // @Autowired
  // private Index_service index_service;

  // @Autowired
  // private DC_Active_service dc_Active_service;

  @Value("${kafka.logs.topic}")
  private String kafka_topic;

  @Value("${kafka.logs.sitereport.topic}")
  private String kafka_sitereport_topic;

  @Value("${download_path}")
  private String UploadPath;

  @Value("${download_path_tem}")
  private String UploadPath_temp;

  @Autowired
  private mail_service mailservice;

  @Value("${usermanagement.email}")
  private String mail_body;

  @GetMapping(path = "/elk/index/recreate")
  public void recreateAllIndices() {
    // index_service.recreateIndices(true);
      System.out.println("ELASTICSEARCH METHOD DISABLED");
      System.out.println("MOCK RESPONSE - RECREATED ELK INDICES"); // REPLACED ELASTICSEARCH CALL WITH MOCK
  }

  @GetMapping(path = "/test")
  public void test() throws JSONException {
    String emailSubject = "Workconnect Account";

    String emailBody = mail_body.replace("user_placeholder", "test");
    emailBody = emailBody.replace("pass_placeholder", "test");

    JSONObject request = new JSONObject();
    request.put("to", "menrod.baqs@gmail.com");
    request.put("text", emailBody);
    request.put("subject", emailSubject);
    mailservice.processNotification(request);

    request.put("to", "nemrod@apolloglobal.net");
    request.put("text", emailBody);
    request.put("subject", emailSubject);
    mailservice.processNotification(request);
  }
  
  // @PostMapping(path = "/sendlog")
  // public ResponseEntity<String> logAction(@RequestBody String message)
  //   throws JSONException, IOException {

  //   return new ResponseEntity<String>("done", HttpStatus.OK);
  // }

  @PostMapping(path = "/log_time")
  public ResponseEntity<String> time_in(@RequestBody String payload) {
    System.out.println(payload);
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");

    try {
      JSONObject json_payload = new JSONObject(payload);
      String longitude = json_payload.getString("lon");
      String latitude = json_payload.getString("lat");
      String cookie = json_payload.getString("cookie");
      String log_type = json_payload.getString("log_type");
      String name = "";
      try {
        accounts_and_cookies user = AccountCookieRepo.findByAccountCooke(
          cookie
        );
        name = user.getname();
      } catch (Exception e) {
        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        // TODO: handle exception
      }

      LocalDateTime currentDateTime = LocalDateTime.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm:ss"
      );
      String log_time = currentDateTime.format(formatter);

      attendance newattendance = new attendance();
      newattendance.setuser(name);
      newattendance.setlongitude(Double.parseDouble(longitude));
      newattendance.setlatitude(Double.parseDouble(latitude));
      newattendance.setdatetime(currentDateTime);
      newattendance.setlog_type(log_type);
      AttendanceRepo.save(newattendance);

      String newpayload =
        "{" +
        "\"user\":\"" +
        name +
        "\",\"log_type\":\"" +
        log_type +
        "\",\"lon\":\"" +
        longitude +
        "\",\"lat\":\"" +
        latitude +
        "\",\"datetime\":\"" +
        log_time +
        "\"}";
      // kafka.sendMessage(newpayload,kafka_topic);
      return new ResponseEntity<String>(newpayload, headers, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<String>("kafka error", headers, HttpStatus.OK);
    }
  }

  @PostMapping(path = "/upload/location_history")
  public ResponseEntity<String> upload_gpx(@RequestBody MultipartFile file)
    throws IOException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");

    String path = new java.io.File(UploadPath).getAbsolutePath();

    if (!new java.io.File(path).exists()) {
      new java.io.File(path).mkdir();
    }
    java.io.File dest = new java.io.File(
      path + "/" + file.getOriginalFilename()
    );
    file.transferTo(dest);
    return new ResponseEntity<String>(
      "already created",
      headers,
      HttpStatus.OK
    );
  }

  @PostMapping(path = "/post/location_points")
  public ResponseEntity<String> update_gpx(@RequestBody String payload)
    throws IOException, JSONException, ParseException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");

    LocalDate date = LocalDate.now();

    String ParentDir = UploadPath;

    String newpayload = "";

    JSONObject json_payload = new JSONObject(payload);
    JSONArray json_array = json_payload.getJSONArray("points");

    for (int i = 0; i < json_array.length(); i++) {
      JSONObject current_point = json_array.getJSONObject(i);

      String username = current_point.getString("name");
      double longitude = Double.parseDouble(current_point.getString("lon"));
      double latitude = Double.parseDouble(current_point.getString("lat"));
      String datetime = current_point.getString("time");

      if (!new java.io.File(ParentDir).exists()) {
        new java.io.File(ParentDir).mkdir();
      }

      String fileName = username + "/" + date.toString() + ".gpx";
      String folderpath = new java.io.File(ParentDir + username)
        .getAbsolutePath();
      String filepath = new java.io.File(ParentDir + fileName)
        .getAbsolutePath();

      if (!new java.io.File(folderpath).exists()) {
        new java.io.File(folderpath).mkdir();
      }

      newpayload = helpergpx.addPoints(latitude, longitude, filepath, datetime);
      String jsonmessage =
        "{\"lon\":\"" +
        longitude +
        "\",\"lat\":\"" +
        latitude +
        "\",\"time\":\"" +
        datetime +
        "\",\"user\":\"" +
        username +
        "\",\"log_type\":\"gps_log\"}";
      // kafka.sendMessage(jsonmessage,"test_rt_topic");
    }
    return new ResponseEntity<String>(newpayload, headers, HttpStatus.OK);
  }

  @PostMapping(path = "/get/latest_location")
  public ResponseEntity<String> get_latest_location(
    @RequestBody String payload
  ) throws JSONException, IOException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");

    JSONObject json_payload = new JSONObject(payload);
    String name = json_payload.getString("name");

    LocalDate date = LocalDate.now();
    String fileName = name + "/" + date.toString() + ".gpx";

    String path = UploadPath + fileName;

    String json_string = helpergpx.getCurrentLocation(path, name);

    return new ResponseEntity<String>(json_string, headers, HttpStatus.OK);
  }

  @PostMapping(path = "/get/all_location")
  public ResponseEntity<String> get_all_location(@RequestBody String payload)
    throws JSONException, IOException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");

    JSONObject json_payload = new JSONObject(payload);
    String name = json_payload.getString("name");

    LocalDate date = LocalDate.now();
    String fileName = name + "/" + date.toString() + ".gpx";

    String path = UploadPath + fileName;

    String json_string = helpergpx.getUserLocations(path, name);

    return new ResponseEntity<String>(json_string, headers, HttpStatus.OK);
  }

  @PostMapping(path = "/data/mismatch")
  public ResponseEntity<Object> mismatch(@RequestBody String payload)
    throws JSONException {
    SimpleDateFormat formatter = new SimpleDateFormat(
      "yyyy-MM-dd 'at' HH:mm:ss z"
    );
    formatter.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Manila")));
    Date date = new Date(System.currentTimeMillis());

    JSONObject json_payload = new JSONObject(payload);

    JSONArray arrJson = json_payload.getJSONArray("sites");
    /*
     * for(int i = 0; i < arrJson.length(); i++){
     *
     * Map<String, String> message = new HashMap<String, String>();
     * message.put("report_type","net status mismatch");
     * message.put("mismatch_type",arrJson.getJSONObject(i).getString("type"));
     * message.put("data", arrJson.getJSONObject(i).getString("data"));
     * message.put("timestamp",formatter.format(date));
     * kafka.sendMessage(message.toString(), kafka_sitereport_topic);
     *
     * //System.out.println(message.toString());
     * }
     */
    // System.out.println(message.toString());
    return new ResponseEntity<Object>("Done", HttpStatus.OK);
  }

  @PostMapping(path = "/kafka_test")
  public ResponseEntity<String> kafka_test(@RequestBody String message)
    throws JSONException, IOException {
    // kafka.sendMessage(message,kafka_topic);

    return new ResponseEntity<String>("done", HttpStatus.OK);
  }

  @GetMapping("/test/{id}")
  public String test_get_provider(@PathVariable("id") String site_id) {
    Iterable<mikrotik> mikrotik_units = mikrotik_repo.findAll();
    Iterable<ruijie> ruijie_units = ruijie_repo.findAll();

    try {
      for (mikrotik unit : mikrotik_units) {
        if (unit.getSiteId().equals(site_id)) return unit.getPort1();
      }
    } catch (Exception e) {
      // TODO: handle exception
    }

    try {
      for (ruijie unit : ruijie_units) {
        if (unit.getSite_id().equals(site_id)) return unit.getVlan10();
      }
    } catch (Exception e) {
      // TODO: handle exception
    }

    // try {
    // System.out.println("------------TRYING GET PORT 2 PROVIDER------------");
    // return mikrotik_repo.getPort2Provider(site_id);

    // } catch (Exception e) {
    // e.printStackTrace();
    // }

    // try {
    // System.out.println("------------TRYING GET VLAN 10 PROVIDER------------");
    // return ruijie_repo.getVlan10Provider(site_id);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }

    return "no result";
  }

  @PostMapping(path = "/dc_active/add")
  public ResponseEntity<Object> DC_active_add(@RequestBody String params)
    throws JSONException {
      System.out.println("ELASTICSEARCH METHOD DISABLED");
    return null; // REPLACED ELASTICSEARCH CALL WITH MOCK
  }

  @PostMapping(path = "/dc_active/addByList")
  public ResponseEntity<Object> DC_active_addByList(@RequestBody String params)
    throws JSONException {
      System.out.println("ELASTICSEARCH METHOD DISABLED");
    return null; // REPLACED ELASTICSEARCH CALL WITH MOCK
  }

  @PostMapping(path = "/dc_active/remove")
  public ResponseEntity<Object> DC_active_remove(@RequestBody String params)
    throws JSONException {
      System.out.println("ELASTICSEARCH METHOD DISABLED");
      return null; // REPLACED ELASTICSEARCH CALL WITH MOCK
  }

  @PostMapping(path = "/dc_active/removeAll")
  public ResponseEntity<Object> DC_active_removeAll() throws JSONException {
      System.out.println("ELASTICSEARCH METHOD DISABLED");
    return null; // REPLACED ELASTICSEARCH CALL WITH MOCK
  }

  @PostMapping(path = "/dc_active/getAll")
  public ResponseEntity<Object> getAllDC_active() throws JSONException {
      System.out.println("ELASTICSEARCH METHOD DISABLED");
    return null; // REPLACED ELASTICSEARCH CALL WITH MOCK
  }

  @PostMapping(path = "/dc_active/updateToSummaryIndex")
  public ResponseEntity<Object> updateToSummaryIndex() throws JSONException {
      System.out.println("ELASTICSEARCH METHOD DISABLED");
    return null; // REPLACED ELASTICSEARCH CALL WITH MOCK
  }

  @PostMapping(path = "/dc_active/get")
  public ResponseEntity<Object> getAllDC_active(String site_id, String status)
    throws JSONException {
    System.out.println("hashems " + site_id);
      System.out.println("ELASTICSEARCH METHOD DISABLED");
    return null; // REPLACED ELASTICSEARCH CALL WITH MOCK
  }
}
