package com.tracker_app.tracker.DataSource_Tracker.Repo;

import java.util.List;
import java.util.Map;

import com.tracker_app.tracker.DataSource_Tracker.Entity.tracker_agent;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface tracker_agent_repo extends CrudRepository<tracker_agent, Long> {
    @Query(value =
        "SELECT * "
        +"FROM tracker_agent "
        +"WHERE status='ACTIVE' "
        +"AND first_name=:first_name "
        +"AND last_name=:last_name "  
        , nativeQuery = true)
    public tracker_agent getagent(
        @Param("first_name") String first_name,
        @Param("last_name") String last_name
    );

    @Query(value =
        "SELECT first_name, last_name "
        +"FROM tracker_agent WHERE status='ACTIVE' "
        , nativeQuery = true)
    List<Map<String, Object>> get_agent_name(
    );

}
