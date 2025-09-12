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
@Table(name = "triggers")
public class triggers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "triggerid")
    private Long triggerid;
    @Column(name = "comments")
    private String comments;
    @Column(name = "priority")
    private Integer priority;
    @Column(name = "value")
    private Integer value;
    @Column(name = "lastchange")
    private Integer lastchange;
}
