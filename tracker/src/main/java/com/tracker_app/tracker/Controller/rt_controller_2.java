package com.tracker_app.tracker.Controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.tracker_app.tracker.Helper.rt_helper_rest2;

import org.apache.catalina.filters.HttpHeaderSecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage.Body;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api2/rt")
public class rt_controller_2 {

    @Value("${RT.URL}")
    String rt_url;

    @Autowired
    rt_helper_rest2 rt = new rt_helper_rest2();
    

    @PostMapping(path = "/auth")
    public ResponseEntity<String> rt_login(@RequestBody String payload){
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Content-Type-Options", "nosniff");
        
        String cookie = rt.rt_auth(rt_url, payload);
        System.out.println(cookie);
        try {
            JSONObject json_payload = new JSONObject(payload);
            
            if(cookie.contains("401 Credentials required")){
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }else{
                return new ResponseEntity<String>(cookie,headers,HttpStatus.OK);
            }
        }catch (Exception e) {
            return new ResponseEntity<String>("JSON ERROR:"+e,headers,HttpStatus.OK);
            //TODO: handle exception
        }
          
    }

    @PostMapping(path = "/get/queues")
    public ResponseEntity<String> rt_get_queues(@RequestBody String payload){
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Content-Type-Options", "nosniff");
        
        String result = rt.rt_get_queue_all(rt_url, payload);
        System.out.println(result);

            
        if(result.contains("401 Credentials required")){
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }else{
            return new ResponseEntity<String>(result,headers,HttpStatus.OK);
        }
          
    }

    @PostMapping(path = "/get/tickets")
    public ResponseEntity<String> rt_get_ticket(@RequestBody String payload){
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Content-Type-Options", "nosniff");
        
        String result = rt.rt_get_tickets(rt_url, payload);
        System.out.println(result);

            
        if(result.contains("401 Credentials required")){
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }else{
            return new ResponseEntity<String>(result,headers,HttpStatus.OK);
        }
          
    }

    @PostMapping(path = "/create/ticket")
    public ResponseEntity<String> rt_create_ticket(@RequestBody String payload){
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Content-Type-Options", "nosniff");
        
        String result = rt.rt_create_ticket(rt_url, payload);
        System.out.println(result);
            
        if(result.contains("401 Credentials required")){
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }else{
            return new ResponseEntity<String>(result,headers,HttpStatus.OK);
        }
          
    }

    @PutMapping(path = "/update/ticket")
    public ResponseEntity<String> rt_update_ticket(@RequestBody String payload){
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Content-Type-Options", "nosniff");
        
        String result = rt.rt_update_ticket(rt_url, payload);
        System.out.println(result);
            
        if(result.contains("401 Credentials required")){
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }else{
            return new ResponseEntity<String>(result,headers,HttpStatus.OK);
        }
          
    }


    @PostMapping(path = "/comment/ticket")
    public ResponseEntity<String> rt_comment_ticket(@RequestBody String payload){
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Content-Type-Options", "nosniff");
        
        String result = rt.rt_comment_ticket(rt_url, payload);
        System.out.println(result);

        if(result.contains("401 Credentials required")){
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }else{
            return new ResponseEntity<String>(result,headers,HttpStatus.OK);
        }
          
    }


}
