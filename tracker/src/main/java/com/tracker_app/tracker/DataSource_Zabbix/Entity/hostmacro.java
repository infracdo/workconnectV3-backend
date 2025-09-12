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
@Table(name = "hostmacro")
public class hostmacro {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "hostmacroid")
    private Long hostmacroid;
    @Column(name = "hostid")
    private Long hostid;
    @Column(name = "macro")
    private String macro;
    @Column(name = "value")
    private String value;
}
