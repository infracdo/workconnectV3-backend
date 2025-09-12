package com.tracker_app.tracker.DataSource_Radius.Repo;

import com.tracker_app.tracker.DataSource_Radius.Entity.radacct;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface radacct_repo extends CrudRepository<radacct, Long> {
  @QueryHints(
    @QueryHint(
      name = org.hibernate.annotations.QueryHints.CACHEABLE,
      value = "true"
    )
  )
  // @Query(value =
  //     "select distinct racct.nasipaddress, "
  //     +"sum(racct.acctsessiontime) as total_time, "
  //     +"avg(racct.acctsessiontime) as average_time, "
  //     +"count(distinct racct.username) as number_of_users "
  //     +"from radacct racct, radpostauth radauth "
  //     +"where (racct.username = radauth.username) "
  //     +"and (racct.acctstarttime = radauth.authdate)"
  //     +"and (racct.nasipaddress like :site_ip_address%) "
  //     +"and (date(racct.acctstarttime) between date(:start_date) and date(:end_date)) group by racct.nasipaddress;"
  //     , nativeQuery = true)
  // List<Map<String, Object>> get_duration_and_number_of_users(
  //     @Param("site_ip_address") String site_ip_address,
  //     @Param("start_date") String start_date,
  //     @Param("end_date") String end_date
  // );
  // @Query(
  //   value = "SELECT " +
  //   "racct.nasipaddress," +
  //   "SUM(racct.acctsessiontime) AS total_time," +
  //   "AVG(racct.acctsessiontime) AS average_time," +
  //   "COUNT(DISTINCT racct.username) AS number_of_users " +
  //   "FROM " +
  //   "radacct racct " +
  //   "WHERE " +
  //   "racct.nasipaddress LIKE CONCAT(:site_ip_address, '%') " +
  //   "AND racct.acctstarttime BETWEEN DATE(:start_date) AND DATE(:end_date) " +
  //   "GROUP BY " +
  //   "racct.nasipaddress",
  //   nativeQuery = true
  // )
  // List<Map<String, Object>> get_duration_and_number_of_users(
  //   @Param("site_ip_address") String site_ip_address,
  //   @Param("start_date") String start_date,
  //   @Param("end_date") String end_date
  // );
  @Query(
    value = "SELECT " +
    "racct.nasipaddress AS nasipaddress, " +
    " AVG(CASE WHEN racct.acctterminatecause <> '' THEN racct.acctsessiontime ELSE NULL END) AS average_time, " +
    "COUNT(CASE WHEN racct.acctsessiontime > 0 THEN 1 END) AS session_count " +
    "FROM radacct racct " +
    "WHERE " +
    "racct.acctsessiontime > 0 AND " +
    "racct.acctstarttime BETWEEN DATE_FORMAT(:start_date, '%Y-%m-%d 00:00:00') AND DATE_FORMAT(:end_date, '%Y-%m-%d 23:59:59') AND " +
    "racct.acctterminatecause <> '' " +
    "GROUP BY " +
    "racct.nasipaddress"
  )
  List<Map<String, Object>> getAverageTimeAndSessionCount(
    @Param("start_date") Date startDate,
    @Param("end_date") Date endDate
  );

  @Query(
    value = "SELECT " +
    " racct.acctsessiontime AS sessionTime, " +
    " racct.acctstarttime AS startTime, " +
    " racct.acctstoptime AS stopTime, " +
    " racct.acctterminatecause AS terminateCause, " +
    " racct.callingstationid AS callingStationId, " +
    " racct.acctinputoctets AS inputoctets, " +
    " racct.acctoutputoctets AS outputoctets " +
    " FROM radacct racct " +
    "WHERE " +
    " racct.nasipaddress = :lo0 AND " +
    " racct.acctsessiontime > 0 AND " +
    " racct.acctstarttime BETWEEN DATE_FORMAT(:start_date, '%Y-%m-%d 00:00:00') AND DATE_FORMAT(:end_date, '%Y-%m-%d 23:59:59') AND " +
    " racct.acctterminatecause <> '' "
  )
  List<Map<String, Object>> getSessionTimeByStoreId(
    @Param("start_date") Date startDate,
    @Param("end_date") Date endDate,
    @Param("lo0") String lo0
  );
}
