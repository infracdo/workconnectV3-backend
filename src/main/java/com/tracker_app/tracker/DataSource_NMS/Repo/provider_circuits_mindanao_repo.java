package com.tracker_app.tracker.DataSource_NMS.Repo;

import com.tracker_app.tracker.DataSource_NMS.Entity.provider_circuits_today;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface provider_circuits_mindanao_repo
  extends JpaRepository<provider_circuits_today, Long> {
  // Mindanao
  @Query(
    value = "SELECT *  FROM provider_circuits_mindanao",
    nativeQuery = true
  )
  List<Map<String, Object>> getMindanaoList();

  @Query(
    value = "SELECT * FROM provider_circuits_mindanao WHERE site_id IN :siteIds",
    nativeQuery = true
  )
  List<Map<String, Object>> getSpecificMindanaoList(
    @Param("siteIds") List<String> siteIds
  );

  @Query(
    value = "SELECT * FROM provider_circuits_mindanao WHERE loopback_1 > '0.00' AND loopback_2 > '0.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getMindanaoDualCircuit();

  @Query(
    value = "SELECT * FROM provider_circuits_mindanao WHERE loopback_1 > '0.00' AND loopback_2 = '0.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getMindanaoPrimaryOnly();

  @Query(
    value = "SELECT * FROM provider_circuits_mindanao WHERE loopback_1 = '0.00' AND loopback_2 > '0.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getMindanaoBackupOnly();

  @Query(
    value = "SELECT * FROM provider_circuits_mindanao WHERE loopback_0 = '0.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getDownStore_7D();

  @Query(
    value = "SELECT * FROM provider_circuits_mindanao WHERE loopback_0 > '0.00' AND loopback_0 < '0.95'",
    nativeQuery = true
  )
  List<Map<String, Object>> getProblematicStore_7D();

  @Query(
    value = "SELECT * FROM provider_circuits_mindanao WHERE loopback_0 >= '0.95' AND loopback_0 < '1.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getHealhyStore7D();

  @Query(
    value = "SELECT *  FROM provider_circuits_mindanao WHERE loopback_0 = '1.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getStore100uptime7D();
}
