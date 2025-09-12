package com.tracker_app.tracker.DataSource_NMS.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tracker_app.tracker.DataSource_NMS.Entity.store_info_changes;
import com.tracker_app.tracker.DataSource_NMS.Entity.store_info_circuits;
import com.tracker_app.tracker.DataSource_NMS.Repo.store_info_changes_repo;
import com.tracker_app.tracker.DataSource_NMS.Repo.store_info_circuits_repo;

@Service
public class logs_service {

    private static final Logger logger = LoggerFactory.getLogger(logs_service.class);

    private store_info_changes_repo store_info_changes_repo;
    private store_info_circuits_repo store_info_circuits_repo;

    public logs_service(
        store_info_changes_repo store_info_changes_repo,
        store_info_circuits_repo store_info_circuits_repo
    ) {
        this.store_info_changes_repo = store_info_changes_repo;
        this.store_info_circuits_repo = store_info_circuits_repo;
    }


    // Return all circuit change logs
    public List<store_info_changes> getAllCircuitLogs() {
        try {
            return store_info_changes_repo.findAll();
        } catch (Exception e) {
            logger.error("Error occurred while fetching all circuit change logs: ", e);
            throw new RuntimeException("Failed to retrieve all circuit change logs", e);
        }
    }

    // Return circuit change logs by type of change (for either DOWN, NEW or CHANGED) for the current week
    public List<store_info_changes> getCircuitLogsByTypeOfChange(List<String> keywords) {
        try {
            LocalDate today = LocalDate.now();
            LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
            LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

            // Convert LocalDate to LocalDateTime
            LocalDateTime startOfWeekTime = startOfWeek.atStartOfDay();
            LocalDateTime endOfWeekTime = endOfWeek.atTime(23, 59, 59);

            List<store_info_changes> allChanges = store_info_changes_repo.findAll();

            // return store_info_changes_repo.findCircuitLogsByTypeOfChange(keyword);
            return allChanges.stream()
                .filter(change -> {
                    String type = change.getTypeOfChange().toLowerCase();
                    boolean matchesKeyword = keywords.stream()
                        .anyMatch(keyword -> type.contains(keyword.toLowerCase()));
                    boolean withinCurrentWeek = !change.getTimestamp().isBefore(startOfWeekTime)
                        && !change.getTimestamp().isAfter(endOfWeekTime);
                    
                    return matchesKeyword && withinCurrentWeek;
                })
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error occurred while fetching circuit change logs by type of change for the current week: ", e);
            throw new RuntimeException("Failed to retrieve circuit change logs by type of change for the current week", e);
        }
    }

    // Return UP circuit change logs (either rediscovered mac or disconvered mac) for the current week
    public List<store_info_changes> getCircuitLogsChangesUp() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
            LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

            // Convert LocalDate to LocalDateTime
            LocalDateTime startOfWeekTime = startOfWeek.atStartOfDay();
            LocalDateTime endOfWeekTime = endOfWeek.atTime(23, 59, 59);

            return store_info_changes_repo.findCircuitLogsChangesUp(startOfWeekTime, endOfWeekTime);
        } catch (Exception e) {
            logger.error("Error occurred while fetching UP circuit change logs for the current week: ", e);
            throw new RuntimeException("Failed to retrieve UP circuit change logs for the current week", e);
        }
    }

    // Return circuit info that has modem connected (has mac address) but no telco/provider for the current week
    public List<store_info_circuits> getWithModemButNoTelco() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
            LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

            // Conver LocalDate to LocalDateTime
            LocalDateTime startOfWeekTime = startOfWeek.atStartOfDay();
            LocalDateTime endOfWeekTime = endOfWeek.atTime(23, 59, 59);

            return store_info_circuits_repo.findWithModemButNoTelco(startOfWeekTime, endOfWeekTime);
        } catch (Exception e) {
            logger.error("Error occurred while fetching circuit info that has modem connected but no telco/provider for the current week: ", e);
            throw new RuntimeException("Failed to retrieve circuit info that has modem connected but no telco/provider for the current week", e);
        }
    }

    // Return circuit change logs by date range
    public List<store_info_changes> getCircuitLogsByDateRange(String circuitLogStatus, List<String> keywords, LocalDate startDate, LocalDate endDate) {
        try {
            if ("up".equalsIgnoreCase(circuitLogStatus)) {
                return store_info_changes_repo.findCircuitLogsChangesUpByDateRange(startDate, endDate);
            }
            
            if (keywords == null || keywords.isEmpty()) {
                throw new IllegalArgumentException("Keywords must be provided for non-up status queries");
            }
    
            List<store_info_changes> result = new ArrayList<>();
            for (String keyword : keywords) {
                result.addAll(store_info_changes_repo.findCircuitLogsByKeywordsAndDateRange(
                    keyword.toLowerCase(),
                    startDate,
                    endDate
                ));
            }
            return result.stream().distinct().collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error occurred while fetching circuit change logs by date range: ", e);
            throw new RuntimeException("Failed to retrieve circuit change logs by date range", e);
        }
    }

    // Return circuit info by date range
    public List<store_info_circuits> getCircuitInfoByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            return store_info_circuits_repo.findByDateRange(startDate, endDate);
        } catch (Exception e) {
            logger.error("Error occurred while fetching circuit info by date range", e);
            throw new RuntimeException("Failed to retrieve circuit info by date range", e);
        }
    }

}
