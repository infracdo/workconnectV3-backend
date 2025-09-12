package com.tracker_app.tracker.DataSource_NMS.Repo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tracker_app.tracker.DataSource_NMS.Entity.store_info_circuits;

@Repository
public interface store_info_circuits_repo extends JpaRepository<store_info_circuits, String> {

        // Query to fetch circuit info that has modem connected (has mac address) but no
        // telco/provider for the current week
        @Query("SELECT s FROM store_info_circuits s " +
                        "WHERE s.macAddress1 NOT IN ('', 'NONE', 'NO MAC') " +
                        "AND s.macAddress2 NOT IN ('', 'NONE', 'NO MAC') " +
                        "AND (s.providerVpn1 IN ('', 'NONE') OR s.providerVpn1 IS NULL " +
                        "OR s.providerVpn2 IN ('', 'NONE') OR s.providerVpn2 IS NULL)" +
                        "AND s.timestamp BETWEEN :startOfWeek AND :endOfWeek")
        List<store_info_circuits> findWithModemButNoTelco(
                        @Param("startOfWeek") LocalDateTime startOfWeek,
                        @Param("endOfWeek") LocalDateTime endOfWeek);

        // Query to fetch circuit info by date range
        @Query("SELECT s FROM store_info_circuits s WHERE date(s.timestamp) BETWEEN :startDate AND :endDate")
        List<store_info_circuits> findByDateRange(
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query(value = "SELECT * FROM store_info_circuits", nativeQuery = true)
        List<Map<String, Object>> getStoreInfoCircuits();

        @Query(value = "SELECT * FROM store_info_circuits WHERE site_id IN :siteIds", nativeQuery = true)
        List<Map<String, Object>> getStoreInfoCircuitsBySiteIds(@Param("siteIds") List<String> siteIds);

        Optional<store_info_circuits> findBySiteId(String siteId);

}
