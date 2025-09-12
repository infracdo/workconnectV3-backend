package com.tracker_app.tracker.DataSource_Zabbix.Repo;

import java.util.List;
import java.util.Map;

import javax.persistence.QueryHint;

import com.tracker_app.tracker.DataSource_Zabbix.Entity.zabbix_interface;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface zabbix_interface_repo extends CrudRepository<zabbix_interface, Long> {
    
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    @Query(value =
        "SELECT ip "
        +"FROM interface "
        +"WHERE hostid=:hostid AND useip=1 limit 1" 
        , nativeQuery = true)
    String getStoreVIP(
        @Param("hostid") Long hostid);
}
