package com.tracker_app.tracker.DataSource_Zabbix.Repo;

import com.tracker_app.tracker.DataSource_Zabbix.Entity.hosts;
import com.tracker_app.tracker.DataSource_Zabbix.Entity.items;
import com.tracker_app.tracker.DataSource_Zabbix.Entity.triggers;
import java.util.List;
import java.util.Map;
import javax.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface hosts_repo extends CrudRepository<hosts, Long> {
  @QueryHints(
    @QueryHint(
      name = org.hibernate.annotations.QueryHints.CACHEABLE,
      value = "true"
    )
  )
  @Query(
    value = "SELECT inv.poc_2_phone_a, inv.vendor, inv.chassis, inv.model, h.host, h.hostid, h.name, t.comments, t.priority, t.value, t.triggerid, t.lastchange " +
    "FROM hosts h, triggers t, functions f, items i, hosts_groups hg, host_inventory inv " +
    "WHERE (hg.hostid=h.hostid) " +
    "AND (hg.groupid=:host_group) " +
    "AND (h.hostid=i.hostid) " +
    "AND (i.itemid=f.itemid) " +
    "AND (f.triggerid=t.triggerid) " +
    "AND (t.status=0) " +
    "AND (h.status=0) AND (i.status=0) AND (h.name LIKE :host_name%) AND (inv.hostid=h.hostid) ",
    nativeQuery = true
  )
  List<Map<String, Object>> fetchPSCSites(
    @Param("host_name") String host_name,
    @Param("host_group") Integer host_group
  );

  @QueryHints(
    @QueryHint(
      name = org.hibernate.annotations.QueryHints.CACHEABLE,
      value = "true"
    )
  )
  @Query(
    value = "SELECT h.host, h.hostid, h.name, t.comments, t.priority, t.value, t.triggerid, t.lastchange " +
    "FROM hosts h, triggers t, functions f, items i, hosts_groups hg " +
    "WHERE (hg.hostid=h.hostid) " +
    "AND (hg.groupid=:host_group) " +
    "AND (h.hostid=i.hostid) " +
    "AND (i.itemid=f.itemid) " +
    "AND (f.triggerid=t.triggerid) " +
    "AND (t.status=0) " +
    "AND (h.status=0) AND (i.status=0) AND (h.name LIKE :host_name%) ",
    nativeQuery = true
  )
  List<Map<String, Object>> fetchSites(
    @Param("host_name") String host_name,
    @Param("host_group") Integer host_group
  );

  @Query(
    value = "SELECT h.host, h.hostid, h.name, t.comments, t.priority, t.value, t.triggerid, t.lastchange " +
    "FROM hosts h, triggers t, functions f, items i " +
    "WHERE (h.hostid=i.hostid) " +
    "AND (i.itemid=f.itemid) " +
    "AND (f.triggerid=t.triggerid) " +
    "AND (t.status=0) " +
    "AND (h.status=0) AND (i.status=0) " +
    "AND (:host_name IS NULL OR h.name = :host_name) ",
    nativeQuery = true
  )
  List<Map<String, Object>> fetchZabbixV6Sites(
    @Param("host_name") String host_name
  );

  // @Query(
  //   value = "SELECT h.host, t.priority, t.value,  t.lastchange " +
  //   "FROM hosts h, triggers t, functions f, items i " +
  //   "WHERE (h.hostid=i.hostid) " +
  //   "AND (i.itemid=f.itemid) " +
  //   "AND (f.triggerid=t.triggerid) " +
  //   "AND (t.status=0) " +
  //   "AND (h.status=0) AND (i.status=0) " +
  //   "AND (t.priority = 4 OR t.priority = 3) " +
  //   "AND (h.name IN :host_names OR h.name IS NULL OR h.name = '');",
  //   nativeQuery = true
  // )
  // List<Map<String, Object>> fetchZabbixSites(
  //   @Param("host_names") List<String> hostNames
  // );
  @Query(
    value = "SELECT h.host, t.priority, t.value,  t.lastchange " +
    "FROM hosts h, triggers t, functions f, items i " +
    "WHERE (h.hostid=i.hostid) " +
    "AND (i.itemid=f.itemid) " +
    "AND (f.triggerid=t.triggerid) " +
    "AND (t.status=0) " +
    "AND (h.status=0) AND (i.status=0) " +
    "AND (t.priority = 4) " +
    "AND (h.name IN :host_names OR h.name IS NULL OR h.name = '');",
    nativeQuery = true
  )
  List<Map<String, Object>> fetchZabbixSitesPrimary(
    @Param("host_names") List<String> hostNames
  );

  @Query(
    value = "SELECT h.host, t.priority, t.value,  t.lastchange " +
    "FROM hosts h, triggers t, functions f, items i " +
    "WHERE (h.hostid=i.hostid) " +
    "AND (i.itemid=f.itemid) " +
    "AND (f.triggerid=t.triggerid) " +
    "AND (t.status=0) " +
    "AND (h.status=0) AND (i.status=0) " +
    "AND (t.priority = 3) " +
    "AND (h.name IN :host_names OR h.name IS NULL OR h.name = '');",
    nativeQuery = true
  )
  List<Map<String, Object>> fetchZabbixSitesBackup(
    @Param("host_names") List<String> hostNames
  );

  // @Query(
  //   value = "SELECT h.host, t.priority, t.value,  t.lastchange " +
  //   "FROM hosts h, triggers t, functions f, items i " +
  //   "WHERE (h.hostid=i.hostid) " +
  //   "AND (i.itemid=f.itemid) " +
  //   "AND (f.triggerid=t.triggerid) " +
  //   "AND (t.status=0) " +
  //   "AND (h.status=0) AND (i.status=0) " +
  //   "AND (:host_name IS NULL OR h.name = :host_name); ",
  //   nativeQuery = true
  // )
  // List<Map<String, Object>> fetchZabbixSites(
  //   @Param("host_name") String host_name
  // );

  @Query(
    value = "SELECT t.priority, t.value, inv.vendor, inv.chassis, inv.model, t.lastchange " +
    "FROM hosts h, triggers t, functions f, items i, hosts_groups hg, host_inventory inv " +
    "WHERE (hg.hostid=h.hostid) " +
    "AND (hg.groupid=:host_group) " +
    "AND (h.hostid=i.hostid) " +
    "AND (i.itemid=f.itemid) " +
    "AND (f.triggerid=t.triggerid) " +
    "AND (t.status=0) " +
    "AND (h.status=0) AND (i.status=0) AND (h.name LIKE :host_name%) AND (inv.hostid=h.hostid) ",
    nativeQuery = true
  )
  List<Map<String, Object>> get_circuit_status(
    @Param("host_name") String host_name,
    @Param("host_group") Integer host_group
  );

  //   @Query(
  //     value = "SELECT h.name, " +
  //     "case when t.priority = 5 then 'Store' " +
  //     "when t.priority = 4 then 'Primary' " +
  //     "when t.priority = 3 then 'Secondary' " +
  //     "end as store_and_circuit_label, " +
  //     "FROM_UNIXTIME(event_r.clock) as start, " +
  //     "FROM_UNIXTIME(event.clock) as end, " +
  //     "(event.clock - event_r.clock)/60 as duration_in_min " +
  //     "FROM (SELECT eventid, clock, objectid FROM events WHERE source =0 and object=0 and value=0 order by clock) as event, " +
  //     "(SELECT e.clock, er.r_eventid FROM events e, event_recovery er WHERE er.eventid = e.eventid) as event_r, " +
  //     "hosts h, triggers t, functions f, items i " +
  //     "WHERE (h.hostid=i.hostid) " +
  //     "AND (i.itemid=f.itemid) " +
  //     "AND (f.triggerid=t.triggerid) " +
  //     "AND (t.status=0) " +
  //     "AND (h.status=0) " +
  //     "AND (i.status=0) " +
  //     "AND (h.name like :site_id%) " +
  //     "AND (t.triggerid =event.objectid) " +
  //     "AND (event.eventid =event_r.r_eventid) " +
  //     "AND (YEAR(FROM_UNIXTIME(event_r.clock))=:year AND MONTH(FROM_UNIXTIME(event_r.clock))=:month)",
  //     nativeQuery = true
  //   )
  //   List<Map<String, Object>> get_downtime_report(
  //     @Param("site_id") String site_id,
  //     @Param("month") String month,
  //     @Param("year") String year
  //   );
  @Query(
    value = "SELECT h.name, " +
    "CASE WHEN t.priority = 5 THEN 'Store' " +
    "WHEN t.priority = 4 THEN 'Primary' " +
    "WHEN t.priority = 3 THEN 'Secondary' " +
    "END AS store_and_circuit_label, " +
    "FROM_UNIXTIME(event_r.clock) AS start, " +
    "FROM_UNIXTIME(event.clock) AS end, " +
    "(event.clock - event_r.clock)/60 AS duration_in_min " +
    "FROM (SELECT eventid, clock, objectid FROM events WHERE source =0 AND object=0 AND value=0 ORDER BY clock) AS event, " +
    "(SELECT e.clock, er.r_eventid FROM events e, event_recovery er WHERE er.eventid = e.eventid) AS event_r, " +
    "hosts h, triggers t, functions f, items i " +
    "WHERE (h.hostid=i.hostid) " +
    "AND (i.itemid=f.itemid) " +
    "AND (f.triggerid=t.triggerid) " +
    "AND (t.status=0) " +
    "AND (h.status=0) " +
    "AND (i.status=0) " +
    "AND (h.name like :storeId%) " +
    "AND (t.triggerid = event.objectid) " +
    "AND (event.eventid = event_r.r_eventid) " +
    "AND (event_r.clock >= :fromDate AND event_r.clock <= :toDate);",
    nativeQuery = true
  )
  List<Map<String, Object>> get_downtime_report(
    @Param("storeId") String storeId,
    @Param("fromDate") Long fromDate,
    @Param("toDate") Long toDate
  );
}
