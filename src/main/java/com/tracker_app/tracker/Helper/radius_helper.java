package com.tracker_app.tracker.Helper;

import com.tracker_app.tracker.DataSource_NMS.Service.circuit_service;
import com.tracker_app.tracker.DataSource_Radius.Repo.radacct_repo;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class radius_helper {

  @Value("${NETBOX.URL}")
  String netbox_url;

  @Value("${NETBOX.TOKEN}")
  String netbox_auth_token;

  @Autowired
  private netbox_helper netbox_help;

  @Autowired
  private radacct_repo radius_repo;

  @Autowired
  private circuit_service circuitService;

  // public List<Map<String, Object>> get_cliqq_summary_users_and_duration(
  //   String start_date,
  //   String end_date
  // ) throws IOException, InterruptedException, ParseException {
  //   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  //   List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

  //   Date startDate = new Date(dateFormat.parse(start_date).getTime());
  //   Date endDate = new Date(dateFormat.parse(end_date).getTime());

  //   List<Map<String, Object>> rad_datas = radius_repo.getAverageTimeAndSessionCount(
  //     startDate,
  //     endDate
  //   );
  //   for (Map<String, Object> data : rad_datas) {
  //     new Thread(() -> {
  //       String site_id;
  //       try {
  //         site_id =
  //           netbox_help.get_site_id_via_ipam(
  //             netbox_auth_token,
  //             netbox_url,
  //             data.get("nasipaddress").toString()
  //           );
  //       } catch (IOException e) {
  //         site_id = "ERROR";
  //       } catch (InterruptedException e) {
  //         // TODO Auto-generated catch block
  //         site_id = "ERROR";
  //       }
  //       //System.out.println(site_id);

  //       if (!site_id.contains("ERROR")) {
  //         Map<String, Object> new_result = new HashMap<>();
  //         new_result.put("site_id", site_id);
  //         new_result.put(
  //           "average_time",
  //           Math.round(
  //             (Float.parseFloat(data.get("average_time").toString()) / 60) *
  //             100.0
  //           ) /
  //           100.0
  //         );
  //         new_result.put("session_count", data.get("session_count").toString());
  //         // new_result.put("from", start_date);
  //         // new_result.put("to", end_date);
  //         result.add(new_result);
  //       }
  //     })
  //       .start();
  //   }
  //   return result;
  //   // return rad_datas;
  // }
  // public List<Map<String, Object>> get_cliqq_summary_users_and_duration(
  //   String start_date,
  //   String end_date
  // )
  //   throws IOException, InterruptedException, ParseException, ExecutionException {
  //   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  //   List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

  //   Date startDate = new Date(dateFormat.parse(start_date).getTime());
  //   Date endDate = new Date(dateFormat.parse(end_date).getTime());

  //   List<Map<String, Object>> rad_datas = radius_repo.getAverageTimeAndSessionCount(
  //     startDate,
  //     endDate
  //   );
  //   List<CompletableFuture<Void>> futures = new ArrayList<>();

  //   for (Map<String, Object> data : rad_datas) {
  //     CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
  //       try {
  //         String site_id = netbox_help.get_site_id_via_ipam(
  //           netbox_auth_token,
  //           netbox_url,
  //           data.get("nasipaddress").toString()
  //         );
  //         if (!site_id.contains("ERROR")) {
  //           Map<String, Object> new_result = new HashMap<>();
  //           new_result.put("site_id", site_id);
  //           new_result.put(
  //             "average_time_s",
  //             data.get("average_time").toString()
  //           );
  //           new_result.put(
  //             "session_count",
  //             data.get("session_count").toString()
  //           );
  //           synchronized (result) {
  //             result.add(new_result);
  //           }
  //         }
  //       } catch (IOException | InterruptedException e) {
  //         e.printStackTrace();
  //       }
  //     });
  //     futures.add(future);
  //   }

  //   CompletableFuture<Void> allOf = CompletableFuture.allOf(
  //     futures.toArray(new CompletableFuture[0])
  //   );
  //   allOf.get();

  //   return result;
  // }

  public List<Map<String, Object>> get_cliqq_summary_users_and_duration(
    String start_date,
    String end_date
  ) throws IOException, InterruptedException, ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date startDate = new Date(dateFormat.parse(start_date).getTime());
    Date endDate = new Date(dateFormat.parse(end_date).getTime());

    List<Map<String, Object>> radDatas = radius_repo.getAverageTimeAndSessionCount(
      startDate,
      endDate
    );

    return radDatas
      .parallelStream()
      .map(data -> {
        try {
          String site_id = netbox_help.get_site_id_via_ipam(
            netbox_auth_token,
            netbox_url,
            data.get("nasipaddress").toString()
          );
          if (!site_id.contains("ERROR")) {
            Map<String, Object> new_result = new HashMap<>();
            new_result.put("site_id", site_id);
            new_result.put("average_time_s", data.get("average_time"));
            new_result.put("session_count", data.get("session_count"));
            return new_result;
          }
        } catch (IOException | InterruptedException e) {
          e.printStackTrace();
        }
        return null;
      })
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
  }
  // public List<Map<String, Object>> getCliqqSummaryByStoreId(
  //   String start_date,
  //   String end_date,
  //   String storeId
  // ) throws IOException, InterruptedException, ParseException {
  //   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  //   Date startDate = new Date(dateFormat.parse(start_date).getTime());
  //   Date endDate = new Date(dateFormat.parse(end_date).getTime());
  //   String ipAdd = circuitService.getLoopbackIp(storeId, 0);

  //   System.out.println("Helper Level");
  //   System.out.println(startDate);
  //   System.out.println(endDate);
  //   System.out.println(storeId);

  //   List<Map<String, Object>> radDatas = radius_repo.getSessionTimeByStoreId(
  //     startDate,
  //     endDate,
  //     ipAdd
  //   );

  //   return radDatas
  //     .parallelStream()
  //     .map(data -> {
  //       Map<String, Object> new_result = new HashMap<>();
  //       new_result.put("ipAddress", data.get("nasipaddress"));
  //       new_result.put("sessionTime", data.get("acctsessiontime"));
  //       new_result.put("startTime", data.get("acctstarttime"));
  //       new_result.put("stopTime", data.get("acctstoptime"));
  //       new_result.put("terminateCause", data.get("acctterminatecause"));
  //       new_result.put("macAddress", data.get("callingstationid"));
  //       return new_result;
  //     })
  //     .filter(Objects::nonNull)
  //     .collect(Collectors.toList());
  // }
}
