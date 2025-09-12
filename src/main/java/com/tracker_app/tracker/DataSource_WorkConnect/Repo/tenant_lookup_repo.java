package com.tracker_app.tracker.DataSource_WorkConnect.Repo;

import java.util.List;

import com.tracker_app.tracker.DataSource_WorkConnect.Entity.tenant_lookup;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface tenant_lookup_repo extends CrudRepository<tenant_lookup, Long> {
    @Query(value = "SELECT name FROM tenant_lookup ", nativeQuery = true)
    String get_tenant_name();

    @Query(value = "SELECT name FROM tenant_lookup WHERE slug=:slug", nativeQuery = true)
    String get_tenant_by_slug(
        @Param("slug") String slug
    );

    @Query(value = "SELECT name FROM tenant_lookup WHERE prom_name=:promname", nativeQuery = true)
    String get_tenant_by_promname(
        @Param("promname") String promname
    );

    @Query(value = "SELECT slug FROM tenant_lookup WHERE name=:name", nativeQuery = true)
    String get_tenantslug_by_name(
        @Param("name") String name
    );
    @Query(value = "SELECT prom_name FROM tenant_lookup WHERE slug=:slug", nativeQuery = true)
    String get_promname_by_slug(
        @Param("slug") String slug
    );
    @Query(value = "SELECT * FROM tenant_lookup WHERE name=:name", nativeQuery = true)
    tenant_lookup get_lookup_by_name(
        @Param("name") String name
    );
}
