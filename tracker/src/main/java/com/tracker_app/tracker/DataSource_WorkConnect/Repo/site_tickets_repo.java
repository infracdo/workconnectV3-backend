package com.tracker_app.tracker.DataSource_WorkConnect.Repo;

import java.util.List;
import java.util.Map;

import com.tracker_app.tracker.DataSource_WorkConnect.Entity.site_tickets;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface site_tickets_repo extends CrudRepository<site_tickets, Long>{
    @Query(value = "SELECT * FROM site_tickets "
    +"WHERE site_id = :site_id AND id = :id", nativeQuery = true)
    site_tickets find_by_ticket_id(
        @Param("site_id") String site_id,
        @Param("id") Long id
    );
}
