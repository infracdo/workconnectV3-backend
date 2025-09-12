package com.tracker_app.tracker.DataSource_NMS.Service;

import com.fasterxml.jackson.databind.annotation.JsonAppend.Attr;
import com.tracker_app.tracker.DataSource_NMS.Entity.provider_circuits_today;
import com.tracker_app.tracker.DataSource_NMS.Entity.store_count;
import com.tracker_app.tracker.DataSource_NMS.Repo.circuit_repo;
import com.tracker_app.tracker.DataSource_NMS.Repo.provider_circuits_today_repo;
import com.tracker_app.tracker.DataSource_NMS.Repo.store_count_repo;
import com.tracker_app.tracker.DataSource_NMS.Repo.store_count_today_repo;
import com.tracker_app.tracker.DataSource_NMS.Repo.store_data_repo;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class store_count_service {

  @Autowired
  private store_count_repo store_count_repo;

  @Autowired
  private store_data_repo store_data_repo;

  @Autowired
  private circuit_service circuit_service;

  @Autowired
  private circuit_repo circuit_repo;

  @Autowired
  private store_count_today_repo store_count_today_repo;

  @Value("${application.env}")
  String appEnv;

  @Autowired
  private provider_circuits_today_repo provider_circuits_today_repo;

  double thresholdValue = 0.95;

  public boolean saveCounts(Timestamp timeStamp) {
    // Down Store
    int totalDownStores = circuit_service.getAllDownStoresSiteId().size();
    int zabbixDownStore = store_count_today_repo.getZabbixDownStores();

    int dualCircuit = provider_circuits_today_repo.getDualCircuit7D().size();
    int primaryOnly = provider_circuits_today_repo.getPrimaryOnly7D().size();
    int backupOnly = provider_circuits_today_repo.getBackupOnly7D().size();

    int problematicStores0_7d = provider_circuits_today_repo
      .getProblematicStore0up7Days()
      .size();
    int problematicStoresLess95_7d = provider_circuits_today_repo
      .getProblematicStore95up7Days(thresholdValue)
      .size();
    int loopback0_Healthy7D = provider_circuits_today_repo
      .getHealhyLoopback0_7D()
      .size();
    int loopback0_100up7D = provider_circuits_today_repo
      .getLoopback0_100uptime7D()
      .size();

    if (
      dualCircuit == 0 &&
      primaryOnly == 0 &&
      backupOnly == 0 &&
      problematicStores0_7d == 0 &&
      problematicStoresLess95_7d == 0 &&
      loopback0_Healthy7D == 0 &&
      loopback0_100up7D == 0
    ) {
      return false;
    }

    store_count countEntity = new store_count();

    countEntity.setDate(timeStamp);
    countEntity.setDownStore(totalDownStores);
    countEntity.setZabbixDownStores(zabbixDownStore);
    countEntity.setDualCircuit7D(dualCircuit);
    countEntity.setPrimaruOnly7D(primaryOnly);
    countEntity.setBackupOnly7D(backupOnly);

    countEntity.setProblematicStoreCircuitZeroUptime7D(problematicStores0_7d);
    countEntity.setProblematicStoreCircuit95Uptime7D(
      problematicStoresLess95_7d
    );
    countEntity.setStore100uptime7D(loopback0_100up7D);
    countEntity.setStoreHealthy7d(loopback0_Healthy7D);
    store_count_repo.save(countEntity);

    return true;
  }

  @Scheduled(cron = "0 0 * * * ?", zone = "Asia/Manila")
  // @Scheduled(cron = "50 10 * * * *", zone = "Asia/Manila")
  public void scheduledSaveCounts() {
    if (appEnv.equals("prod")) {
      try {
        // LocalDateTime currentDateTime = LocalDateTime.now();
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        //   "yyyy-MM-dd HH:mm"
        // );

        LocalDateTime currentDateTime = LocalDateTime.now();
        ZoneId zoneId = ZoneId.of("Asia/Manila");
        Timestamp timestamp = Timestamp.valueOf(
          currentDateTime.atZone(zoneId).toLocalDateTime()
        );
        boolean isSaved = saveCounts(timestamp);
        if (isSaved) {
          System.out.println("Saved counts for " + timestamp);
        }
        // Optional<store_count> existingCounts = store_count_repo.findByDate(
        //   timestamp
        // );

        // System.out.println(existingCounts);
        // if (existingCounts.isPresent()) {
        //   store_count storeCountDate = existingCounts.get();
        //   Timestamp dateFromExistingCounts = storeCountDate.getDate();
        //   System.out.println(
        //     "Date: " + dateFromExistingCounts + " already exist."
        //   );
        // } else {
        //   // System.out.println("Saving counts in " + randomNum + " seconds");
        //   saveCounts(timestamp);
        //   System.out.println("Saved counts for " + timestamp);
        // }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
