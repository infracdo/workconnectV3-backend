package com.tracker_app.tracker.DataSource_Zabbix.Repo;

import com.tracker_app.tracker.DataSource_Zabbix.Entity.events;
import java.util.List;
import java.util.Map;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface events_repo extends CrudRepository<events, Long> {
  @QueryHints(
    @QueryHint(
      name = org.hibernate.annotations.QueryHints.CACHEABLE,
      value = "true"
    )
  )
  @Query(
    value = "SELECT e.eventid, e.clock " +
    "FROM events e " +
    "WHERE e.source=0 " +
    "AND e.object=0 " +
    "AND e.value=0 " +
    "AND e.objectid=:triggerid " +
    "AND (YEAR(FROM_UNIXTIME(e.clock)) like :year AND MONTH(FROM_UNIXTIME(e.clock)) like :month) " +
    "ORDER BY e.clock DESC",
    nativeQuery = true
  )
  List<Map<String, Object>> getevents(
    @Param("triggerid") Long triggerid,
    @Param("month") String month,
    @Param("year") String year
  );

  @Query(
    value = "SELECT e.eventid, e.clock " +
    "FROM events e " +
    "WHERE e.source = 0 " +
    "AND e.object = 0 " +
    "AND e.value = 0 " +
    "AND (:triggerid IS NULL OR e.objectid = :triggerid) " +
    "AND e.clock BETWEEN :fromDate AND :toDate " +
    "ORDER BY e.clock DESC",
    nativeQuery = true
  )
  List<Map<String, Object>> getEventsInRange(
    @Param("triggerid") Long triggerid,
    @Param("fromDate") Long fromDate,
    @Param("toDate") Long toDate
  );

  @QueryHints(
    @QueryHint(
      name = org.hibernate.annotations.QueryHints.CACHEABLE,
      value = "true"
    )
  )
  @Query(
    value = "SELECT e.clock " +
    "FROM events e, event_recovery er " +
    "WHERE er.eventid = e.eventid " +
    "AND er.r_eventid=:eventid limit 1",
    nativeQuery = true
  )
  Integer getEventsEventRecovery(@Param("eventid") Long eventid);
}
