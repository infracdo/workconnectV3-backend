package com.tracker_app.tracker.DataSource_WorkConnect.Repo;

import java.util.List;
import java.util.Map;

import com.tracker_app.tracker.DataSource_WorkConnect.Entity.site_location;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface site_location_repo extends CrudRepository<site_location, Long> {
    
    @Query(value = "SELECT * FROM site_location WHERE site_id = :site_id", nativeQuery = true)
    public site_location find_location_by_site_id(
        @Param("site_id") String site_id
    );

    @Query(value = "SELECT site_id, name, latitude, longitude, no_links_up FROM site_location "
    +"WHERE tenant = :tenant", nativeQuery = true)
    List<Map<String, Object>> find_location_by_tenant(
        @Param("tenant") String tenant
    );

    @Query(value = "SELECT DISTINCT(sl.site_id), sl.name, sl.latitude, sl.longitude, sl.no_links_up, "
    +"GROUP_CONCAT(cs.circuit_type separator ',') AS circuit_type, "
    +"GROUP_CONCAT(cs.circuit_provider separator ',') AS circuit_provider, "
    +"GROUP_CONCAT(cs.circuit_status separator ',') AS circuit_status "
    +"FROM site_location sl "
    +"LEFT JOIN site_circuit_status cs ON sl.site_id = cs.site_id "
    +"WHERE (sl.site_id = cs.site_id) "
    +"AND (NOT (cs.circuit_type = \"LOOPBACK 0\")) "
    +"AND (sl.tenant LIKE :tenant%) "
    +"AND (:circuit_provider IS NULL OR cs.circuit_provider LIKE :circuit_provider%) "
    +"GROUP BY sl.site_id "
    , nativeQuery = true)
    List<Map<String, Object>> find_site_location(
        @Param("tenant") String tenant,
        @Param("circuit_provider") String circuit_provider
    );

}
