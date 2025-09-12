package com.tracker_app.tracker.DataSource_NMS.Entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "store_info_circuits")
public class store_info_circuits {

    @Id
    @Column(name = "site_id")
    private String siteId;

    @Column(name = "prov_vpn_0")
    private String providerVpn0;

    @Column(name = "prov_vpn_1")
    private String providerVpn1;

    @Column(name = "prov_vpn_2")
    private String providerVpn2;

    @Column(name = "gtw_vpn_0")
    private String gtwVpn0;

    @Column(name = "status_vpn_1")
    private String statusVpn1;

    @Column(name = "status_vpn_2")
    private String statusVpn2;

    @Column(name = "discovered_ip_vpn_1")
    private String discoveredIpVpn1;

    @Column(name = "discovered_ip_vpn_2")
    private String discoveredIpVpn2;

    @Column(name = "gateway_ip_1")
    private String gatewayIp1;

    @Column(name = "gateway_ip_2")
    private String gatewayIp2;

    @Column(name = "mac_address_1")
    private String macAddress1;

    @Column(name = "mac_address_2")
    private String macAddress2;

    @Column(name = "last_up_vpn_1")
    private String lastUpVpn1;

    @Column(name = "last_up_vpn_2")
    private String lastUpVpn2;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
