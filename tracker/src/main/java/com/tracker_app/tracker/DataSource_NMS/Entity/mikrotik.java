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
@Table(name = "mac_mikrotik")
public class mikrotik {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(name = "site_id")
  private String siteId;

  private String port1;

  private String port2;

  @Column(name = "latest_date")
  private String latestDate;

  private String router;

  @Column(name = "public_ips")
  private String publicIps;

  @Column(name = "public_ips2")
  private String publicIps2;
}
