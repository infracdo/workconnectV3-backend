package com.tracker_app.tracker.Controller;

import com.tracker_app.tracker.DataSource_WorkConnect.Entity.accounts_and_cookies;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.accounts_and_cookiesRepo;
// import com.tracker_app.tracker.DataSource_elasticsearch.service.ElasticLogService;
//import com.tracker_app.tracker.Helper.kafka_helper;
import com.tracker_app.tracker.Helper.rt_helper_rest1;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/rt")
public class rt_controller {

  @Autowired
  private accounts_and_cookiesRepo AccountCookieRepo;

  @Autowired
  rt_helper_rest1 rt = new rt_helper_rest1();

  // @Autowired
  // private ElasticLogService elasticLogService;

  //@Autowired
  //kafka_helper kafka = new kafka_helper();

  @Value("${RT.URL}")
  String rt_url;

  @PostMapping(path = "/auth")
  public ResponseEntity<String> rt_login(@RequestBody String payload) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");

    String cookie = rt.rt_auth(rt_url, payload);
    try {
      JSONObject json_payload = new JSONObject(payload);
      String name = json_payload.getString("rt_user");

      if (cookie.contains("401 Credentials required")) {
        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
      } else {
        try {
          accounts_and_cookies user = AccountCookieRepo.findByAccountName(name);
          user.setcookie(cookie);
          AccountCookieRepo.save(user);
        } catch (Exception e) {
          accounts_and_cookies user = new accounts_and_cookies();
          user.setname(name);
          user.setcookie(cookie);
          AccountCookieRepo.save(user);
        }
        return new ResponseEntity<String>(cookie, headers, HttpStatus.OK);
      }
    } catch (Exception e) {
      return new ResponseEntity<String>(
        "JSON ERROR:" + e,
        headers,
        HttpStatus.OK
      );
      //TODO: handle exception
    }
  }

  @PostMapping(path = "/get/queue/tickets/all")
  public ResponseEntity<String> rt_get_que(@RequestBody String payload) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    try {
      JSONObject json_payload = new JSONObject(payload);
      String cookie = json_payload.getString("cookie");
      accounts_and_cookies user = AccountCookieRepo.findByAccountCooke(cookie);

      String new_payload = rt.rt_get_tickets_all(
        rt_url,
        payload,
        user.getname()
      );

      if (new_payload.contains("401 Credentials required")) {
        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
      } else {
        return new ResponseEntity<String>(new_payload, headers, HttpStatus.OK);
      }
    } catch (Exception e) {
      return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
    }
  }

  @PostMapping(path = "/get/tickets/detail")
  public ResponseEntity<String> rt_get_ticketdetails(
    @RequestBody String payload
  ) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");

    String new_payload = rt.rt_get_tickets(rt_url, payload);
    if (new_payload.contains("401 Credentials required")) {
      return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
    } else {
      return new ResponseEntity<String>(new_payload, headers, HttpStatus.OK);
    }
  }

  @PostMapping(path = "/set/tickets/status")
  public ResponseEntity<String> rt_set_ticket_status(
    @RequestBody String payload
  ) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");

    String new_payload = rt.rt_change_tickets_status(rt_url, payload);
    if (new_payload.contains("401 Credentials required")) {
      return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
    } else {
      return new ResponseEntity<String>(new_payload, headers, HttpStatus.OK);
    }
  }

  @PostMapping(path = "/get/tickets/first_comment")
  public ResponseEntity<String> rt_get_first_comment(
    @RequestBody String payload
  ) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");

    String new_payload = rt.rt_get_1stComment(rt_url, payload);
    if (new_payload.contains("401 Credentials required")) {
      return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
    } else {
      return new ResponseEntity<String>(new_payload, headers, HttpStatus.OK);
    }
  }

  @PostMapping(path = "/comment/tickets")
  public ResponseEntity<String> rt_comment_ticket(@RequestBody String payload) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");

    String new_payload = rt.rt_comment_ticket(rt_url, payload);
    if (new_payload.contains("401 Credentials required")) {
      return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
    } else {
      return new ResponseEntity<String>(new_payload, headers, HttpStatus.OK);
    }
  }

  @PostMapping(path = "/tickets/CheckInOut")
  public ResponseEntity<String> rt_CheckInOut(@RequestBody String payload) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");

    String ticket_id = "";
    String cookie = "";
    String longitude = "";
    String latitude = "";
    String datetime = "";
    String log_type = "";
    String name = "";
    try {
      JSONObject json_payload = new JSONObject(payload);
      ticket_id = json_payload.getString("id");
      cookie = json_payload.getString("cookie");
      longitude = json_payload.getString("lon");
      latitude = json_payload.getString("lat");
      datetime = json_payload.getString("time");
      log_type = json_payload.getString("log_type");
    } catch (Exception e) {
      return new ResponseEntity<String>(
        "json input error",
        headers,
        HttpStatus.OK
      );
    }

    String new_payload = rt.rt_tickets_CheckInOut(rt_url, payload);

    try {
      accounts_and_cookies user = AccountCookieRepo.findByAccountCooke(cookie);
      name = user.getname();
    } catch (Exception e) {
      return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
      //TODO: handle exception
    }
    /* 
        if (new_payload.contains("Ok")){
            try {
                System.out.println("sending to kafka");                
                String jsonmessage = "{\"id\":\""+ticket_id+"\",\"lon\":\""+longitude+"\",\"lat\":\""+latitude+"\",\"time\":\""+datetime+"\",\"user\":\""+name+"\",\"log_type\":\""+log_type+"\"}";
                //kafka.sendMessage(jsonmessage,"test_rt_topic");
                kafka.sendMessage(jsonmessage,"workconnect_topic");
            } catch (Exception e) {
                //TODO: handle exception
                return new ResponseEntity<String>("kafka error",headers,HttpStatus.OK);
            }
        }
        */
    if (new_payload.contains("401 Credentials required")) {
      return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
    } else {
      return new ResponseEntity<String>(new_payload, headers, HttpStatus.OK);
    }
  }

  @GetMapping(path = "/sites/ticket")
  public ResponseEntity<Object> circuit_details(
    @RequestHeader(value = "Authorization", required = false) String auth,
    @RequestParam(value = "site_id", required = false) String site_id,
    @RequestParam String clientIp
  ) throws IOException {
    ResponseEntity<Object> responseEntity = new ResponseEntity<>(
      rt.get_site_tickets(rt_url, site_id),
      HttpStatus.OK
    );

    HttpStatus status = responseEntity.getStatusCode();

    // elasticLogService.saveWorkConnectLog(
    //   auth,
    //   "Api call - backend",
    //   status.toString(),
    //   "api/rt/sites/ticket",
    //   site_id,
    //   clientIp
    // );  // REMOVED ELASTICSEARCH CALL 
    return responseEntity;
  }
}
