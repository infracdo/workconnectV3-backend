package com.tracker_app.tracker.DataSource_WorkConnect.Repo;

import java.util.List;
import java.util.Map;

import com.tracker_app.tracker.DataSource_WorkConnect.Entity.site_circuit_status;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface site_circuit_status_repo extends CrudRepository<site_circuit_status, Long>{
    @Query(value = "SELECT * FROM site_circuit_status "
    +"WHERE site_id = :site_id "
    +"AND circuit_type = :circuit_type", nativeQuery = true)
    public site_circuit_status find_circuit_by_site_id(
        @Param("site_id") String site_id,
        @Param("circuit_type") String circuit_type
    );

    @Query(value = "SELECT * FROM site_circuit_status "
    +"WHERE site_id = :site_id ", nativeQuery = true)
    List<site_circuit_status> get_circuit_by_site_id(
        @Param("site_id") String site_id
    );
}
