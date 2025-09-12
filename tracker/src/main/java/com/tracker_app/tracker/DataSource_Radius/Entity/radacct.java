package com.tracker_app.tracker.DataSource_Radius.Entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "radacct")
public class radacct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long radacctid;

    @Column(name = "acctsessionid")
    private String acctsessionid;

    @Column(name = "acctuniqueid")
    private String acctuniqueid;

    @Column(name = "username")
    private String username;
    
    @Column(name = "groupname")
    private String groupname;
    
    @Column(name = "realm")
    private String realm;
    
    @Column(name = "nasipaddress")
    private String nasipaddress;
    
    @Column(name = "nasportid")
    private String nasportid;
    
    @Column(name = "nasporttype")
    private String nasporttype;
    
    @Column(name = "acctstarttime")
    private Date acctstarttime;
    
    @Column(name = "acctstoptime")
    private Date acctstoptime;
    
    @Column(name = "acctsessiontime")
    private Integer acctsessiontime;
    
    @Column(name = "acctauthentic")
    private String acctauthentic;
    
    @Column(name = "connectinfo_start")
    private String connectinfo_start;
    
    @Column(name = "connectinfo_stop")
    private String connectinfo_stop;
    
    @Column(name = "acctinputoctets")
    private Long acctinputoctets;
    
    @Column(name = "acctoutputoctets")
    private Long acctoutputoctets;
    
    @Column(name = "calledstationid")
    private String calledstationid;
    
    @Column(name = "callingstationid")
    private String callingstationid;
    
    @Column(name = "acctterminatecause")
    private String acctterminatecause;
    
    @Column(name = "servicetype")
    private String servicetype;
    
    @Column(name = "framedprotocol")
    private String framedprotocol;
    
    @Column(name = "framedipaddress")
    private String framedipaddress;
    
    @Column(name = "acctstartdelay")
    private Integer acctstartdelay;
    
    @Column(name = "acctstopdelay")
    private Integer acctstopdelay;
    
    @Column(name = "xascendsessionsvrkey")
    private String xascendsessionsvrkey;

}
