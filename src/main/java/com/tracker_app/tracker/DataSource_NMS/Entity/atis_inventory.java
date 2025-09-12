package com.tracker_app.tracker.DataSource_NMS.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "atis_inventory")
public class atis_inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "site_id")
    private String site_id;

    @Column(name = "provider")
    private String provider;

    @Column(name = "name")
    private String name;

    @Column(name = "atis")
    private Boolean atis;

    @Column(name = "autodiscover")
    private Boolean autodiscover;

    @Column(name = "last_update")
    private String last_update;

    @Column(name = "status")
    private String status;
}
