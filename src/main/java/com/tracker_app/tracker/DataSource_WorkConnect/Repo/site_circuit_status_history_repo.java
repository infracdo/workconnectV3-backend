package com.tracker_app.tracker.DataSource_WorkConnect.Repo;

import java.util.List;
import java.util.Map;

import com.tracker_app.tracker.DataSource_WorkConnect.Entity.site_circuit_status_history;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface site_circuit_status_history_repo extends CrudRepository<site_circuit_status_history, Long>{
    
}
