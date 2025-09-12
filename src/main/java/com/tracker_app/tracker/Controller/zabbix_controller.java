package com.tracker_app.tracker.Controller;

import antlr.collections.impl.LList;
import com.tracker_app.tracker.DataSource_WorkConnect.Entity.site_circuit_status;
import com.tracker_app.tracker.DataSource_WorkConnect.Entity.site_location;
import com.tracker_app.tracker.DataSource_WorkConnect.Entity.sites_summary;
import com.tracker_app.tracker.DataSource_WorkConnect.Entity.tenant_lookup;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.site_circuit_status_repo;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.site_location_repo;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.sites_summary_repo;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.tenant_lookup_repo;
// import com.tracker_app.tracker.DataSource_elasticsearch.model.ElasticSiteSummary;
// import com.tracker_app.tracker.DataSource_elasticsearch.service.ElasticLogService;
// import com.tracker_app.tracker.DataSource_elasticsearch.service.ElasticSiteSummary_service;
import com.tracker_app.tracker.Helper.AuthService;
import com.tracker_app.tracker.Helper.atis_helper;
import com.tracker_app.tracker.Helper.zabbix_helper;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/zabbix")
public class zabbix_controller {

  // @Autowired
  // private ElasticLogService elasticLogService;

  @Autowired
  private zabbix_helper zabbix_help;

  @Autowired
  private site_circuit_status_repo site_circuit_repo;

  //@Autowired
  //private ElasticSiteSummary_service elksite_sum_service;

  @Autowired
  private atis_helper atishelper;

  @Autowired
  private sites_summary_repo site_sum_repo;

  @Autowired
  private AuthService authService;

  @Autowired
  private tenant_lookup_repo tenant_repo;

  @Value("${ATIS.URL}")
  private String atis_url;

  // @GetMapping(path = "/circuit_details")
  // public ResponseEntity<Object> circuit_details(
  //   @RequestParam(value = "site_id", required = false) String site_id,
  //   @RequestParam(value = "month", required = false) String month,
  //   @RequestParam(value = "year", required = false) String year
  // ) throws JSONException, ParseException {
  //   // will turn 01, 02, 03, 04 into 1,2,3,4
  //   int monthParse = Integer.parseInt(month);
  //   String monthString = String.valueOf(monthParse);
  //   System.out.println("Month Value: " + monthString);
  //   return new ResponseEntity<Object>(
  //     zabbix_help.classifySites(site_id, monthString, year),
  //     HttpStatus.OK
  //   );
  // }
  @GetMapping(path = "/circuit_details")
  public ResponseEntity<Object> circuit_details(
    // @RequestHeader(value = "Authorization", required = false) String auth,
    @RequestParam(value = "storeId", required = false) String storeId,
    @RequestParam(value = "fromDate", required = false) String fromDate,
    @RequestParam(value = "toDate", required = false) String toDate
  ) throws JSONException, ParseException {
    try {
      return new ResponseEntity<Object>(
        zabbix_help.classifySites(storeId, fromDate, toDate),
        HttpStatus.OK
      );
    } catch (Exception e) {
      e.printStackTrace();

      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Internal Server Error");
    }
  }

  // @GetMapping(path = "/circuitLastChange")
  // public ResponseEntity<Object> circuitLastChange(
  //   // @RequestHeader(value = "Authorization", required = false) String auth,
  //   @RequestParam(value = "storeId", required = true) String storeId
  // ) throws JSONException, ParseException {
  //   try {
  //     return new ResponseEntity<Object>(
  //       zabbix_help.classifySitesLastChange(storeId),
  //       HttpStatus.OK
  //     );
  //   } catch (Exception e) {
  //     e.printStackTrace();

  //     return ResponseEntity
  //       .status(HttpStatus.INTERNAL_SERVER_ERROR)
  //       .body("Internal Server Error");
  //   }
  // }

  private static String toCSV(List<Map<String, Object>> dataList) {
    String header = "name,store_and_circuit_label,start,end,duration_in_min";

    String content = dataList
      .stream()
      .map(row ->
        row
          .values()
          .stream()
          .map(Object::toString)
          .collect(Collectors.joining(","))
      )
      .collect(Collectors.joining("\n"));
    return header + "\n" + content;
  }

  @GetMapping(path = "/get_zabbix_report")
  public ResponseEntity<Object> zabbix_report(
    @RequestParam(value = "storeId", required = false) String storeId,
    @RequestParam(value = "fromDate", required = false) String fromDate,
    @RequestParam(value = "toDate", required = false) String toDate
  ) throws JSONException, ParseException {
    List<Map<String, Object>> report_list = zabbix_help.getZabbixReport(
      storeId,
      fromDate,
      toDate
    );

    //List<String> csvData = toCSV(report_list);
    //String csvContent = String.join("name,store_and_circuit_label,start,end,duration_in_min\n", csvData);
    // String csvContent = toCSV(report_list);

    // HttpHeaders headers = new HttpHeaders();
    // headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    // headers.setContentDispositionFormData("attachment", "data.csv");

    return new ResponseEntity<Object>(report_list, HttpStatus.OK);
  }

  @GetMapping(path = "/tenant_host_group_id/update")
  public ResponseEntity<Object> tenant_name_update(
    @RequestParam(value = "atis_tenant") String atis_tenant,
    @RequestParam(value = "group_host_id") Integer[] group_host_id,
    @RequestHeader(value = "Authorization") String auth
  ) {
    Map<String, String> result = authService.isTokenValid(auth);
    if (result.get("status").equals("SUCCESS")) {
      if (authService.getRoles(auth).contains("ROLE_SUPERADMIN")) {
        try {
          tenant_lookup tenant = tenant_repo.get_lookup_by_name(atis_tenant);
          tenant.setZabbix_group_id(group_host_id);
          tenant_repo.save(tenant);
          return new ResponseEntity<Object>("OK", HttpStatus.OK);
        } catch (Exception e) {
          return new ResponseEntity<Object>(
            "Error " + e.getMessage(),
            HttpStatus.CONFLICT
          );
        }
      } else {
        return new ResponseEntity<Object>(
          "Not Authorized",
          HttpStatus.UNAUTHORIZED
        );
      }
    } else {
      return new ResponseEntity<Object>(
        "Not Authorized",
        HttpStatus.UNAUTHORIZED
      );
    }
  }
  // @Scheduled(fixedRate = (10*60000))
  // public void update_sites_link() throws InterruptedException, IOException, JSONException{
  //     System.out.println("----------------START UPDATING SITES LINK----------------"+LocalDateTime.now());
  //     zabbix_help.update_site_openAndnew_tickets();
  //     List<String> tenants_monitored = new ArrayList<String>();
  //     tenants_monitored.add("philippine seven corporation");
  //     //tenants_monitored.add("ultra mega multi-Sales inc");
  //     //tenants_monitored.add("dict");
  //     long startTime = System.currentTimeMillis();
  //     for (String tenant_monitored : tenants_monitored) {
  //         List<String> site_status_monitored = new ArrayList<String>();
  //         //site_status_monitored.add("active");

  //         Iterable<ElasticSiteSummary> site_loc = elksite_sum_service.get_all_ActiveAndTempClosed(site_status_monitored,tenant_monitored);

  //         if(site_loc != null){

  //             int iterations = 0;
  //             for (ElasticSiteSummary site_location : site_loc) {
  //                 System.out.println("Site ID"+site_location.getSite_id());
  //                 try {
  //                     System.out.println("Site ID"+site_location.getCircuits().toArray().length);
  //                 } catch (Exception e) {
  //                     e.printStackTrace();
  //                 }

  //                 long start_linksup = System.currentTimeMillis();
  //                 System.out.println("FUUUUUUUUUUUUUUUUUUUUK:");

  //        if(!site_location.getTenant_name().contains("DICT")){
  //            try {
  //                String site_id = site_location.getSite_id();
  //                Object obj_circuits = atishelper.get_site_circuits(atis_url, site_id);
  //                JSONArray circuits_arr = new JSONArray(obj_circuits.toString());
  //
  //                if(circuits_arr.length()<1){
  //                    obj_circuits = atishelper.get_site_interface(atis_url, site_id);
  //                    circuits_arr = new JSONArray(obj_circuits.toString());
  //                    if(circuits_arr.length()<1){
  //                        continue;
  //                    }
  //                    //Thread.sleep(10);
  //                }
  //            } catch (Exception e) {
  //                try {
  //                    String site_id = site_location.getSite_id();
  //                    Object obj_circuits = atishelper.get_site_interface(atis_url, site_id);
  //                    JSONArray circuits_arr = new JSONArray(obj_circuits.toString());
  //                    if(circuits_arr.length()<1){
  //                        continue;
  //                    }
  //                } catch (Exception e1) {
  //                    continue;
  //                }
  //            }
  //        }

  // iterations = iterations + 1;

  // List<site_circuit_status> old_circuits = new ArrayList<site_circuit_status>();
  // //System.out.println("FUUUUUUUUUUUUUUUUUUUUK:"+old_circuits.size());
  // List<site_circuit_status> old_db_circuits = new ArrayList<site_circuit_status>();
  // try {
  //     old_circuits = site_location.getCircuits();
  //     old_db_circuits = site_circuit_repo.get_circuit_by_site_id(site_location.getSite_id());
  // } catch (Exception e) {
  //     e.printStackTrace();
  // }

  // System.out.println("Old Circuits Size:"+old_circuits);

  /* 
                    if(old_db_circuits.size()==0){
                        for (site_circuit_status old_circuit : old_circuits) {
                            site_circuit_status new_old_circuit = new site_circuit_status();
                            new_old_circuit = old_circuit;
                            site_circuit_repo.save(new_old_circuit);
                        }
                    }
                    */
  // new Thread(()->{
  //     Integer num_circuits_up = 0;
  //     try {
  //         num_circuits_up = zabbix_help.update_link_status(site_location);
  //     } catch (Exception e) {
  //         num_circuits_up = 0;
  //     }

  //     try {
  //         site_location.setLinks_up(num_circuits_up);
  //         List<site_circuit_status> circuits = site_circuit_repo.get_circuit_by_site_id(site_location.getSite_id());
  //         site_location.setCircuits(circuits);
  //         elksite_sum_service.save(site_location);
  //     } catch (Exception e) {
  //     System.out.println("ERROR: "+e.getMessage());
  //         site_location.setLinks_up(num_circuits_up);
  //         elksite_sum_service.save(site_location);
  //     }
  // }).start();

  //    new Thread(()->{
  //        Integer num_circuits_up = 0;
  //        try {
  //            num_circuits_up = zabbix_help.update_link_status(site_location);
  //        } catch (Exception e) {
  //            num_circuits_up = 0;
  //        }
  //
  //        try {
  //            site_location.setLinks_up(num_circuits_up);
  //            List<site_circuit_status> circuits = site_circuit_repo.get_circuit_by_site_id(site_location.getSite_id());
  //            site_location.setCircuits(circuits);
  //            elksite_sum_service.save(site_location);
  //        } catch (Exception e) {
  //           // System.out.println("ERROR: "+e.getMessage());
  //            site_location.setLinks_up(num_circuits_up);
  //            elksite_sum_service.save(site_location);
  //        }
  //    }).start();
  // Thread.sleep(100);
  // if(iterations == 10){
  //     iterations = 0;
  //     Thread.sleep(1000);
  //long end_linksup = System.currentTimeMillis();
  //System.out.println("Duration: "+ ((end_linksup-start_linksup))+"ms");
  //                 }
  //             }
  //         }
  //     }
  //     long endtime = System.currentTimeMillis();
  //     System.out.println("----------------DONE UPDATING SITES LINK----------------Duration: "+(endtime-startTime)+"ms----------------");
  // }

}
