package com.tracker_app.tracker.DataSource_Zabbix.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "items")
public class items {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "itemid")
    private Long itemid;
    @Column(name = "hostid")
    private Long hostid;
    @Column(name = "name")
    private String name;
    @Column(name = "history")
    private String history;

    /*
    @OneToMany
    private functions functions;
    */
}
