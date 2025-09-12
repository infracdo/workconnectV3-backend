package com.tracker_app.tracker.DataSource_NMS.Repo;

import com.tracker_app.tracker.DataSource_NMS.Entity.atis_inventory;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface atis_inventory_repo
    extends CrudRepository<atis_inventory, Long> {

  @Query(value = "SELECT * from atis_inventory where provider=?1", nativeQuery = true)
  List<Map<String, Object>> getListByProvider(String provider);
}
