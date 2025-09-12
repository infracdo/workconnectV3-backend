package com.tracker_app.tracker.DataSource_NMS.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tracker_app.tracker.DataSource_NMS.Entity.vpn_connection_today;
import com.tracker_app.tracker.DataSource_NMS.Repo.vpn_connection_today_repo;

@Service
public class vpn_connection_today_service {

    @Autowired
    private vpn_connection_today_repo vpn_connection_today_repo;

    // Get all VPN Connection Today
    public List<vpn_connection_today> getAllVpnConnectionToday() {
        return vpn_connection_today_repo.findAll();
    }
}
