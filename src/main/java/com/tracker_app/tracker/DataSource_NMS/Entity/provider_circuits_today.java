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
@Table(name = "provider_circuits_today")
public class provider_circuits_today {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private long id;

  @Column(name = "site_id")
  private String siteId;

  @Column(name = "mac_primary")
  private String macPrimary;

  @Column(name = "mac_backup")
  private String macBackup;

  @Column(name = "mac_third")
  private String macThird;

  @Column(name = "public_ip_primary")
  private String publicIpPrimary;

  @Column(name = "public_ip_backup")
  private String publicIpBackup;

  @Column(name = "public_ip_third")
  private String publicIpThird;

  @Column(name = "provider_primary")
  private String providerPrimary;

  @Column(name = "provider_backup")
  private String providerBackup;

  @Column(name = "provider_third")
  private String providerThird;
}
