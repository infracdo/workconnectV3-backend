package com.tracker_app.tracker.DataSource_NMS.Repo;

import com.tracker_app.tracker.DataSource_NMS.Entity.store_data;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface store_data_repo extends CrudRepository<store_data, Long> {
  Optional<store_data> findBySiteId(String siteId);

  @Query(
    value = "SELECT primary_circuit_status, primary_provider, backup_circuit_status, backup_provider FROM store_data WHERE site_id = :siteId",
    nativeQuery = true
  )
  List<Object[]> findCircuitAndProviderDetailsBySiteId(
    @Param("siteId") String siteId
  );

  @Query(
    value = "SELECT * FROM store_data WHERE site_id = :siteId",
    nativeQuery = true
  )
  Map<String, Object> findDetailsBySiteId(@Param("siteId") String siteId);

  @Query(
    value = "SELECT :fieldName FROM store_data WHERE site_id = :siteId",
    nativeQuery = true
  )
  String findFieldValueBySiteId(
    @Param("siteId") String siteId,
    @Param("fieldName") String fieldName
  );

  @Query(
    value = "SELECT EXISTS(SELECT 1 FROM store_data WHERE site_id = :siteId)",
    nativeQuery = true
  )
  boolean existsBySiteId(@Param("siteId") String siteId);

  // Problematic Stores
  @Query(
    value = "SELECT * FROM store_data WHERE loopback_0 = 0",
    nativeQuery = true
  )
  List<Map<String, Object>> getProblematicStore0up7Days();

  @Query(
    value = "SELECT * FROM store_data WHERE loopback_0 > 0 AND loopback_0 < :threshold",
    nativeQuery = true
  )
  List<Map<String, Object>> getProblematicStore95up7Days(
    @Param("threshold") Double threshold
  );

  @Query(
    value = "SELECT * FROM store_data WHERE loopback_0_24h > 0 AND loopback_0_24h < :threshold",
    nativeQuery = true
  )
  List<Map<String, Object>> getProblematicStore95up24h(
    @Param("threshold") Double threshold
  );

  @Query(
    value = "SELECT * FROM store_data WHERE LOWER(wireless_only) = 'true' AND LOWER(router)='mikrotik'",
    nativeQuery = true
  )
  List<Map<String, Object>> getWirelessOnlyTableList();

  @Query(
    value = "SELECT * FROM store_data WHERE LOWER(single_circuit) = 'true' AND LOWER(router)='mikrotik'",
    nativeQuery = true
  )
  List<Map<String, Object>> getSingleCircuitTableList();

  // Primary

  @Query(
    value = "SELECT * FROM store_data WHERE loopback_1 = 0 ",
    nativeQuery = true
  )
  List<Map<String, Object>> getPrimaryCircuit0up7Days();

  @Query(
    value = "SELECT * FROM store_data WHERE loopback_1 = 1 ",
    nativeQuery = true
  )
  List<Map<String, Object>> getPrimaryCircuit100up7Days();

  @Query(
    value = "SELECT * FROM store_data WHERE loopback_1 < :threshold AND loopback_1 > 0",
    nativeQuery = true
  )
  List<Map<String, Object>> getPrimaryCircuit95up7Days(
    @Param("threshold") Double threshold
  );

  @Query(
    value = "SELECT * FROM store_data WHERE loopback_1_24h < :threshold AND loopback_1_24h > 0",
    nativeQuery = true
  )
  List<Map<String, Object>> getPrimaryCircuit95up24h(
    @Param("threshold") Double threshold
  );

  @Query(
    value = "SELECT *  FROM store_data WHERE LOWER(primary_circuit_status) = 'no internet'",
    nativeQuery = true
  )
  List<Map<String, Object>> getPrimaryCircuitNoInternetTableList();

  @Query(
    value = "SELECT * FROM store_data WHERE LOWER(primary_provider) = 'no provider'",
    nativeQuery = true
  )
  List<Map<String, Object>> getPrimaryCircuitNoProviderTableList();

  // Backup
  @Query(
    value = "SELECT * FROM store_data WHERE loopback_2 = 0",
    nativeQuery = true
  )
  List<Map<String, Object>> getBackupCircuit0up7Days();

  @Query(
    value = "SELECT * FROM store_data WHERE loopback_2 = 1",
    nativeQuery = true
  )
  List<Map<String, Object>> getBackupCircuit100up7Days();

  @Query(
    value = "SELECT * FROM store_data WHERE loopback_2 < :threshold AND loopback_2 > 0",
    nativeQuery = true
  )
  List<Map<String, Object>> getBackupCircuit95up7Days(
    @Param("threshold") Double threshold
  );

  @Query(
    value = "SELECT * FROM store_data WHERE loopback_2_24h < :threshold AND loopback_2_24h > 0",
    nativeQuery = true
  )
  List<Map<String, Object>> getBackupCircuit95up24h(
    @Param("threshold") Double threshold
  );

  @Query(
    value = "SELECT * FROM store_data WHERE LOWER(backup_circuit_status) = 'no internet'",
    nativeQuery = true
  )
  List<Map<String, Object>> getBackupCircuitNoInternetTableList();

  @Query(
    value = "SELECT * FROM store_data WHERE LOWER(backup_provider) = 'no provider'",
    nativeQuery = true
  )
  List<Map<String, Object>> getBackupCircuitNoProviderTableList();

  // All stores

  @Query(value = "SELECT * FROM store_data", nativeQuery = true)
  List<Map<String, Object>> getAllStoreData();

  @Query(
    value = "SELECT DISTINCT * FROM store_data s WHERE s.site_id IN :siteIds",
    nativeQuery = true
  )
  List<Map<String, Object>> getStoreDataForSiteIds(
    @Param("siteIds") List<String> siteIds
  );

  @Query(
    value = "SELECT * FROM store_data WHERE loopback_1 > 0.00 AND loopback_2 > 0.00",
    nativeQuery = true
  )
  List<Map<String, Object>> getDualCircuit7D();

  @Query(
    value = "SELECT * FROM store_data WHERE loopback_1 > 0.00 AND loopback_2 = 0.00",
    nativeQuery = true
  )
  List<Map<String, Object>> getPrimaryOnly7D();

  @Query(
    value = "SELECT * FROM store_data WHERE loopback_1 = 0.00 AND loopback_2 > 0.00",
    nativeQuery = true
  )
  List<Map<String, Object>> getBackupOnly7D();

  @Query(
    value = "SELECT * FROM store_data WHERE loopback_0 >= 0.95 AND loopback_0 < 1.00",
    nativeQuery = true
  )
  List<Map<String, Object>> getHealhyLoopback0_7D();

  @Query(
    value = "SELECT *  FROM store_data WHERE loopback_0 = 1.00",
    nativeQuery = true
  )
  List<Map<String, Object>> getLoopback0_100uptime7D();
}
