package com.tracker_app.tracker.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tracker_app.tracker.DataSource_WorkConnect.Entity.tenant_lookup;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.tenant_lookup_repo;
import com.tracker_app.tracker.Helper.AuthService;
import com.tracker_app.tracker.Helper.prometheus_helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/prometheus")
public class prometheus_controller {
    
    @Autowired
    private prometheus_helper prometheus_help;
    @Autowired
    private AuthService authService;
    @Autowired
    private tenant_lookup_repo tenant_repo;
    
    @GetMapping(path = "/network_status_summary")
    public ResponseEntity<Object> network_status_summary(
        @RequestParam(value = "site_tenant", required = false) String site_tenant,
        @RequestHeader(value="Authorization") String auth
    ) throws JSONException, ParseException, IOException, InterruptedException{

        String tenant = "";
        try {
            Map<String, List<String>> attrib = authService.getUserAttributes(auth);
            System.out.println("TENANT: " + attrib);
            String slug = attrib.get("tenant").get(0);
            String site_tenant_slug = tenant_repo.get_tenantslug_by_name(site_tenant);
            if(site_tenant_slug.contains(slug)){
                tenant = tenant_repo.get_promname_by_slug(slug);
                if(tenant == null){
                    tenant = "";
                }
            }
            else{
                Object obj = new Object();
                return new ResponseEntity<Object>(obj,HttpStatus.OK);
            }
        } catch (Exception e) {
            //TODO: handle exception
            String slug = tenant_repo.get_tenantslug_by_name(site_tenant);
            tenant = tenant_repo.get_promname_by_slug(slug.trim());
        }
        
        return new ResponseEntity<Object>(prometheus_help.get_network_status_summary(tenant),HttpStatus.OK);
    }

    @GetMapping(path = "/tenant_name/update")
    public ResponseEntity<Object> tenant_name_update(
        @RequestParam(value = "atis_tenant") String atis_tenant,
        @RequestParam(value = "prom_tenant") String prom_tenant,
        @RequestHeader(value="Authorization") String auth
    )
    {   Map<String, String> result = authService.isTokenValid(auth);
        if (result.get("status").equals("SUCCESS")) {
            if(authService.getRoles(auth).contains("ROLE_SUPERADMIN")) {
                try {
                    tenant_lookup tenant = tenant_repo.get_lookup_by_name(atis_tenant);
                    tenant.setProm_name(prom_tenant);
                    tenant_repo.save(tenant);
                    return new ResponseEntity<Object>("OK",HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<Object>("Error",HttpStatus.CONFLICT);
                }
            }else{
                return new ResponseEntity<Object>("Not Authorized",HttpStatus.UNAUTHORIZED);
            }
        }
        else{
            return new ResponseEntity<Object>("Not Authorized",HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(path = "/site/traffic_in")
    public ResponseEntity<Object> traffic_in(
        @RequestParam(value = "site_id", required = false) String site_id,
        @RequestParam(value = "str_end_date", required = false) String str_end_date,
        @RequestParam(value = "hours", required = false) Integer hours, 
        @RequestParam(value = "interval_in_min", required = false) Integer interval_in_min
    ) throws JSONException, ParseException, IOException, InterruptedException{
        try {
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            result = (List<Map<String, Object>>) prometheus_help.get_site_in_network_traffic(site_id,str_end_date,hours,interval_in_min);
            if(result.size()==0){
                result = (List<Map<String, Object>>) prometheus_help.get_site_in_network_traffic(site_id,str_end_date,hours,interval_in_min);
            }
            return new ResponseEntity<Object>(result,HttpStatus.OK);    
        } catch (Exception e) {
            return new ResponseEntity<Object>(prometheus_help.get_site_in_network_traffic(site_id,str_end_date,hours,interval_in_min),HttpStatus.OK);
        }
    }
    
    @GetMapping(path = "/site/traffic_out")
    public ResponseEntity<Object> traffic_out(
        @RequestParam(value = "site_id", required = false) String site_id,
        @RequestParam(value = "str_end_date", required = false) String str_end_date,
        @RequestParam(value = "hours", required = false) Integer hours, 
        @RequestParam(value = "interval_in_min", required = false) Integer interval_in_min
    ) throws JSONException, ParseException, IOException, InterruptedException{
       
        try {
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            result = (List<Map<String, Object>>) prometheus_help.get_site_out_network_traffic(site_id,str_end_date,hours,interval_in_min);
            if(result.size()==0){
                result = (List<Map<String, Object>>) prometheus_help.get_site_out_network_traffic(site_id,str_end_date,hours,interval_in_min);
            }
            return new ResponseEntity<Object>(result,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Object>(prometheus_help.get_site_in_network_traffic(site_id,str_end_date,hours,interval_in_min),HttpStatus.OK);
        }   
    }

    @GetMapping(path = "/test")
    public ResponseEntity<Object> test(
        @RequestParam(value = "site_id", required = false) String site_id
    ) throws JSONException, ParseException, IOException, InterruptedException{
        
        return new ResponseEntity<Object>(prometheus_help.get_circuit_status("loopback 1", site_id),HttpStatus.OK);
    }
}
