package com.tracker_app.tracker.DataSource_NMS.Repo;

import com.tracker_app.tracker.DataSource_NMS.Entity.automated_imsi;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface automated_imsi_repo
  extends JpaRepository<automated_imsi, Integer> {
  @Query(
    value = "SELECT MAX(CASE WHEN port = 'port-1' THEN imsi END) AS imsi_primary, MAX(CASE WHEN port = 'port-2' THEN imsi END) AS imsi_backup FROM automated_imsi WHERE site_id = :siteId AND port IN ('port-1', 'port-2');",
    nativeQuery = true
  )
  Map<String, Object> findImsiBySiteId(@Param("siteId") String siteId);
}
