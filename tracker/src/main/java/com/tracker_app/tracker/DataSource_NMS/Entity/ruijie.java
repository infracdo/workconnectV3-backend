package com.tracker_app.tracker.DataSource_NMS.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity // labels an entity that can be used for JPA
@Table(name = "mac_ruijie")
public class ruijie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String site_id;

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    private String vlan10;

    public String getVlan10() {
        return vlan10;
    }

    public void setVlan10(String vlan10) {
        this.vlan10 = vlan10;
    }

    private String vlan20;

    public String getVlan20() {
        return vlan20;
    }

    public void setVlan20(String vlan20) {
        this.vlan20 = vlan20;
    }

    private String cellular;

    public String getCellular() {
        return cellular;
    }

    public void setCellular(String cellular) {
        this.cellular = cellular;
    }

    private String router;

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

}
