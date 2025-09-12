package com.tracker_app.tracker.DataSource_NMS.Repo;

import com.tracker_app.tracker.DataSource_NMS.Entity.store_count_today;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface store_count_today_repo extends JpaRepository<store_count_today, Long> {
  @Query(value = "SELECT zabbix_down_stores FROM store_counts_today where id=1", nativeQuery = true)
  Integer getZabbixDownStores();

}
