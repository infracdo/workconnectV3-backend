package com.tracker_app.tracker.Controller;

import com.tracker_app.tracker.DataSource_NMS.Helper.util;
import com.tracker_app.tracker.DataSource_NMS.Service.executable_playbook_service;
// import com.tracker_app.tracker.DataSource_elasticsearch.service.ElasticLogService;
import com.tracker_app.tracker.Helper.AuthService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(path = "/api/playbook/")
public class awx_controller {

  @Autowired
  private executable_playbook_service executable_playbook_service;

  @Autowired
  private util util;

  // @Autowired
  // private ElasticLogService elasticLogService;

  @Autowired
  private AuthService authService;

  ////////////////
  /// MIKROTIK ///
  ////////////////
  @PostMapping("mngnmtVpn")
  private ResponseEntity<Object> provisionGTv3(
    @RequestHeader(value = "Authorization", required = false) String auth,
    @RequestParam String storeId,
    @RequestParam String ansibleHost,
    @RequestParam String clientIp
  ) {
    util.createHostAWX(storeId, ansibleHost);

    System.out.println("Running management VPN");
    int jobIdVpn = executable_playbook_service.managementVpn(
      storeId,
      ansibleHost
    );
    String vpnResponse = executable_playbook_service.checkPlaybookStatus(
      jobIdVpn
    );

    if (!vpnResponse.equals("success")) {
      System.err.println("Error Running Management VPN!");
      return responseEntity(
        "Error Running Management VPN!",
        HttpStatus.BAD_REQUEST,
        0
      );
    }

    System.out.println("Management VPN " + vpnResponse);
    // elasticLogService.saveWorkConnectLog(
    //   auth,
    //   "Api call - backend",
    //   HttpStatus.OK.toString(),
    //   "api/playbook/managementVPN",
    //   storeId,
    //   clientIp
    // ); // REMOVED ELASTICSEARCH CALL 
    return responseEntity(
      "Management VPN " + vpnResponse,
      HttpStatus.OK,
      jobIdVpn
    );
  }

  @PostMapping("GTv3")
  private ResponseEntity<Object> GTV3(
    @RequestHeader(value = "Authorization", required = false) String auth,
    @RequestParam String storeId,
    @RequestParam String ansibleHost,
    @RequestParam String clientIp
  ) {
    System.out.println("Running GTv3 Mikrotik");
    int JobIdMikrotik = executable_playbook_service.gtv3(storeId, ansibleHost);
    String gtv3Response = executable_playbook_service.checkPlaybookStatus(
      JobIdMikrotik
    );

    if (!gtv3Response.equals("success")) {
      System.err.println("Error Running GTV3!");
      return responseEntity("Error Running GTV3!", HttpStatus.BAD_REQUEST, 0);
    }
    HashMap<String, Object> res = new HashMap<>();
    res.put("response", gtv3Response);
    res.put("jobId", JobIdMikrotik);
    // elasticLogService.saveWorkConnectLog(
    //   auth,
    //   "Api call - backend",
    //   HttpStatus.OK.toString(),
    //   "api/playbook/GTv3",
    //   storeId,
    //   clientIp
    // ); // REMOVED ELASTICSEARCH CALL

    System.out.println("Operations GTv3 Mikrotik " + gtv3Response);
    return responseEntity(
      "Operations GTv3 Mikrotik " + gtv3Response,
      HttpStatus.OK,
      JobIdMikrotik
    );
  }

  ////////////////////
  /// END MIKROTIK ///
  ////////////////////

  //////////////
  /// RUIJIE ///
  //////////////

  @PostMapping("ruijieV3")
  private ResponseEntity<Object> ruijieV3(
    @RequestHeader(value = "Authorization", required = false) String auth,
    @RequestParam String storeId,
    @RequestParam String ansibleHost,
    @RequestParam String clientIp
  ) throws InterruptedException {
    System.out.println("Running Ruijie v3");

    int jobIdRuijie = executable_playbook_service.ruijieV3(
      storeId,
      ansibleHost
    );
    String ruijieResponse = executable_playbook_service.checkPlaybookStatus(
      jobIdRuijie
    );

    if (!ruijieResponse.equals("success")) {
      return responseEntity(
        "Error Running Ruijie v3!",
        HttpStatus.BAD_REQUEST,
        0
      );
    }
    // elasticLogService.saveWorkConnectLog(
    //   auth,
    //   "Api call - backend",
    //   HttpStatus.OK.toString(),
    //   "api/playbook/ruijiev3",
    //   storeId,
    //   clientIp
    // ); // REMOVED ELASTICSEARCH CALL

    return responseEntity("Successful Ruijie v3!", HttpStatus.OK, jobIdRuijie);
  }

  @PostMapping("onBoarding")
  private ResponseEntity<Object> onBoarding(
    @RequestHeader(value = "Authorization", required = false) String auth,
    @RequestParam String storeId,
    @RequestParam Integer jobId,
    @RequestParam String clientIp
  ) throws InterruptedException {
    System.out.println("Running On Boarding Playbook");
    System.out.println("Job Id to check ip: " + jobId);

    int jobIdOnBoarding = executable_playbook_service.storeOnBoarding(
      storeId,
      jobId
    );
    String onBoardingResponse = executable_playbook_service.checkPlaybookStatus(
      jobIdOnBoarding
    );

    if (!onBoardingResponse.equals("success")) {
      System.err.println("Error Running Store Onboarding!");
      int jobIdOnBoarding2 = executable_playbook_service.storeOnBoarding(
        storeId,
        jobId
      );
      String onBoardingResponse2 = executable_playbook_service.checkPlaybookStatus(
        jobIdOnBoarding2
      );
      if (!onBoardingResponse2.equals("success")) {
        return responseEntity(
          "Error Running Store Onboarding!",
          HttpStatus.BAD_REQUEST,
          jobIdOnBoarding2
        );
      }
    }
    // elasticLogService.saveWorkConnectLog(
    //   auth,
    //   "Api call - backend",
    //   HttpStatus.OK.toString(),
    //   "api/playbook/onBoarding",
    //   storeId,
    //   clientIp
    // ); // REMOVED ELASTICSEARCH CALL

    System.out.println("Onboarding" + onBoardingResponse);

    return responseEntity(
      "Onboarding" + onBoardingResponse,
      HttpStatus.OK,
      jobIdOnBoarding
    );
  }

  @PostMapping("offBoarding")
  private ResponseEntity<Object> offBoarding(
    @RequestHeader(value = "Authorization", required = false) String auth,
    @RequestParam String storeId,
    @RequestParam String clientIp
  ) throws InterruptedException {
    try {
      Map<String, String> result = authService.isTokenValid(auth);
      if (!result.get("status").equals("SUCCESS")) {
        return responseEntity(
          "USER NOT AUTHORIZED FOR PROVISION!",
          HttpStatus.UNAUTHORIZED,
          null
        );
      }
      if (!authService.getRoles(auth).contains("ROLE_PLAYBOOK_OFFBOARDING")) {
        // elasticLogService.saveWorkConnectLog(
        //   auth,
        //   "Api call - backend",
        //   HttpStatus.UNAUTHORIZED.toString(),
        //   "api/playbook/offBoarding",
        //   storeId,
        //   clientIp
        // ); // // REMOVED ELASTICSEARCH CALL
        return responseEntity(
          "USER NOT AUTHORIZED FOR PROVISION!",
          HttpStatus.UNAUTHORIZED,
          null
        );
      }

      int jobId = executable_playbook_service.storeOffBoarding(storeId);
      System.out.println(jobId);
      String response = executable_playbook_service.checkPlaybookStatus(jobId);
      if (!response.equals("success")) {
        return responseEntity(
          "Error Running Store Off boarding!",
          HttpStatus.BAD_REQUEST,
          null
        );
      }

      // elasticLogService.saveWorkConnectLog(
      //   auth,
      //   "Api call - backend",
      //   HttpStatus.OK.toString(),
      //   "api/playbook/offBoarding",
      //   storeId,
      //   clientIp
      // ); // // REMOVED ELASTICSEARCH CALL

      return responseEntity(
        "Store Off Boarding " + response,
        HttpStatus.OK,
        jobId
      );
    } catch (Exception e) {
      e.printStackTrace();
      // elasticLogService.saveWorkConnectLog(
      //   auth,
      //   "Api call - backend",
      //   HttpStatus.INTERNAL_SERVER_ERROR.toString(),
      //   "api/playbook/offBoarding",
      //   storeId,
      //   clientIp
      // ); // // REMOVED ELASTICSEARCH CALL
      return responseEntity(
        "Error during off boarding: " + e.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR,
        null
      );
    }
  }

  @PostMapping("updateStoreStatus")
  private ResponseEntity<Object> updateStoreStatus(
    @RequestHeader(value = "Authorization", required = false) String auth,
    @RequestParam String storeId,
    @RequestParam String storeStatus,
    @RequestParam String clientIp
  ) {
    try {
      Map<String, String> result = authService.isTokenValid(auth);
      if (!result.get("status").equals("SUCCESS")) {
        return responseEntity(
          "USER NOT AUTHORIZED FOR PROVISION!",
          HttpStatus.UNAUTHORIZED,
          null
        );
      }
      if (!authService.getRoles(auth).contains("ROLE_PLAYBOOK_UPDATE_STATUS")) {
        // elasticLogService.saveWorkConnectLog(
        //   auth,
        //   "Api call - backend",
        //   HttpStatus.UNAUTHORIZED.toString(),
        //   "api/playbook/updateStoreStatus",
        //   storeId,
        //   clientIp
        // ); // // REMOVED ELASTICSEARCH CALL
        return responseEntity(
          "USER NOT AUTHORIZED FOR PROVISION!",
          HttpStatus.UNAUTHORIZED,
          null
        );
      }

      int jobId = executable_playbook_service.storeUpdateStatus(
        storeId,
        storeStatus
      );
      String response = executable_playbook_service.checkPlaybookStatus(jobId);
      if (!response.equals("success")) {
        System.out.println("Unsuccessful Off Board!");
        return responseEntity(
          "Unsuccessful in Updating Store Status",
          HttpStatus.BAD_REQUEST,
          null
        );
      }
      // elasticLogService.saveWorkConnectLog(
      //   auth,
      //   "Api call - backend",
      //   HttpStatus.CREATED.toString(),
      //   "api/playbook/updateStoreStatus",
      //   storeId + " " + storeStatus,
      //   clientIp
      // ); // // REMOVED ELASTICSEARCH CALL
      return responseEntity(
        "Update Store Status " + response,
        HttpStatus.OK,
        jobId
      );
    } catch (Exception e) {
      e.printStackTrace();

      // elasticLogService.saveWorkConnectLog(
      //   auth,
      //   "Api call - backend",
      //   HttpStatus.INTERNAL_SERVER_ERROR.toString(),
      //   "api/playbook/updateStoreStatus",
      //   storeId + " " + storeStatus,
      //   clientIp
      // ); // // REMOVED ELASTICSEARCH CALL
      return responseEntity(
        "Error during updating store status: " + e.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR,
        null
      );
    }
  }
  
  @PostMapping("operations-playbook")
  private ResponseEntity<Object> operationsPlaybook(
    @RequestHeader(value = "Authorization", required = false) String auth,
    @RequestParam String storeId,
    @RequestParam String operationPlaybookType
  ) throws InterruptedException {
    try {
      Map<String, String> result = authService.isTokenValid(auth);
      if (!result.get("status").equals("SUCCESS")) {
        return responseEntity(
          "USER NOT AUTHORIZED FOR PROVISION!",
          HttpStatus.UNAUTHORIZED,
          null
        );
      }

      int jobId = executable_playbook_service.storeOperationsPlaybook(storeId, operationPlaybookType);
      System.out.println(jobId);
      String response = executable_playbook_service.checkPlaybookStatus(jobId);

      if (operationPlaybookType.equalsIgnoreCase("cardTerminal") && response.toLowerCase().contains("ssid") && response.toLowerCase().contains("generated password")) {
        return ResponseEntity.ok(response);
      }

      if (!response.equals("success")) {
        return responseEntity(
          "Error Running Operations Playbook!",
          HttpStatus.BAD_REQUEST,
          null
        );
      }

      return responseEntity(
        "Store Operation Playbook " + response,
        HttpStatus.OK,
        jobId
      );
    } catch (Exception e) {
      e.printStackTrace();
      return responseEntity(
        "Error during operation playbook execution: " + e.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR,
        null
      );
    }
  }

  private ResponseEntity<Object> responseEntity(
    String message,
    HttpStatus status,
    Integer jobId
  ) {
    HashMap<String, Object> res = new HashMap<>();
    if (status == HttpStatus.OK) {
      res.put("message", message);
      res.put("status", status);
      res.put("jobId", jobId);
    } else {
      res.put("error", message);
      res.put("status", status);
    }

    return new ResponseEntity<>(res, status);
  }

  @GetMapping("testAWX")
  public String testAwx(@RequestParam int JobId) {
    return executable_playbook_service.checkPlaybookStatusAPI(JobId);
  }
}
