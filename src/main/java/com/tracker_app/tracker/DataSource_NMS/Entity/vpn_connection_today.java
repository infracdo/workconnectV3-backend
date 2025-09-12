package com.tracker_app.tracker.DataSource_NMS.Entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class vpn_connection_today {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "site_id")
    private String siteId;

    @Column(name = "date_timestamp", columnDefinition = "TIMESTAMP(3)") // 3 milliseconds precision (same with db)
    private LocalDateTime dateTimestamp;

    @Column(name = "public_ip")
    private String publicIp;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public LocalDateTime getDateTimestamp() {
        return dateTimestamp;
    }

    public void setDateTimestamp(LocalDateTime dateTimestamp) {
        this.dateTimestamp = dateTimestamp;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }
}
