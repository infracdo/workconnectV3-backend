package com.tracker_app.tracker.Controller;

import com.tracker_app.tracker.DataSource_NMS.Service.circuit_service;
import com.tracker_app.tracker.DataSource_Radius.Repo.radacct_repo;
import com.tracker_app.tracker.Helper.radius_helper;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/cliqq")
public class radius_cliqq_controller {

  @Autowired
  private radius_helper rad_helper;

  @Autowired
  private radacct_repo radacct_repo;

  @Autowired
  private circuit_service circuitService;

  @GetMapping(path = "/get_num_users_and_duration")
  public ResponseEntity<Object> get_num_users_and_duration(
    @RequestParam String start_date,
    @RequestParam String end_date
  )
    throws IOException, InterruptedException, ParseException, ExecutionException {
    List<Map<String, Object>> result = rad_helper.get_cliqq_summary_users_and_duration(
      start_date,
      end_date
    );

    return new ResponseEntity<Object>(result, HttpStatus.OK);
  }

  @GetMapping("/getCliqqSummaryByStoreId")
  public ResponseEntity<List<Map<String, Object>>> getCliqqSummaryString(
    @RequestParam String end_date,
    @RequestParam String start_date,
    @RequestParam String storeId
  ) throws IOException, InterruptedException, ParseException {
    System.out.println("Controller Level");
    System.out.println(start_date);
    System.out.println(end_date);
    System.out.println(storeId);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date startDate = new Date(dateFormat.parse(start_date).getTime());
    Date endDate = new Date(dateFormat.parse(end_date).getTime());
    String ipAdd = circuitService.getLoopbackIp(storeId, 0);
    // List<Map<String, Object>> result = rad_helper.getCliqqSummaryByStoreId(
    //   start_date,
    //   end_date,
    //   storeId
    // );

    List<Map<String, Object>> result = radacct_repo.getSessionTimeByStoreId(
      startDate,
      endDate,
      ipAdd
    );
    List<Map<String, Object>> filteredResult = new ArrayList<>();
    for (Map<String, Object> map : result) {
      if (map != null && !map.containsKey(null)) {
        filteredResult.add(map);
      }
    }

    return new ResponseEntity<List<Map<String, Object>>>(
      filteredResult,
      HttpStatus.OK
    );
  }
}
