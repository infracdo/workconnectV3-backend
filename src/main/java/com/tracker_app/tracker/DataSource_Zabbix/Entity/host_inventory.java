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
@Table(name = "host_inventory")
public class host_inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "hostid")
    private Long hostid;
    @Column(name = "poc_2_phone_a")
    private String poc_2_phone_a;
    @Column(name = "vendor")
    private String vendor;
    @Column(name = "chassis")
    private String chassis;
    @Column(name = "model")
    private String model;

}
