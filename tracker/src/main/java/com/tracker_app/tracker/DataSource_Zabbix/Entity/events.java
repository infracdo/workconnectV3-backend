package com.tracker_app.tracker.DataSource_Zabbix.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "events")
public class events {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "eventid")
    private Long eventid;
    @Column(name = "source")
    private Integer source;
    @Column(name = "object")
    private Integer object;
    @Column(name = "objectid")
    private Long objectid;
    @Column(name = "clock")
    private Integer clock;
    @Column(name = "value")
    private Integer value;
    @Column(name = "acknowledged")
    private Integer acknowledged;
    @Column(name = "ns")
    private Integer ns;

    
}
