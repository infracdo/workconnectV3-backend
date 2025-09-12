package com.tracker_app.tracker.Controller;

import com.tracker_app.tracker.DataSource_NMS.Service.mac_retrieve_service;
// import com.tracker_app.tracker.DataSource_elasticsearch.service.ElasticLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/autodiscover/")
public class autodiscover_controller {

  @Autowired
  private mac_retrieve_service mac_retrieve_service;

  // @Autowired
  // private ElasticLogService elasticLogService;

  @GetMapping("snmpEnable")
  public String getSNMPEnable(@RequestParam String storeId) {
    String response = mac_retrieve_service.enableSNMP(storeId);
    System.out.println(response);
    return response;
  }

  @GetMapping("getSNMP")
  public String getSNMP(
    @RequestParam String storeId,
    @RequestHeader(value = "Authorization", required = false) String auth,
    @RequestParam(value = "clientIp", required = false) String clientIp
  ) {
    // elasticLogService.saveWorkConnectLog(
    //   auth,
    //   "Api call - backend",
    //   HttpStatus.OK.toString(),
    //   "api/autodiscover/getSNMP",
    //   storeId,
    //   clientIp
    // );  // REMOVED ELASTICSEARCH CALL 

    String response = mac_retrieve_service.discoverSNMP(storeId);
    System.out.println(response);
    return response;
  }
}
