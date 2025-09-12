package com.tracker_app.tracker.DataSource_Tracker.Repo;

import java.util.List;
import java.util.Map;

import com.tracker_app.tracker.DataSource_Tracker.Entity.tracker_location;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface tracker_location_repo extends CrudRepository<tracker_location, Long> {
    @Query(value =
        "SELECT tl.latitude, tl.longitude, tl.date "
        +"FROM tracker_location tl, tracker_agent ta "
        +"WHERE ta.last_name=:last_name "
        +"AND ta.first_name=:first_name "
        +"AND tl.agent_code=ta.agent_code "
        +"ORDER BY tl.date DESC "
        , nativeQuery = true)
    List<Map<String, Object>> get_agent_location(
        @Param("first_name") String first_name,
        @Param("last_name") String last_name
    ); 
    
    @Query(value =
        "SELECT ta.first_name, ta.last_name, tl.latitude, tl.longitude, tl.date "
        +"FROM tracker_location tl, tracker_agent ta "
        +"WHERE tl.agent_code=ta.agent_code "
        +"AND (ta.first_name=:first_name AND ta.last_name=:last_name) "
        +"ORDER BY tl.date DESC limit 5 "
        , nativeQuery = true)
    List<Map<String, Object>> get_agent_latest_location(
        @Param("first_name") String first_name,
        @Param("last_name") String last_name
    );
}
