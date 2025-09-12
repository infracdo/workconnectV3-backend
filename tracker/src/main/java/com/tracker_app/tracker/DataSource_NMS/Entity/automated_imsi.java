package com.tracker_app.tracker.DataSource_NMS.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "automated_imsi")
public class automated_imsi {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private int id;

  @Column(name = "site_id")
  private String siteId;

  private String port;

  private String imsi;

  @Column(name = "serial_num")
  private String serialNum;

  private String mac;
  private String username;
  private String passw;

  @Column(name = "layout_type")
  private Integer layoutType;

  private String comment;
}
