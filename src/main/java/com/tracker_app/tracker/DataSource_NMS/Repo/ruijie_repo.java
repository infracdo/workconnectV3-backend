package com.tracker_app.tracker.DataSource_NMS.Repo;

import com.tracker_app.tracker.DataSource_NMS.Entity.ruijie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ruijie_repo extends CrudRepository<ruijie, Long> {
  @Query(
    value = "SELECT vlan10 FROM mac_ruijie where site_id=?1",
    nativeQuery = true
  )
  String getVlan10Provider(String site_id);

  @Query(
    value = "SELECT vlan20 FROM mac_ruijie where site_id=?1",
    nativeQuery = true
  )
  String getVlan20Provider(String site_id);

  @Query(
    value = "SELECT cellular FROM mac_ruijie where site_id=?1",
    nativeQuery = true
  )
  String getCellularProvider(String site_id);

  @Query(
    value = "SELECT * FROM mac_ruijie WHERE site_id = :siteId",
    nativeQuery = true
  )
  Optional<ruijie> findBySite_id(@Param("siteId") String site_id);
}
