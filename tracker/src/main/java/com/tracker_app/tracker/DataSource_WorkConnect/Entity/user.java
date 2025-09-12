package com.tracker_app.tracker.DataSource_WorkConnect.Entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class user {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String KeycloakId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String remarks;
    private String isEnabled;
    private Date dateCreated;
    private Date lastUpdated;
    private String createdBy;
    private String lastUpdatedBy;
    private String roles;  
    private String tenant;   

    public Long getid() {
        return id;
    }
    public String getKeycloakId() {
        return KeycloakId;
    }
    public String getusername() {
        return username;
    }
    public String getfirstName() {
         return firstName;
    }
    public String getlastName() {
         return lastName;
    }
    public String getemail() {
         return email;
    }
    public String getmobileNumber() {
        return mobileNumber;
    }
    public String getremarks() {
        return remarks;
    }
    public String getisEnabled() {
        return isEnabled;
    }
    public Date getdateCreated() {
        return dateCreated;
    }
    public Date getlastUpdated() {
        return lastUpdated;
    }
    public String getcreatedBy() {
        return createdBy;
    }
    public String getlastUpdatedBy() {
        return lastUpdatedBy;
    }
    public String getroles(){
        return roles;
    }
    public String gettenant(){
        return tenant;
    }



    public void setKeycloakId( String KeycloakId){
        this.KeycloakId = KeycloakId;
    }
    public void setusername( String username ) {
        this.username = username;
    }
    public void setfirstName( String firstName ) {
        this.firstName = firstName;
    }
    public void setlastName( String lastName ) {
        this.lastName = lastName;
    }
    public void setemail( String email ) {
        this.email = email;
    }
    public void setmobileNumber( String mobileNumber ) {
        this.mobileNumber = mobileNumber;
    }
    public void setremarks( String remarks ) {
        this.remarks = remarks;
    }
    public void setisEnabled( String isEnabled ) {
        this.isEnabled = isEnabled;
    }
    public void setdateCreated( Date dateCreated ) {
        this.dateCreated = dateCreated;
    }
    public void setlastUpdated( Date lastUpdated ) {
        this.lastUpdated = lastUpdated;
    }
    public void setcreatedBy( String createdBy ) {
        this.createdBy = createdBy;
    }
    public void setlastUpdatedBy( String lastUpdatedBy ) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    public void setroles(String roles){
        this.roles = roles;
    }
    public void settenant(String tenant){
        this.tenant = tenant;
    }
}
