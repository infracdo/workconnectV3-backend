package com.tracker_app.tracker.DataSource_WorkConnect.Entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
    private String user;
    private String log_type;
    private double longitude;
    private double latitude;
    private LocalDateTime datetime;

    public Long getid(){
        return id;
    }
    public String getuser(){
        return user;
    }
    public String getlog_type(){
        return log_type;
    }
    public double getlongitude(){
        return longitude;
    }
    public double getlatitude(){
        return latitude;
    }
    public LocalDateTime getdatetime(){
        return datetime;
    }

    public void setuser(String user) {
        this.user = user;
    }
    public void setlog_type(String log_type) {
        this.log_type = log_type;
    }
    public void setlongitude(double longitude) {
        this.longitude = longitude;
    }
    public void setlatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setdatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }
}
