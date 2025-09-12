package com.tracker_app.tracker.DataSource_NMS.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "provider_circuits_report")
public class provider_circuit_report {

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

    @Column(name = "provider")
    private String provider;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Column(name = "atis")
    private Integer atis;

    public Integer getAtis() {
        return atis;
    }

    public void setAtis(Integer atis) {
        this.atis = atis;
    }

    @Column(name = "active")
    private Integer active;

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    @Column(name = "not_found_w_mac")
    private Integer notFoundWMac;

    public Integer getNotFoundWMac() {
        return notFoundWMac;
    }

    public void setNotFoundWMac(Integer notFoundWMac) {
        this.notFoundWMac = notFoundWMac;
    }

    @Column(name = "not_found_wout_mac")
    private Integer notFoundWoutMac;

    public Integer getNotFoundWoutMac() {
        return notFoundWoutMac;
    }

    public void setNotFoundWoutMac(Integer notFoundWoutMac) {
        this.notFoundWoutMac = notFoundWoutMac;
    }

    @Column(name = "not_in_list")
    private Integer notInList;

    public Integer getNotInList() {
        return notInList;
    }

    public void setNotInList(Integer notInList) {
        this.notInList = notInList;
    }

}
