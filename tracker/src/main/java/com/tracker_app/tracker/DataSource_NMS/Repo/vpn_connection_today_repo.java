package com.tracker_app.tracker.DataSource_NMS.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tracker_app.tracker.DataSource_NMS.Entity.vpn_connection_today;

@Repository
public interface vpn_connection_today_repo extends JpaRepository<vpn_connection_today, Long> {

}
