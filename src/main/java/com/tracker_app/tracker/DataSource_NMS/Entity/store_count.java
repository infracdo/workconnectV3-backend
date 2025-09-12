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
@Table(name = "store_counts")
public class store_count {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Timestamp date;

  @Column(name = "down_store")
  private int downStore;

  @Column(name = "down_mikrotik")
  private int downMikrotik;

  @Column(name = "down_ruijie")
  private int downRuijie;

  @Column(name = "problematic0_7d")
  private int problematicStoreCircuitZeroUptime7D;

  @Column(name = "problematic95_7d")
  private int problematicStoreCircuit95Uptime7D;

  @Column(name = "problematic95_24h")
  private int problematicStoreCircuit95Uptime24H;

  @Column(name = "wireless_only")
  private int wirelessOnly;

  @Column(name = "single_circuit")
  private int singleCircuit;

  @Column(name = "primary0_7d")
  private int primaryCircuitZeroUptime7D;

  @Column(name = "primary95_7d")
  private int primaryCircuit95Uptime7D;

  @Column(name = "primary95_24h")
  private int primaryCircuit95Uptime24H;

  @Column(name = "primary100_7d")
  private int primaryCircuit100Uptime7D;

  @Column(name = "primary_no_internet")
  private int primaryNoInternet;

  @Column(name = "primary_no_provider")
  private int primaryNoProvider;

  @Column(name = "primary_wireless")
  private int primaryWireless;

  @Column(name = "backup0_7d")
  private int backupCircuitZeroUptime7D;

  @Column(name = "backup95_7d")
  private int backupCircuit95Uptime7D;

  @Column(name = "backup100_7d")
  private int backupCircuit100Uptime7D;

  @Column(name = "backup95_24h")
  private int backupCircuit95Uptime24H;

  @Column(name = "backup_no_internet")
  private int backupNoInternet;

  @Column(name = "backup_no_provider")
  private int backupNoProvider;

  @Column(name = "backup_wired")
  private int backupWired;

  @Column(name = "backup_only_7d")
  private int backupOnly7D;

  @Column(name = "primary_only_7d")
  private int primaruOnly7D;

  @Column(name = "zabbix_down_stores")
  private int zabbixDownStores;

  @Column(name = "dual_circuit_7d")
  private int dualCircuit7D;

  @Column(name = "store100_7d")
  private int store100uptime7D;

  @Column(name = "store_healthy_7d")
  private int storeHealthy7d;
}
