package com.tracker_app.tracker.DataSource_WorkConnect.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class site_location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String site_id;
    private String name;
    private Float latitude; 
    private Float longitude;
    private String tenant;
    private int no_links_up;
    //private int primary_satus;
    //private int backup_satus;

}
