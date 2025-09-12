package com.tracker_app.tracker.Controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.tracker_app.tracker.CustomObject.keycloack_user;
import com.tracker_app.tracker.Helper.AuthService;
//import com.tracker_app.tracker.Helper.kafka_helper;
import com.tracker_app.tracker.Helper.keycloak_helper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/user")
public class keycloak_controller {

    @Autowired
    private keycloak_helper keycloakhelper;
    @Autowired
    private AuthService authService;
    //@Autowired
    //private kafka_helper kafka = new kafka_helper();

    @Value("${kafka.logs.topic}")
    private String kafka_topic;

    @PostMapping(path = "/")
    public ResponseEntity<Object> create_user(
        @RequestHeader(value="Authorization",required=false) String auth,
        @RequestBody Map<String, String> params
    ) throws JSONException{
        return keycloakhelper.create_user(auth, params);
    }
    
    @PostMapping(path = "/roles")
    public ResponseEntity<Object> user_roles(
        @RequestHeader(value="Authorization",required=false) String auth,
        @RequestBody Map<String, String> params) throws JSONException{
        return keycloakhelper.get_user_roles(auth,params);
    }   
   
    @PostMapping(path = "/search")
    public ResponseEntity<Object> get_user(
        @RequestHeader(value="Authorization",required=false) String auth,
        @RequestBody Map<String, String> params
    ) throws ParseException{
        return keycloakhelper.getUsers(auth,params);
    }    
    
    @GetMapping(path = "/sync")
    public ResponseEntity<Object> sync_user( ) throws JSONException{
        new Thread(()->{
            try {
                keycloakhelper.sync_users();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }).start();
        return new ResponseEntity<Object>("Done",HttpStatus.OK);
    }   
   
    @PatchMapping(path = "/")
    public ResponseEntity<Object> patch_user(
        @RequestHeader(value="Authorization",required=false) String auth,
        @RequestBody Map<String, String> params
    ) throws JSONException{
        return keycloakhelper.updateUser(auth,params);
    }

    @GetMapping(path = "/realm/roles")
    public ResponseEntity<Object> realm_roles(
        @RequestHeader(value="Authorization",required=false) String auth
    ) throws JSONException{
        return keycloakhelper.getRealmRoles(auth);
    }
    
    @PostMapping(path = "/realm/roles")
    public ResponseEntity<Object> create_roles(
        @RequestHeader(value="Authorization",required=false) String auth,
        @RequestParam(value = "role_name") String role_name,
        @RequestParam(value = "role_description", required = false) String role_description
    ) 
    {
        return keycloakhelper.create_roles(role_name,role_description);
    }
    
    @GetMapping(path = "/attributes")
    public ResponseEntity<Object> attributes(
        @RequestHeader(value="Authorization") String auth
    ){
        return new ResponseEntity<Object>(authService.getUserAttributes(auth), HttpStatus.OK);
    }
        
    @GetMapping(path = "/log")
    public ResponseEntity<Object> user_log(
        @RequestHeader(value="Authorization") String auth,
        @RequestParam(value = "log_type") String log_type
    ) throws JSONException{
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        formatter.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Manila")));
        Date date = new Date(System.currentTimeMillis());

        Map<String, String> result = authService.isTokenValid(auth);
        Map<String, Object> user_detail = (Map<String, Object>) authService.getUserUsername(auth);
        Map<String, Object> message = new HashMap<String, Object>();
        /* 
        if (result.get("status").equals("SUCCESS")) {
            message.put("user_details",user_detail);
            message.put("log_type",log_type);
            message.put("status","success");
            message.put("time",formatter.format(date));
            kafka.sendMessage(message.toString(), kafka_topic);
        }else{
            message.put("user_details",user_detail);
            message.put("log_type",log_type);
            message.put("status","fail");
            message.put("time",formatter.format(date));
            kafka.sendMessage(message.toString(), kafka_topic);
        }
        */
        System.out.println(message.toString());
        return new ResponseEntity<Object>("Done",HttpStatus.OK);
    }
}
