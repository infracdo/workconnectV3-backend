package com.tracker_app.tracker.DataSource_WorkConnect.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class accounts_and_cookies {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
    private String name;
    private String cookie;

    public Long getid(){
        return id;
    }
    public String getname(){
        return name;
    }
    public String getcookie(){
        return cookie;
    }

    public void setname(String name) {
        this.name = name;
    }
    public void setcookie(String cookie) {
        this.cookie = cookie;
    }
}
