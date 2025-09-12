package com.tracker_app.tracker.DataSource_Zabbix.Repo;

import com.tracker_app.tracker.DataSource_Zabbix.Entity.triggers;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface triggers_repo extends CrudRepository<triggers, Long> {
    
}
