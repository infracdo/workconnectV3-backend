package com.tracker_app.tracker.DataSource_WorkConnect.Entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class sites_summary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String site_id;
    private String name;
    private String island_group; 
    private String address;
    private Timestamp date_created;
    private Timestamp date_last_updated;
    private String contact_person;
    private String contact_nos;
    private String tenant_name;
    private String region;
    private String status;
    private String network_status;

}
