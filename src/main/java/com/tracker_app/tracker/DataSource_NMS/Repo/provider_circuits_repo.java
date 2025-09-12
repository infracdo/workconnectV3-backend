package com.tracker_app.tracker.DataSource_NMS.Repo;

import com.tracker_app.tracker.DataSource_NMS.Entity.provider_circuit;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface provider_circuits_repo
  extends CrudRepository<provider_circuit, Long> {
  @Query(
    value = "SELECT vlan10 FROM mac_ruijie where site_id=?1",
    nativeQuery = true
  )
  String getPrimaryProvider(String site_id);

  @Query(
    value = "SELECT vlan20 FROM mac_ruijie where site_id=?1",
    nativeQuery = true
  )
  String getBackUpProvider(String site_id);

  @Query(
    value = "SELECT DISTINCT ON (site_id)* FROM provider_circuits ORDER BY site_id, date_timestamp DESC;",
    nativeQuery = true
  )
  List<provider_circuit> getLatestData();

  @Query(
    value = "SELECT mac_primary, mac_backup,gtw_primary, gtw_backup ,public_ip_primary, public_ip_backup, provider_primary, provider_backup, serial_number FROM provider_circuits_today WHERE site_id = :siteId",
    nativeQuery = true
  )
  Map<String, Object> findDetailsProviderCircuitBySiteId(
    @Param("siteId") String siteId
  );

  @Query(
    value = "SELECT provider FROM circuit_provider WHERE CAST(?1 AS inet) BETWEEN CAST(start_ip AS inet) AND CAST(end_ip AS inet) LIMIT 1",
    nativeQuery = true
  )
  String findProviderByIp(String publicIp);
  //SELECT provider
  //FROM circuit_provider
  //WHERE '110.54.128.213' BETWEEN start_ip AND end_ip;
}
