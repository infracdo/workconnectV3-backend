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
@Table(name = "event_recovery")
public class event_recovery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "eventid")
    private Long eventid;
    @Column(name = "r_eventid")
    private Long r_eventid;
    @Column(name = "c_eventid")
    private Long c_eventid ;
    @Column(name = "correlationid")
    private Long correlationid;
    @Column(name = "userid")
    private Long userid;
}
