package com.tracker_app.tracker.CustomObject;

import org.springframework.boot.configurationprocessor.json.JSONArray;

public class keycloack_user {
   private String username;
   private String firstName;
   private String lastName;
   private String email;
   private String mobileNumber;
   private String remarks;
   private Object role;
   private String isEnabled;

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
   public Object getrole() {
        return role;
   }
   public String getisEnabled() {
        return isEnabled;
   }
}
