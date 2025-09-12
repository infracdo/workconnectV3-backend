package com.tracker_app.tracker.DataSource_NMS.Repo;

import com.tracker_app.tracker.DataSource_NMS.Entity.store_count;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface store_count_repo extends JpaRepository<store_count, Long> {
  Optional<store_count> findByDate(Timestamp date);

  List<store_count> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
