package com.tracker_app.tracker.DataSource_NMS.Entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "store_counts_today")
public class store_count_today {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDate date;

  @Column(name = "down_store")
  private int downStore;

  @Column(name = "zabbix_down_stores")
  private int zabbixDownStore;

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

  public int getZabbixDownStore() {
    return zabbixDownStore;
  }

  public void setZabbixDownStore(int zabbixDownStore) {
    this.zabbixDownStore = zabbixDownStore;
  }

  public int getPrimaryCircuit100Uptime7D() {
    return primaryCircuit100Uptime7D;
  }

  public void setPrimaryCircuit100Uptime7D(int primaryCircuit100Uptime7D) {
    this.primaryCircuit100Uptime7D = primaryCircuit100Uptime7D;
  }

  public int getBackupCircuit100Uptime7D() {
    return backupCircuit100Uptime7D;
  }

  public void setBackupCircuit100Uptime7D(int backupCircuit100Uptime7D) {
    this.backupCircuit100Uptime7D = backupCircuit100Uptime7D;
  }

  @Column(name = "dual_circuit_7d")
  private int dualCircuit7D;

  public int getBackupOnly7D() {
    return backupOnly7D;
  }

  public void setBackupOnly7D(int backupOnly7D) {
    this.backupOnly7D = backupOnly7D;
  }

  public int getPrimaruOnly7D() {
    return primaruOnly7D;
  }

  public void setPrimaruOnly7D(int primaruOnly7D) {
    this.primaruOnly7D = primaruOnly7D;
  }

  public int getDualCircuit7D() {
    return dualCircuit7D;
  }

  public void setDualCircuit7D(int dualCircuit7D) {
    this.dualCircuit7D = dualCircuit7D;
  }

  public int getStore100uptime7D() {
    return store100uptime7D;
  }

  public void setStore100uptime7D(int store100uptime7d) {
    store100uptime7D = store100uptime7d;
  }

  public int getStoreHealthy7d() {
    return storeHealthy7d;
  }

  public void setStoreHealthy7d(int storeHealthy7d) {
    this.storeHealthy7d = storeHealthy7d;
  }

  @Column(name = "store100_7d")
  private int store100uptime7D;

  @Column(name = "store_healthy_7d")
  private int storeHealthy7d;

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public int getDownStore() {
    return downStore;
  }

  public void setDownStore(int downStore) {
    this.downStore = downStore;
  }

  public int getDownMikrotik() {
    return downMikrotik;
  }

  public void setDownMikrotik(int downMikrotik) {
    this.downMikrotik = downMikrotik;
  }

  public int getDownRuijie() {
    return downRuijie;
  }

  public void setDownRuijie(int downRuijie) {
    this.downRuijie = downRuijie;
  }

  public int getProblematicStoreCircuitZeroUptime7D() {
    return problematicStoreCircuitZeroUptime7D;
  }

  public void setProblematicStoreCircuitZeroUptime7D(
      int problematicStoreCircuitZeroUptime7D) {
    this.problematicStoreCircuitZeroUptime7D = problematicStoreCircuitZeroUptime7D;
  }

  public int getProblematicStoreCircuit95Uptime7D() {
    return problematicStoreCircuit95Uptime7D;
  }

  public void setProblematicStoreCircuit95Uptime7D(
      int problematicStoreCircuit95Uptime7D) {
    this.problematicStoreCircuit95Uptime7D = problematicStoreCircuit95Uptime7D;
  }

  public int getProblematicStoreCircuit95Uptime24H() {
    return problematicStoreCircuit95Uptime24H;
  }

  public void setProblematicStoreCircuit95Uptime24H(
      int problematicStoreCircuit95Uptime24H) {
    this.problematicStoreCircuit95Uptime24H = problematicStoreCircuit95Uptime24H;
  }

  public int getWirelessOnly() {
    return wirelessOnly;
  }

  public void setWirelessOnly(int wirelessOnly) {
    this.wirelessOnly = wirelessOnly;
  }

  public int getSingleCircuit() {
    return singleCircuit;
  }

  public void setSingleCircuit(int singleCircuit) {
    this.singleCircuit = singleCircuit;
  }

  public int getPrimaryCircuitZeroUptime7D() {
    return primaryCircuitZeroUptime7D;
  }

  public void setPrimaryCircuitZeroUptime7D(int primaryCircuitZeroUptime7D) {
    this.primaryCircuitZeroUptime7D = primaryCircuitZeroUptime7D;
  }

  public int getPrimaryCircuit95Uptime7D() {
    return primaryCircuit95Uptime7D;
  }

  public void setPrimaryCircuit95Uptime7D(int primaryCircuit95Uptime7D) {
    this.primaryCircuit95Uptime7D = primaryCircuit95Uptime7D;
  }

  public int getPrimaryCircuit95Uptime24H() {
    return primaryCircuit95Uptime24H;
  }

  public void setPrimaryCircuit95Uptime24H(int primaryCircuit95Uptime24H) {
    this.primaryCircuit95Uptime24H = primaryCircuit95Uptime24H;
  }

  public int getPrimaryNoInternet() {
    return primaryNoInternet;
  }

  public void setPrimaryNoInternet(int primaryNoInternet) {
    this.primaryNoInternet = primaryNoInternet;
  }

  public int getPrimaryNoProvider() {
    return primaryNoProvider;
  }

  public void setPrimaryNoProvider(int primaryNoProvider) {
    this.primaryNoProvider = primaryNoProvider;
  }

  public int getPrimaryWireless() {
    return primaryWireless;
  }

  public void setPrimaryWireless(int primaryWireless) {
    this.primaryWireless = primaryWireless;
  }

  public int getBackupCircuitZeroUptime7D() {
    return backupCircuitZeroUptime7D;
  }

  public void setBackupCircuitZeroUptime7D(int backupCircuitZeroUptime7D) {
    this.backupCircuitZeroUptime7D = backupCircuitZeroUptime7D;
  }

  public int getBackupCircuit95Uptime7D() {
    return backupCircuit95Uptime7D;
  }

  public void setBackupCircuit95Uptime7D(int backupCircuit95Uptime7D) {
    this.backupCircuit95Uptime7D = backupCircuit95Uptime7D;
  }

  public int getBackupCircuit95Uptime24H() {
    return backupCircuit95Uptime24H;
  }

  public void setBackupCircuit95Uptime24H(int backupCircuit95Uptime24H) {
    this.backupCircuit95Uptime24H = backupCircuit95Uptime24H;
  }

  public int getBackupNoInternet() {
    return backupNoInternet;
  }

  public void setBackupNoInternet(int backupNoInternet) {
    this.backupNoInternet = backupNoInternet;
  }

  public int getBackupNoProvider() {
    return backupNoProvider;
  }

  public void setBackupNoProvider(int backupNoProvider) {
    this.backupNoProvider = backupNoProvider;
  }

  public int getBackupWired() {
    return backupWired;
  }

  public void setBackupWired(int backupWired) {
    this.backupWired = backupWired;
  }
}
