package com.tracker_app.tracker.DataSource_Zabbix.Repo;

import com.tracker_app.tracker.DataSource_Zabbix.Entity.items;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface items_repo extends CrudRepository<items, Long> {
    
}
