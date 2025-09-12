package com.tracker_app.tracker.DataSource_NMS.Repo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tracker_app.tracker.DataSource_NMS.Entity.store_info_changes;

@Repository
public interface store_info_changes_repo extends JpaRepository<store_info_changes, Long> {

    // Query to fetch circuit change logs by type of change for the current week
    @Query("SELECT s FROM store_info_changes s " +
        "WHERE LOWER(s.typeOfChange) LIKE %:keyword% " +
        "AND s.timestamp BETWEEN :startOfWeek AND :endOfWeek")
    List<store_info_changes> findCircuitLogsByTypeOfChange(
        @Param("keyword") String keyword,
        @Param("startOfWeek") LocalDateTime startOfWeek,
        @Param("endOfWeek") LocalDateTime endOfWeek
    );

    // Query to fetch circuit changes logs with UP status (either rediscovered mac or disconvered mac) for the current week
    @Query("SELECT s FROM store_info_changes s " +
        "WHERE LOWER(s.typeOfChange) LIKE '%discovered mac address%' AND " +
        "LOWER(s.typeOfChange) NOT LIKE '%no discovered mac address%' AND " +
        "s.timestamp BETWEEN :startOfWeek AND :endOfWeek")
    List<store_info_changes> findCircuitLogsChangesUp(
        @Param("startOfWeek") LocalDateTime startOfWeek,
        @Param("endOfWeek") LocalDateTime endOfWeek
    );

    // Query to fetch circuit changes logs by date range
    @Query("SELECT s FROM store_info_changes s WHERE date(s.timestamp) BETWEEN :startDate AND :endDate")
    List<store_info_changes> findByDateRange(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    // Query to fetch UP circuit change logs (discovered mac address/rediscovered mac address) by date range
    @Query("SELECT s FROM store_info_changes s " +
        "WHERE date(s.timestamp) BETWEEN :startDate AND :endDate " +
        "AND LOWER(s.typeOfChange) LIKE '%discovered mac address%' " +
        "AND LOWER(s.typeOfChange) NOT LIKE '%no discovered mac address%'"
    )
    List<store_info_changes> findCircuitLogsChangesUpByDateRange(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    // Query to fetch circuit change logs by keywords and date range
    @Query("SELECT s FROM store_info_changes s " +
        "WHERE date(s.timestamp) BETWEEN :startDate AND :endDate " +
        "AND (LOWER(s.typeOfChange) LIKE %:keyword%)")
    List<store_info_changes> findCircuitLogsByKeywordsAndDateRange(
        @Param("keyword") String keyword,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

}
