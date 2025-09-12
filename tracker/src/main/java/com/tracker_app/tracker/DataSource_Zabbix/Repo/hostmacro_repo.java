package com.tracker_app.tracker.DataSource_Zabbix.Repo;

import javax.persistence.QueryHint;

import com.tracker_app.tracker.DataSource_Zabbix.Entity.hostmacro;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface hostmacro_repo extends CrudRepository<hostmacro, Long>{
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    @Query(value =
        "SELECT value "
        +"FROM hostmacro "
        +"WHERE macro LIKE :key% AND hostid=:hostid" 
        , nativeQuery = true)
    String getStoreCircuitIP(
        @Param("key") String key,
        @Param("hostid") Long hostid);
}
