package com.tracker_app.tracker.DataSource_NMS.Repo;

import com.tracker_app.tracker.DataSource_NMS.Entity.mikrotik;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface mikrotik_repo extends CrudRepository<mikrotik, Long> {
  List<mikrotik> findById(long id);

  @Query(
    value = "SELECT * FROM mac_mikrotik WHERE site_id = :siteId",
    nativeQuery = true
  )
  Optional<mikrotik> findBySiteId(@Param("siteId") String siteId);
}
