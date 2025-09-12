package com.tracker_app.tracker.DataSource_Zabbix.Repo;

import com.tracker_app.tracker.DataSource_Zabbix.Entity.event_recovery;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface event_recovery_repo extends CrudRepository<event_recovery, Long> {
    
}
