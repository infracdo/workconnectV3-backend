package com.tracker_app.tracker.DataSource_NMS.Service;

import com.tracker_app.tracker.DataSource_NMS.Entity.store_count;
import com.tracker_app.tracker.DataSource_NMS.Entity.store_count_down;
import com.tracker_app.tracker.DataSource_NMS.Repo.store_count_down_repo;
import com.tracker_app.tracker.DataSource_NMS.Repo.store_count_today_repo;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class store_count_down_service {

  @Autowired
  private store_count_today_repo store_count_today_repo;

  @Autowired
  private store_count_down_repo store_count_down_repo;

  @Autowired
  private circuit_service circuit_service;

  @Value("${application.env}")
  String appEnv;

  public boolean saveCounts(Timestamp timeStamp) {
    // Down Store
    int totalDownStores = circuit_service.getAllDownStoresSiteId().size();
    int zabbixDownStore = store_count_today_repo.getZabbixDownStores();

    // if (zabbixDownStore == 0) {
    //   int previousZabbixDownStore = store_count_down_repo.getPreviousZabbixDownStores();
    //   zabbixDownStore = previousZabbixDownStore;
    // }
    // if (totalDownStores == 0 && zabbixDownStore == 0) {
    //   System.out.println("Both Prometheus and zabbbix is 0");
    //   return false;
    // }

    store_count_down countEntity = new store_count_down();
    System.out.println(
      timeStamp + " " + totalDownStores + " " + zabbixDownStore
    );
    countEntity.setDate(timeStamp);
    countEntity.setPromDown(totalDownStores);
    countEntity.setZabbixDown(zabbixDownStore);
    store_count_down_repo.save(countEntity);
    return true;
  }

  @Scheduled(cron = "0 */5 * * * ?", zone = "Asia/Manila")
  public void scheduledSaveCounts() {
    // System.out.println(appEnv);
    if (appEnv.equals("prod")) {
      try {
        LocalDateTime currentDateTime = LocalDateTime.now();
        ZoneId zoneId = ZoneId.of("Asia/Manila");
        Timestamp timestamp = Timestamp.valueOf(
          currentDateTime.atZone(zoneId).toLocalDateTime()
        );
        boolean isSaved = saveCounts(timestamp);
        if (isSaved) {
          System.out.println("Saved counts for Down stores " + timestamp);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      // System.out.println(
      //   "This is a development setup so no saving down counts"
      // );
    }
  }
}
