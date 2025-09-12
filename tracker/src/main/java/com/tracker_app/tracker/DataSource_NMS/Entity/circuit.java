package com.tracker_app.tracker.DataSource_NMS.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

@Data
@Entity
@Table(name = "circuit")
public class circuit {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private long id;

  @Column(name = "latest_date")
  private String latestDate;

  @Column(name = "site_id")
  private String siteId;

  @Column(name = "router")
  private String router;

  @Column(name = "port_1")
  private String port1;

  @Column(name = "port_1_status")
  private String port1Status;

  @Column(name = "port_2")
  private String port2;

  @Column(name = "cellular_modem")
  private String cellularModem;

  @Column(name = "cellular_status")
  private String cellularStatus;

  @Column(name = "fa11")
  private String fall;

  @Column(name = "fa11_status")
  private String fallStatus;

  @Column(name = "fa10")
  private String fa10;

  @Column(name = "fa10_status")
  private String fa10Staus;
}
