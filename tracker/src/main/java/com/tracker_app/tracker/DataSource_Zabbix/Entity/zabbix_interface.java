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
@Table(name = "interface")
public class zabbix_interface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "interfaceid")
    private Long interfaceid;
    @Column(name = "hostid")
    private Long hostid;
    @Column(name = "main")
    private Integer main;
    @Column(name = "type")
    private Integer type;
    @Column(name = "useip")
    private Integer useip;
    @Column(name = "ip")
    private String ip;
    @Column(name = "dns")
    private String dns;
    @Column(name = "port")
    private String port;
    @Column(name = "bulk")
    private Integer bulk;
}
