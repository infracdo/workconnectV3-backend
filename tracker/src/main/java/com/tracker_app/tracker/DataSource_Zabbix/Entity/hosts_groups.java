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
@Table(name = "hosts_groups")
public class hosts_groups {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "hostgroupid")
    private Long hostgroupid;
    @Column(name = "hostid")
    private Long hostid;
    @Column(name = "groupid")
    private Long groupid ;

}
