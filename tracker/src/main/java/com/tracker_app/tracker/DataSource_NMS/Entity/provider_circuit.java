package com.tracker_app.tracker.DataSource_NMS.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "provider_circuits")
public class provider_circuit {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private long id;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Column(name = "site_id")
  private String siteId;

  @Column(name = "date_timestamp")
  private String dateTimestamp;

  @Column(name = "mac_port1")
  private String macPort1;

  @Column(name = "mac_port2")
  private String macPort2;

  @Column(name = "modem_gtw_port1")
  private String modemGtwPort1;

  @Column(name = "modem_gtw_port2")
  private String modemGtwPort2;

  @Column(name = "public_ip_port1")
  private String publicIpPort1;

  @Column(name = "public_ip_port2")
  private String publicIpPort2;

  @Column(name = "status_port1")
  private String statusPort1;

  @Column(name = "status_port2")
  private String statusPort2;

  @Column(name = "cir_prov_port1")
  private String cirProvPort1;

  @Column(name = "cir_prov_port2")
  private String cirProvPort2;

  @Column(name = "router")
  private String router;

  @Column(name = "min_rtt_port1")
  private String minRttPort1;

  @Column(name = "avg_rtt_port1")
  private String avgRttPort1;

  @Column(name = "max_rtt_port1")
  private String maxRttPort1;

  @Column(name = "packet_loss_port1")
  private String packetLossPort1;

  @Column(name = "packet_loss_port2")
  private String packetLossPort2;

  @Column(name = "min_rtt_port2")
  private String minRttPort2;

  @Column(name = "avg_rtt_port2")
  private String avgRttPort2;

  @Column(name = "max_rtt_port2")
  private String maxRttPort2;

  @Column(name = "mac_vlan10")
  private String macVlan10;

  @Column(name = "mac_vlan20")
  private String macVlan20;

  @Column(name = "mac_cellular")
  private String macCellular;

  @Column(name = "modem_gtw_vlan10")
  private String modemGtwVlan10;

  @Column(name = "modem_gtw_vlan20")
  private String modemGtwVlan20;

  @Column(name = "modem_gtw_cellular")
  private String modemGtwCellular;

  public String getModemGtwVlan10() {
    return modemGtwVlan10;
  }

  public void setModemGtwVlan10(String modemGtwVlan10) {
    this.modemGtwVlan10 = modemGtwVlan10;
  }

  public String getModemGtwVlan20() {
    return modemGtwVlan20;
  }

  public void setModemGtwVlan20(String modemGtwVlan20) {
    this.modemGtwVlan20 = modemGtwVlan20;
  }

  public String getModemGtwCellular() {
    return modemGtwCellular;
  }

  public void setModemGtwCellular(String modemGtwCellular) {
    this.modemGtwCellular = modemGtwCellular;
  }

  @Column(name = "public_ip_vlan10")
  private String publicVlanIpVlan10;

  @Column(name = "public_ip_vlan20")
  private String publicVlanIpVlan20;

  @Column(name = "public_ip_cellular")
  private String publicIpCellular;

  @Column(name = "status_vlan10")
  private String statusVlan10;

  @Column(name = "status_vlan20")
  private String statusVlan20;

  @Column(name = "status_cellular")
  private String statusCellular;

  @Column(name = "cir_prov_vlan10")
  private String cirProvVlan10;

  @Column(name = "cir_prov_vlan20")
  private String cirProvVlan20;

  @Column(name = "cir_prov_cellular")
  private String cirProvCellular;

  @Column(name = "min_rtt_vlan10")
  private String minRttVlan10;

  @Column(name = "avg_rtt_vlan10")
  private String avgRttVlan10;

  @Column(name = "mac_rtt_vlan10")
  private String maxRttVlan10;

  @Column(name = "packet_loss_vlan10")
  private String packetLossVlan10;

  @Column(name = "min_rtt_vlan20")
  private String minRttVlan20;

  @Column(name = "avg_rtt_vlan20")
  private String avgRttVlan20;

  @Column(name = "max_rtt_vlan20")
  private String maxRttVlan20;

  @Column(name = "packet_loss_vlan20")
  private String packetLossVlan20;

  @Column(name = "min_rtt_cellular")
  private String minRttCellular;

  @Column(name = "avg_rtt_cellular")
  private String avgRttCellular;

  @Column(name = "max_rtt_cellular")
  private String maxRttCellular;

  @Column(name = "packet_loss_cellular")
  private String packetLossCellular;

  public String getSiteId() {
    return siteId;
  }

  public void setSiteId(String siteId) {
    this.siteId = siteId;
  }

  public String getDateTimestamp() {
    return dateTimestamp;
  }

  public void setDateTimestamp(String dateTimestamp) {
    this.dateTimestamp = dateTimestamp;
  }

  public String getMacPort1() {
    return macPort1;
  }

  public void setMacPort1(String macPort1) {
    this.macPort1 = macPort1;
  }

  public String getMacPort2() {
    return macPort2;
  }

  public void setMacPort2(String macPort2) {
    this.macPort2 = macPort2;
  }

  public String getModemGtwPort1() {
    return modemGtwPort1;
  }

  public void setModemGtwPort1(String modemGtwPort1) {
    this.modemGtwPort1 = modemGtwPort1;
  }

  public String getModemGtwPort2() {
    return modemGtwPort2;
  }

  public void setModemGtwPort2(String modemGtwPort2) {
    this.modemGtwPort2 = modemGtwPort2;
  }

  public String getPublicIpPort1() {
    return publicIpPort1;
  }

  public void setPublicIpPort1(String publicIpPort1) {
    this.publicIpPort1 = publicIpPort1;
  }

  public String getPublicIpPort2() {
    return publicIpPort2;
  }

  public void setPublicIpPort2(String publicIpPort2) {
    this.publicIpPort2 = publicIpPort2;
  }

  public String getStatusPort1() {
    return statusPort1;
  }

  public void setStatusPort1(String statusPort1) {
    this.statusPort1 = statusPort1;
  }

  public String getStatusPort2() {
    return statusPort2;
  }

  public void setStatusPort2(String statusPort2) {
    this.statusPort2 = statusPort2;
  }

  public String getCirProvPort1() {
    return cirProvPort1;
  }

  public void setCirProvPort1(String cirProvPort1) {
    this.cirProvPort1 = cirProvPort1;
  }

  public String getCirProvPort2() {
    return cirProvPort2;
  }

  public void setCirProvPort2(String cirProvPort2) {
    this.cirProvPort2 = cirProvPort2;
  }

  public String getRouter() {
    return router;
  }

  public void setRouter(String router) {
    this.router = router;
  }

  public String getMinRttPort1() {
    return minRttPort1;
  }

  public void setMinRttPort1(String minRttPort1) {
    this.minRttPort1 = minRttPort1;
  }

  public String getAvgRttPort1() {
    return avgRttPort1;
  }

  public void setAvgRttPort1(String avgRttPort1) {
    this.avgRttPort1 = avgRttPort1;
  }

  public String getMaxRttPort1() {
    return maxRttPort1;
  }

  public void setMaxRttPort1(String maxRttPort1) {
    this.maxRttPort1 = maxRttPort1;
  }

  public String getPacketLossPort1() {
    return packetLossPort1;
  }

  public void setPacketLossPort1(String packetLossPort1) {
    this.packetLossPort1 = packetLossPort1;
  }

  public String getPacketLossPort2() {
    return packetLossPort2;
  }

  public void setPacketLossPort2(String packetLossPort2) {
    this.packetLossPort2 = packetLossPort2;
  }

  public String getMinRttPort2() {
    return minRttPort2;
  }

  public void setMinRttPort2(String minRttPort2) {
    this.minRttPort2 = minRttPort2;
  }

  public String getAvgRttPort2() {
    return avgRttPort2;
  }

  public void setAvgRttPort2(String avgRttPort2) {
    this.avgRttPort2 = avgRttPort2;
  }

  public String getMaxRttPort2() {
    return maxRttPort2;
  }

  public void setMaxRttPort2(String maxRttPort2) {
    this.maxRttPort2 = maxRttPort2;
  }

  public String getMacVlan10() {
    return macVlan10;
  }

  public void setMacVlan10(String macVlan10) {
    this.macVlan10 = macVlan10;
  }

  public String getMacVlan20() {
    return macVlan20;
  }

  public void setMacVlan20(String macVlan20) {
    this.macVlan20 = macVlan20;
  }

  public String getMacCellular() {
    return macCellular;
  }

  public void setMacCellular(String macCellular) {
    this.macCellular = macCellular;
  }

  public String getPublicVlanIpVlan10() {
    return publicVlanIpVlan10;
  }

  public void setPublicVlanIpVlan10(String publicVlanIpVlan10) {
    this.publicVlanIpVlan10 = publicVlanIpVlan10;
  }

  public String getPublicVlanIpVlan20() {
    return publicVlanIpVlan20;
  }

  public void setPublicVlanIpVlan20(String publicVlanIpVlan20) {
    this.publicVlanIpVlan20 = publicVlanIpVlan20;
  }

  public String getPublicIpCellular() {
    return publicIpCellular;
  }

  public void setPublicIpCellular(String publicIpCellular) {
    this.publicIpCellular = publicIpCellular;
  }

  public String getStatusVlan10() {
    return statusVlan10;
  }

  public void setStatusVlan10(String statusVlan10) {
    this.statusVlan10 = statusVlan10;
  }

  public String getStatusVlan20() {
    return statusVlan20;
  }

  public void setStatusVlan20(String statusVlan20) {
    this.statusVlan20 = statusVlan20;
  }

  public String getStatusCellular() {
    return statusCellular;
  }

  public void setStatusCellular(String statusCellular) {
    this.statusCellular = statusCellular;
  }

  public String getCirProvVlan10() {
    return cirProvVlan10;
  }

  public void setCirProvVlan10(String cirProvVlan10) {
    this.cirProvVlan10 = cirProvVlan10;
  }

  public String getCirProvVlan20() {
    return cirProvVlan20;
  }

  public void setCirProvVlan20(String cirProvVlan20) {
    this.cirProvVlan20 = cirProvVlan20;
  }

  public String getCirProvCellular() {
    return cirProvCellular;
  }

  public void setCirProvCellular(String cirProvCellular) {
    this.cirProvCellular = cirProvCellular;
  }

  public String getMinRttVlan10() {
    return minRttVlan10;
  }

  public void setMinRttVlan10(String minRttVlan10) {
    this.minRttVlan10 = minRttVlan10;
  }

  public String getAvgRttVlan10() {
    return avgRttVlan10;
  }

  public void setAvgRttVlan10(String avgRttVlan10) {
    this.avgRttVlan10 = avgRttVlan10;
  }

  public String getMaxRttVlan10() {
    return maxRttVlan10;
  }

  public void setMaxRttVlan10(String maxRttVlan10) {
    this.maxRttVlan10 = maxRttVlan10;
  }

  public String getPacketLossVlan10() {
    return packetLossVlan10;
  }

  public void setPacketLossVlan10(String packetLossVlan10) {
    this.packetLossVlan10 = packetLossVlan10;
  }

  public String getMinRttVlan20() {
    return minRttVlan20;
  }

  public void setMinRttVlan20(String minRttVlan20) {
    this.minRttVlan20 = minRttVlan20;
  }

  public String getAvgRttVlan20() {
    return avgRttVlan20;
  }

  public void setAvgRttVlan20(String avgRttVlan20) {
    this.avgRttVlan20 = avgRttVlan20;
  }

  public String getMaxRttVlan20() {
    return maxRttVlan20;
  }

  public void setMaxRttVlan20(String maxRttVlan20) {
    this.maxRttVlan20 = maxRttVlan20;
  }

  public String getPacketLossVlan20() {
    return packetLossVlan20;
  }

  public void setPacketLossVlan20(String packetLossVlan20) {
    this.packetLossVlan20 = packetLossVlan20;
  }

  public String getMinRttCellular() {
    return minRttCellular;
  }

  public void setMinRttCellular(String minRttCellular) {
    this.minRttCellular = minRttCellular;
  }

  public String getAvgRttCellular() {
    return avgRttCellular;
  }

  public void setAvgRttCellular(String avgRttCellular) {
    this.avgRttCellular = avgRttCellular;
  }

  public String getMaxRttCellular() {
    return maxRttCellular;
  }

  public void setMaxRttCellular(String maxRttCellular) {
    this.maxRttCellular = maxRttCellular;
  }

  public String getPacketLossCellular() {
    return packetLossCellular;
  }

  public void setPacketLossCellular(String packetLossCellular) {
    this.packetLossCellular = packetLossCellular;
  }
  // @Column(name = "site_id")
  // private String site_id;

  // public String getSite_id() {
  // return site_id;
  // }

  // public void setSite_id(String site_id) {
  // this.site_id = site_id;
  // }

  // @Column(name = "router")
  // private String router;

  // public String getRouter() {
  // return router;
  // }

  // public void setRouter(String router) {
  // this.router = router;
  // }

  // @Column(name = "cir_prov_port2")
  // private String primary_provider;

  // public String getPrimary_provider() {
  // return primary_provider;
  // }

  // public void setPrimary_provider(String primary_provider) {
  // this.primary_provider = primary_provider;
  // }

  // @Column(name = "cir_prov_port1")
  // private String backup_provider;

  // public String getBackup_provider() {
  // return backup_provider;
  // }

  // public void setBackup_provider(String backup_provider) {
  // this.backup_provider = backup_provider;
  // }

}
