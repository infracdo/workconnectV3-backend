package com.tracker_app.tracker.DataSource_NMS.Entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "store_count_down")
public class store_count_down {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Timestamp date;

  @Column(name = "prom_down")
  private int promDown;

  @Column(name = "zabbix_down")
  private int zabbixDown;
}
