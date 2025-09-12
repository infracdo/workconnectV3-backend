package com.tracker_app.tracker.DataSource_WorkConnect.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class tenant_lookup {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String slug;
    private String name;
    private String prom_name;
    private Integer[] zabbix_group_id;

}
