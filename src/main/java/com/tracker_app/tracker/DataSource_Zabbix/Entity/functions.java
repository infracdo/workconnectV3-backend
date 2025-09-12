package com.tracker_app.tracker.DataSource_Zabbix.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "functions")
public class functions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "functionid")
    private Long functionid;
    @Column(name = "itemid")
    private Long itemid;
    @Column(name = "triggerid")
    private Long triggerid;
    @Column(name = "function")
    private String function;
    @Column(name = "parameter")
    private String parameter;

    /*
    @OneToOne
    private triggers triggers;
    */
}
