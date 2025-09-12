package com.tracker_app.tracker.DataSource_WorkConnect.Repo;

import java.util.List;
import java.util.Map;

import com.tracker_app.tracker.DataSource_WorkConnect.Entity.sites_summary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface sites_summary_repo extends CrudRepository<sites_summary, Long> {
    
    @Query(value = "SELECT * FROM sites_summary", nativeQuery = true)
    public List<sites_summary> get_all();

    @Query(value = "SELECT * FROM sites_summary WHERE site_id = :site_id", nativeQuery = true)
    public sites_summary find_site_by_site_id(
        @Param("site_id") String site_id
    );

    @Query(value = "SELECT * FROM sites_summary ss LEFT JOIN site_location sl ON ss.site_id = sl.site_id "
    +"WHERE (:tenant_name IS NULL OR ss.tenant_name LIKE :tenant_name%) "
    +"AND (:site_id IS NULL OR LOWER(ss.site_id) LIKE %:site_id%)"
    +"AND (:name IS NULL OR LOWER(ss.name) LIKE %:name%)"
    +"AND (:status IS NULL OR LOWER(ss.status) LIKE %:status%)"
    +"AND (:island_group IS NULL OR LOWER(ss.island_group) LIKE %:island_group%)"
    +"AND (:region IS NULL OR LOWER(ss.region) LIKE %:region%)"
    +"AND (:no_links_up IS NULL OR sl.no_links_up = :no_links_up) "
    , nativeQuery = true)
    Page<sites_summary> get_list(
        @Param("site_id") String site_id,
        @Param("tenant_name") String tenant_name,
        @Param("name") String name,
        @Param("status") String status,
        @Param("island_group") String island_group,
        @Param("region") String region,
        @Param("no_links_up") Integer no_links_up,
        Pageable pageable
    );

    @Query(value = "SELECT * FROM sites_summary ss LEFT JOIN site_location sl ON ss.site_id = sl.site_id "
    +"WHERE (:tenant_name IS NULL OR ss.tenant_name LIKE :tenant_name%) "
    +"AND (:name IS NULL OR LOWER(ss.name) LIKE %:name%)"
    +"AND (:status IS NULL OR LOWER(ss.status) LIKE %:status%)"
    +"AND (:island_group IS NULL OR LOWER(ss.island_group) LIKE %:island_group%)"
    +"AND (:region IS NULL OR LOWER(ss.region) LIKE %:region%)"
    +"AND (:no_links_up IS NULL OR sl.no_links_up = :no_links_up) "
    , nativeQuery = true)
    Page<sites_summary> get_list_with_tenant_only(
        @Param("tenant_name") String tenant_name,
        @Param("name") String name,
        @Param("status") String status,
        @Param("island_group") String island_group,
        @Param("region") String region,
        @Param("no_links_up") Integer no_links_up,
        Pageable pageable
    );

}
