package com.tracker_app.tracker.DataSource_NMS.Repo;

import com.tracker_app.tracker.DataSource_NMS.Entity.provider_circuits_today;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface provider_circuits_today_repo
  extends JpaRepository<provider_circuits_today, Long> {
  provider_circuits_today findBySiteId(String siteId);

  @Modifying
  @Query(
    value = "UPDATE provider_circuits_today SET public_ip_primary = :primary, public_ip_backup = :backup, provider_primary = :primaryProvider, provider_backup = :backupProvider WHERE site_id = :siteId",
    nativeQuery = true
  )
  void updateProviderCircuit(
    @Param("primary") String primary,
    @Param("backup") String backup,
    @Param("primaryProvider") String primaryProvider,
    @Param("backupProvider") String backupProvider,
    @Param("siteId") String siteId
  );

  @Query(
    value = "SELECT mac_primary, mac_backup, gtw_primary, gtw_backup FROM provider_circuits_today WHERE site_id = :siteId",
    nativeQuery = true
  )
  Map<String, Object> findMacAndGtwBySiteId(@Param("siteId") String siteId);

  @Query(
    value = "SELECT mac_primary, mac_backup, gtw_primary, gtw_backup,  public_ip_primary, public_ip_backup, provider_primary, provider_backup FROM provider_circuits_today WHERE site_id = :siteId",
    nativeQuery = true
  )
  Map<String, Object> findMacAndPublicIpBySiteId(
    @Param("siteId") String siteId
  );

  @Query(value = "SELECT * FROM provider_circuits_today", nativeQuery = true)
  List<Map<String, Object>> getProviderCircuitsToday();

  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loopback_1 > '0.00' AND loopback_2 > '0.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getDualCircuit7D();

  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loopback_1 > '0.00' AND loopback_2 > '0.00' AND loopback_0 = '0.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getDualCircuitButDownStore7D();

  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loopback_1 > '0.00' AND loopback_2 = '0.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getPrimaryOnly7D();

  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loopback_1 = '0.00' AND loopback_2 > '0.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getBackupOnly7D();

  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loopback_3 = 1.00",
    nativeQuery = true
  )
  List<Map<String, Object>> getThirdOnly7D();

  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loopback_1_24h > '0.00' AND loopback_2_24h > '0.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getDualCircuit24H();

  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loopback_1_24H > '0.00' AND loopback_2_24H > '0.00' AND loopback_0_24H = '0.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getDualCircuitButDownStore24H();

  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loopback_1_24h > '0.00' AND loopback_2_24h = '0.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getPrimaryOnly24H();

  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loopback_1_24h = '0.00' AND loopback_2_24h > '0.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getBackupOnly24H();

  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loppback_3_24h = 1.00",
    nativeQuery = true
  )
  List<Map<String, Object>> getThirdOnly24H();

  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loopback_0 = '0.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getDownStore_7D();

  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loopback_0 > '0.00' AND loopback_0 < '0.95'",
    nativeQuery = true
  )
  List<Map<String, Object>> getProblematicStore_7D();

  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loopback_0 >= '0.95' AND loopback_0 < '1.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getHealhyStore7D();

  @Query(
    value = "SELECT *  FROM provider_circuits_today WHERE loopback_0 = '1.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getStore100uptime7D();

  // 24 hours
  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loopback_0_24h = '0.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getDownStore_24H();

  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loopback_0_24h > '0.00' AND loopback_0_24h < '0.95'",
    nativeQuery = true
  )
  List<Map<String, Object>> getProblematicStore_24H();

  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loopback_0_24h >= '0.95' AND loopback_0_24h < '1.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getHealhyStore24H();

  @Query(
    value = "SELECT *  FROM provider_circuits_today WHERE loopback_0_24h = '1.00'",
    nativeQuery = true
  )
  List<Map<String, Object>> getStore100uptime24H();

  @Query(
    value = "SELECT MAX(date_timestamp) FROM provider_circuits_today;",
    nativeQuery = true
  )
  String getLatestTimeStampPCT();

  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loopback_0 = 0",
    nativeQuery = true
  )
  List<Map<String, Object>> getProblematicStore0up7Days();

  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loopback_0 > 0 AND loopback_0 < :threshold",
    nativeQuery = true
  )
  List<Map<String, Object>> getProblematicStore95up7Days(
    @Param("threshold") Double threshold
  );

  @Query(
    value = "SELECT * FROM provider_circuits_today WHERE loopback_0 >= 0.95 AND loopback_0 < 1.00",
    nativeQuery = true
  )
  List<Map<String, Object>> getHealhyLoopback0_7D();

  @Query(
    value = "SELECT *  FROM provider_circuits_today WHERE loopback_0 = 1.00",
    nativeQuery = true
  )
  List<Map<String, Object>> getLoopback0_100uptime7D();

  @Query(
    value = "SELECT provider_primary, site_id FROM provider_circuits_today WHERE site_id IN :siteIds",
    nativeQuery = true
  )
  List<Map<String, Object>> getPrimaryProvider(List<String> siteIds);

  @Query(
    value = "SELECT provider_backup, site_id FROM provider_circuits_today WHERE site_id IN :siteIds",
    nativeQuery = true
  )
  List<Map<String, Object>> getBackupProvider(List<String> siteIds);
}
