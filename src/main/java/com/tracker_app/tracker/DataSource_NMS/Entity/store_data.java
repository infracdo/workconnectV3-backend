package com.tracker_app.tracker.DataSource_NMS.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity // labels an entity that can be used for JPA
@Table(name = "store_data")
public class store_data {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "site_id")
  private String siteId;

  @Column(name = "status")
  private String status;

  @Column(name = "single_circuit")
  private String single_circuit;

  @Column(name = "wireless_only")
  private String wireless_only;

  @Column(name = "primary_circuit_status")
  private String primary_circuit_status;

  @Column(name = "primary_provider")
  private String primary_provider;

  @Column(name = "backup_circuit_status")
  private String backup_circuit_status;

  @Column(name = "backup_provider")
  private String backup_provider;

  @Column(name = "router")
  private String router;

  @Column(name = "loopback_0")
  private Double loopback_0;

  @Column(name = "loopback_1")
  private Double loopback_1;

  @Column(name = "loopback_2")
  private Double loopback_2;

  @Column(name = "loopback_3")
  private Double loopback_3;

  @Column(name = "loopback_0_24h")
  private Double loopback_0_24h;

  @Column(name = "loopback_1_24h")
  private Double loopback_1_24h;

  @Column(name = "loopback_2_24h")
  private Double loopback_2_24h;

  @Column(name = "loopback_3_24h")
  private Double loopback_3_24h;

  public String getSiteId() {
    return siteId;
  }

  public void setSiteId(String siteId) {
    this.siteId = siteId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getSingle_circuit() {
    return single_circuit;
  }

  public void setSingle_circuit(String single_circuit) {
    this.single_circuit = single_circuit;
  }

  public String getWireless_only() {
    return wireless_only;
  }

  public void setWireless_only(String wireless_only) {
    this.wireless_only = wireless_only;
  }

  public String getPrimary_circuit_status() {
    return primary_circuit_status;
  }

  public void setPrimary_circuit_status(String primary_circuit_status) {
    this.primary_circuit_status = primary_circuit_status;
  }

  public String getPrimary_provider() {
    return primary_provider;
  }

  public void setPrimary_provider(String primary_provider) {
    this.primary_provider = primary_provider;
  }

  public String getBackup_circuit_status() {
    return backup_circuit_status;
  }

  public void setBackup_circuit_status(String backup_circuit_status) {
    this.backup_circuit_status = backup_circuit_status;
  }

  public String getBackup_provider() {
    return backup_provider;
  }

  public void setBackup_provider(String backup_provider) {
    this.backup_provider = backup_provider;
  }

  public String getRouter() {
    return router;
  }

  public void setRouter(String router) {
    this.router = router;
  }

  public double getLoopback_0() {
    return loopback_0;
  }

  public void setLoopback_0(double loopback_0) {
    this.loopback_0 = loopback_0;
  }

  public double getLoopback_1() {
    return loopback_1;
  }

  public void setLoopback_1(double loopback_1) {
    this.loopback_1 = loopback_1;
  }

  public double getLoopback_2() {
    return loopback_2;
  }

  public void setLoopback_2(double loopback_2) {
    this.loopback_2 = loopback_2;
  }

  public double getLoopback_3() {
    return loopback_3;
  }

  public void setLoopback_3(double loopback_3) {
    this.loopback_3 = loopback_3;
  }

  public double getLoopback_0_24h() {
    return loopback_0_24h;
  }

  public void setLoopback_0_24h(double loopback_0_24h) {
    this.loopback_0_24h = loopback_0_24h;
  }

  public double getLoopback_1_24h() {
    return loopback_1_24h;
  }

  public void setLoopback_1_24h(double loopback_1_24h) {
    this.loopback_1_24h = loopback_1_24h;
  }

  public double getLoopback_2_24h() {
    return loopback_2_24h;
  }

  public void setLoopback_2_24h(double loopback_2_24h) {
    this.loopback_2_24h = loopback_2_24h;
  }

  public double getLoopback_3_24h() {
    return loopback_3_24h;
  }

  public void setLoopback_3_24h(double loopback_3_24h) {
    this.loopback_3_24h = loopback_3_24h;
  }
}
