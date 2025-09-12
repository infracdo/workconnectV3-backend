package com.tracker_app.tracker.DataSource_Zabbix.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "hosts")
public class hosts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "hostid")
    private Long hostid;
    @Column(name = "host")
    private String host;
    @Column(name = "name")
    private String name;

    
    
    /*
    @ManyToOne
    private hosts_groups hostgroup;
    
    @OneToMany
    private items items;

    @OneToOne
    private host_inventory host_inventory;
    */

}
