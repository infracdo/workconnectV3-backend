package com.tracker_app.tracker.DataSource_WorkConnect.Repo;

import java.util.List;

import com.tracker_app.tracker.DataSource_WorkConnect.Entity.agent_latest_location;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface agent_latest_location_repo extends CrudRepository<agent_latest_location, Long> {
    @Query(value = "SELECT * FROM agent_latest_location", nativeQuery = true)
    List<agent_latest_location> get_agent_location();

    @Query(value = "SELECT * FROM agent_latest_location WHERE name=:name", nativeQuery = true)
    agent_latest_location get_agent(
        @Param("name") String name
    );
}
