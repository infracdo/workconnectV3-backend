package com.tracker_app.tracker.DataSource_Tracker.Repo;

import com.tracker_app.tracker.DataSource_Tracker.Entity.auth_user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface auth_user_repo extends CrudRepository<auth_user, Long> {
    @Query(value =
        "SELECT email "
        +"FROM auth_user "
        +"WHERE first_name=:first_name "
        +"AND last_name=:last_name "
        +"AND last_login IS NOT NULL "
        +"AND is_active=1 "  
        , nativeQuery = true)
    public String get_agent_email(
        @Param("first_name") String first_name,
        @Param("last_name") String last_name
    );
}
