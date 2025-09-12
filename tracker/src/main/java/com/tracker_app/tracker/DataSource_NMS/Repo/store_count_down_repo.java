package com.tracker_app.tracker.DataSource_NMS.Repo;

import com.tracker_app.tracker.DataSource_NMS.Entity.store_count_down;
import java.security.Timestamp;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface store_count_down_repo
  extends JpaRepository<store_count_down, Integer> {
  Optional<store_count_down> findByDate(Timestamp date);

  @Query(
    value = "SELECT zabbix_down FROM store_count_down WHERE date < ? ORDER BY date DESC LIMIT 1",
    nativeQuery = true
  )
  Integer getPreviousZabbixDownStores();
}
