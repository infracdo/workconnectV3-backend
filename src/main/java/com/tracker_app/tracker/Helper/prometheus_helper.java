package com.tracker_app.tracker.Helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.tracker_app.tracker.DataSource_WorkConnect.Repo.tenant_lookup_repo;
import com.tracker_app.tracker.DataSource_elasticsearch.service.ElasticSiteSummary_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jdk.jfr.Timestamp;



@Service
public class prometheus_helper {

    @Value("${PROMETHEUS.URL}")
    private String prometheus_url;

    @Value("${INFLUX.URL}")
    private String influx_url;
    
    @Autowired
    private tenant_lookup_repo tenant_repo;

    @Autowired
    private ElasticSiteSummary_service elksite_sum_service;

    public Object get_network_status_summary(String site_tenant) throws IOException, InterruptedException, JSONException, ParseException{
        String loopback_0 = "";
        String tenant_name = tenant_repo.get_tenant_by_promname(site_tenant);

        if(tenant_name.contains("Ultra Mega Multi-Sales Inc")){
            loopback_0 = "ultramega_tcp";
        }
        /*
        else if(tenant_name.contains("DICT")){
            loopback_0 = "sma2_sites";
        }
        */
        else{
            loopback_0 = "loopback 0";
        }

        //Active Sites
        String prom_url = prometheus_url+"/api/v1/query?query=count(lo_status{job=\""+loopback_0+"\", site_status='active', site_tenant=\""+site_tenant+"\"} == 1) OR vector(0)";
        prom_url = prom_url.replace(" ", "%20");
        JSONObject obj = new JSONObject();
        System.out.println(prom_url);
        URL url = new URL(prom_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);

        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.flush();
        out.close();
     
        int status = con.getResponseCode();

        int total_sites = 0, active_sites = 0, down_sites = 0, temporary_closed_sites = 0;

        if(status==204||status==201||status==200){
            StringBuffer contBuffer = new StringBuffer();
            String inputLine = null;
    
            BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((inputLine = in.readLine())!=null){
                contBuffer.append(inputLine);
            } 
            con.disconnect();
           
            JSONObject data = new JSONObject(contBuffer.toString()).getJSONObject("data");
            JSONObject result = data.getJSONArray("result").getJSONObject(0);

            try {
                active_sites = result.getJSONArray("value").getInt(1);
                obj.put("active_sites", active_sites);
            }catch (Exception e) {
                System.out.println(e);
                return active_sites;
            }

        }
        //Down Sites
        prom_url = prometheus_url+"/api/v1/query?query=count(lo_status{job=\""+loopback_0+"\", site_status='active', site_tenant=\""+site_tenant+"\"} == 0) OR vector(0)";
        prom_url = prom_url.replace(" ", "%20");
        url = new URL(prom_url);
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);

        out = new DataOutputStream(con.getOutputStream());
        out.flush();
        out.close();
     
        status = con.getResponseCode();
        if(status==204||status==201||status==200){
            StringBuffer contBuffer = new StringBuffer();
            String inputLine = null;
    
            BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((inputLine = in.readLine())!=null){
                contBuffer.append(inputLine);
            } 
            con.disconnect();
           
            JSONObject data = new JSONObject(contBuffer.toString()).getJSONObject("data");
            JSONObject result = data.getJSONArray("result").getJSONObject(0);

            try {
                down_sites = result.getJSONArray("value").getInt(1);
                obj.put("down_sites", down_sites);
            }catch (Exception e) {
                System.out.println(e);
                return down_sites;
            }

        }
        //Temporarily Closed
        prom_url = prometheus_url+"/api/v1/query?query=count(lo_status{job=\""+loopback_0+"\", site_status!='active', site_tenant=\""+site_tenant+"\"}) OR vector(0)";
        prom_url = prom_url.replace(" ", "%20");
        url = new URL(prom_url);
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);

        out = new DataOutputStream(con.getOutputStream());
        out.flush();
        out.close();
     
        status = con.getResponseCode();
        if(status==204||status==201||status==200){
            StringBuffer contBuffer = new StringBuffer();
            String inputLine = null;
    
            BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((inputLine = in.readLine())!=null){
                contBuffer.append(inputLine);
            } 
            con.disconnect();
           
            JSONObject data = new JSONObject(contBuffer.toString()).getJSONObject("data");
            JSONObject result = data.getJSONArray("result").getJSONObject(0);
            try {
                temporary_closed_sites = result.getJSONArray("value").getInt(1);
                obj.put("temporary_closed_sites", temporary_closed_sites);
            }catch (Exception e) {
                System.out.println(e);
                return temporary_closed_sites;
            }

        }
        //Total
        prom_url = prometheus_url+"/api/v1/query?query=count(lo_status{job=\""+loopback_0+"\", site_tenant=\""+site_tenant+"\"}) OR vector(0)";
        prom_url = prom_url.replace(" ", "%20");
        url = new URL(prom_url);
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);

        out = new DataOutputStream(con.getOutputStream());
        out.flush();
        out.close();
     
        status = con.getResponseCode();
        if(status==204||status==201||status==200){
            StringBuffer contBuffer = new StringBuffer();
            String inputLine = null;
    
            BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((inputLine = in.readLine())!=null){
                contBuffer.append(inputLine);
            } 
            con.disconnect();
           
            JSONObject data = new JSONObject(contBuffer.toString()).getJSONObject("data");
            JSONObject result = data.getJSONArray("result").getJSONObject(0);

            try {
                total_sites = result.getJSONArray("value").getInt(1);
                obj.put("total_sites", total_sites);
            }catch (Exception e) {
                System.out.println(e);
                return total_sites;
            }

        }

        return obj.toString();
    }

    public Object get_circuit_data(String site_id,String str_end_date, Long hours_interval) throws IOException, InterruptedException, JSONException{
        
        String loopback_0 = "";
        
        String tenant_name = elksite_sum_service.findBySiteId(site_id).getTenant_name();

        if(tenant_name.contains("Ultra Mega Multi-Sales Inc")){
            loopback_0 = "ultramega_tcp";
        }
        /*
        else if(tenant_name.contains("DICT")){
            loopback_0 = "sma2_sites";
        }
        */
        else{
            loopback_0 = "loopback 0";
        }

        String prom_url = prometheus_url+"/api/v1/query?query=lo_status{site_id=\""+site_id+"\"}";
        System.out.println(prom_url);
        URL url = new URL(prom_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);

        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.flush();
        out.close();
     
        int status = con.getResponseCode();

        if(status==204||status==201||status==200){
            StringBuffer contBuffer = new StringBuffer();
            String inputLine = null;
    
            BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((inputLine = in.readLine())!=null){
                contBuffer.append(inputLine);
            } 
            con.disconnect();
           
            JSONObject data = new JSONObject(contBuffer.toString()).getJSONObject("data");
            JSONArray array = new JSONArray(data.get("result").toString());
            
            List<Map<String, Object>> sites = new ArrayList<Map<String, Object>>();
            if(array.length()==0){
                Map<String, Object> obj = new HashMap<String, Object>();
                obj.put("status",HttpStatus.OK);
                obj.put("error_message", "site is not yet added to prometheus");
                return obj;
            }
            for(int i = 0; i < array.length(); i++){
                try {
                    Map<String, Object> obj = new HashMap<String, Object>();
                    JSONObject currnet_data = array.getJSONObject(i);
                    String value = currnet_data.get("value").toString().replace("[","").replace("]","");
                    String stat = value.split(",")[1];

                    if(currnet_data.getJSONObject("metric").getString("job").contains(loopback_0)){
                        List<Map<String, Object>> history = new ArrayList<Map<String, Object>>();
                        history = (List<Map<String, Object>>) get_historical_status(site_id, loopback_0,str_end_date,hours_interval);
                        System.out.println("Loopback 0 size"+history.size());
                        if(history.size()==0){
                            System.out.println("Loopback 0 size"+history.size());
                            history = (List<Map<String, Object>>) get_historical_status(site_id, loopback_0,str_end_date,hours_interval);
                            if(history.size()==0){
                                continue;
                            }
                        }

                        obj.put("history",history);
                        obj.put("site_id", currnet_data.getJSONObject("metric").get("site_id"));
                        if(stat.contains("1")) obj.put("loopback0", 1);
                        else obj.put("loopback0", 0);
                        obj.put("loopback0_ip", currnet_data.getJSONObject("metric").get("instance"));
                        sites.add(obj);
                    } 
                    if(currnet_data.getJSONObject("metric").getString("job").contains("loopback 1")){
                        List<Map<String, Object>> history = new ArrayList<Map<String, Object>>();
                        history = (List<Map<String, Object>>) get_historical_status(site_id, "loopback 1",str_end_date,hours_interval);
                        if(history.size()==0){
                            history = (List<Map<String, Object>>) get_historical_status(site_id, "loopback 1",str_end_date,hours_interval);
                            if(history.size()==0){
                                continue;
                            }
                        }

                        obj.put("history",history);
                        obj.put("site_id", currnet_data.getJSONObject("metric").get("site_id"));
                        if(stat.contains("1")) obj.put("loopback1", 1);
                        else obj.put("loopback1", 0);
                        obj.put("loopback1_ip", currnet_data.getJSONObject("metric").get("instance"));
                        try {
                            obj.put("loopback1_provider", currnet_data.getJSONObject("metric").get("loopback_provider"));    
                        } catch (Exception e) {
                            obj.put("loopback1_provider", "None");
                        }
                        
                        sites.add(obj);
                    } 
                    if(currnet_data.getJSONObject("metric").getString("job").contains("loopback 2")){
                        List<Map<String, Object>> history = new ArrayList<Map<String, Object>>();
                        history = (List<Map<String, Object>>) get_historical_status(site_id, "loopback 2",str_end_date,hours_interval);
                        if(history.size()==0){
                            history = (List<Map<String, Object>>) get_historical_status(site_id, "loopback 2",str_end_date,hours_interval);
                            if(history.size()==0){
                                continue;
                            }
                        }

                        obj.put("history",history);
                        obj.put("site_id", currnet_data.getJSONObject("metric").get("site_id"));
                        if(stat.contains("1")) obj.put("loopback2", 1);
                        else obj.put("loopback2", 0);
                        obj.put("loopback2_ip", currnet_data.getJSONObject("metric").get("instance"));
                        try {
                            obj.put("loopback2_provider", currnet_data.getJSONObject("metric").get("loopback_provider"));    
                        } catch (Exception e) {
                            obj.put("loopback2_provider", "None");
                        }
                        sites.add(obj);
                    } 
                    if(currnet_data.getJSONObject("metric").getString("job").contains("loopback 3")){
                        List<Map<String, Object>> history = new ArrayList<Map<String, Object>>();
                        history = (List<Map<String, Object>>) get_historical_status(site_id, "loopback 3",str_end_date,hours_interval);
                        if(history.size()==0){
                            history = (List<Map<String, Object>>) get_historical_status(site_id, "loopback 3",str_end_date,hours_interval);
                            if(history.size()==0){
                                continue;
                            }
                        }
                        
                        obj.put("history",history);
                        obj.put("site_id", currnet_data.getJSONObject("metric").get("site_id"));
                        if(stat.contains("1")) obj.put("loopback3", 1);
                        else obj.put("loopback3", 0);
                        obj.put("loopback3_ip", currnet_data.getJSONObject("metric").get("instance"));
                        try {
                            obj.put("loopback3_provider", currnet_data.getJSONObject("metric").get("loopback_provider"));    
                        } catch (Exception e) {
                            obj.put("loopback3_provider", "None");
                        }
                        sites.add(obj);
                    } 
                } catch (Exception e) {
                    System.out.println(e);
                    continue;
                }
            }
            if(sites.size()==0){
                Map<String, Object> obj = new HashMap<String, Object>();
                obj.put("status",HttpStatus.OK);
                obj.put("error_message", "no data from prometheus");
                return obj;
            }
            return sites;
        }
        return site_id;
    }

    public Object get_historical_status(String site_id,String loopback,String str_end_date, Long hours_interval) throws IOException, InterruptedException, JSONException, ParseException{
        
        str_end_date = str_end_date+"T23:59:59.99";
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Manila")));
        Date end_date = isoFormat.parse(str_end_date);
        LocalDateTime end = LocalDateTime.ofInstant(end_date.toInstant(), ZoneId.of("UTC"));
        LocalDateTime start = end.minusHours(hours_interval);     
        
        //System.out.println(LocalDateTime.ofInstant(end_date.toInstant(), ZoneId.of("Asia/Manila")));
        //System.out.println(end +" UTC");
        //System.out.println(start+" UTC");

        String prom_url = prometheus_url+"/api/v1/query_range?query=lo_status{job=\""+loopback.replace(" ", "%20")+"\",site_id=\""+site_id+"\"}&start="+start+"Z&end="+end+"Z&step=300s";
        URL url = new URL(prom_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);

        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.flush();
        out.close();
     
        int status = con.getResponseCode();
        //System.out.println(prom_url);
        System.out.println("get_historical_status: "+status);
        List<Map<String, Object>> sites = new ArrayList<Map<String, Object>>();
        if(status==204||status==201||status==200){
            StringBuffer contBuffer = new StringBuffer();
            String inputLine = null;
    
            BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((inputLine = in.readLine())!=null){
                contBuffer.append(inputLine);
            } 
            con.disconnect();
            
            try {
                JSONObject data = new JSONObject(contBuffer.toString()).getJSONObject("data");
                JSONObject result = data.getJSONArray("result").getJSONObject(0);
                JSONArray array = new JSONArray(result.getString("values"));
                
                
                for(int i = 0; i < array.length(); i++){
                    Map<String, Object> obj = new HashMap<String, Object>();
                    String[] value = array.getString(i).replace("[","").replace("]","").split(",");

                    try {
                        Calendar cal = Calendar.getInstance();
                        Double d = Double.parseDouble(value[0]);
                        //System.out.println(value[0]);
                        Long l = (long) (d * 1000);
                        cal.setTimeInMillis(l);
                        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
                        df.setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")));
                        //System.out.println(cal.getTime());
                        String date = df.format(cal.getTime());
                        obj.put("date", date);
                        obj.put("status", Integer.parseInt(value[1].replace("\"", "")));
                        sites.add(obj);

                        Long x = Long.parseLong(value[0]);
                        cal.setTimeInMillis(x*1000);
                        

                    } catch (Exception e) {
                        return sites;
                    }
                                    
                }
                return sites;    
            } catch (Exception e) {
                System.out.println(e);
                return sites;
            }
            
        }
        return sites;
    }
    public String getProblematicCircuits(String loopback) throws IOException, JSONException{
        String prom_url = prometheus_url+"/api/v1/query?query=count(uptime_availability_24hrs{job=\""+loopback.replace(" ", "%20")+"\",site_status=\"active\",site_tenant=\"PSC\"}<0.95)";
        URL url = new URL(prom_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);

        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.flush();
        out.close();
     
        int status = con.getResponseCode();

        //System.out.println(status);

        if(status==204||status==201||status==200){
            StringBuffer contBuffer = new StringBuffer();
            String inputLine = null;
            BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((inputLine = in.readLine())!=null){
                contBuffer.append(inputLine);
            } 
            con.disconnect();
            //System.out.println(contBuffer.toString());
            JSONObject data = new JSONObject(contBuffer.toString()).getJSONObject("data");
            JSONObject result = data.getJSONArray("result").getJSONObject(0);
            try {
                String values = result.getString("value");
                
                String[] uptime = values.replace("[","").replace("]","").split(",");
                return uptime[1].replaceAll("\"", "");
            } catch (Exception e) {
                return "0";
            }
        }
        return "0";
    }
    public String getUptimeAvailability(String site_id,String loopback) throws IOException, JSONException{
        String prom_url = prometheus_url+"/api/v1/query?query=uptime_availability_24hrs{job=\""+loopback.replace(" ", "%20")+"\",site_id=\""+site_id+"\"}";
        URL url = new URL(prom_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);

        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.flush();
        out.close();
     
        int status = con.getResponseCode();

        //System.out.println(status);

        if(status==204||status==201||status==200){
            StringBuffer contBuffer = new StringBuffer();
            String inputLine = null;
            BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((inputLine = in.readLine())!=null){
                contBuffer.append(inputLine);
            } 
            con.disconnect();
            //System.out.println(contBuffer.toString());
            JSONObject data = new JSONObject(contBuffer.toString()).getJSONObject("data");
            JSONArray array = new JSONArray(data.get("result").toString());
            JSONObject current_data = array.getJSONObject(0);
            String value = current_data.getString("value").replace("[","").replace("]","");
            try {
                String uptime = value.split(",")[1].replace("\"","");
                System.out.println("Uptime: " + uptime);
                return uptime;
            } catch (Exception e) {
                return "0";
            }
        }
        return "0";
    }

    public List<Map<String, Object>> get_historical_status_list(String site_id,String loopback,String str_end_date, Long hours_interval) throws IOException, InterruptedException, JSONException, ParseException{
        
        str_end_date = str_end_date+"T23:59:59.99";
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Manila")));
        Date end_date = isoFormat.parse(str_end_date);
        LocalDateTime end = LocalDateTime.ofInstant(end_date.toInstant(), ZoneId.of("UTC"));
        LocalDateTime start = end.minusHours(hours_interval);     
        
        //System.out.println(LocalDateTime.ofInstant(end_date.toInstant(), ZoneId.of("Asia/Manila")));
        //System.out.println(end +" UTC");
        //System.out.println(start+" UTC");

        String prom_url = prometheus_url+"/api/v1/query_range?query=lo_status{job=\""+loopback.replace(" ", "%20")+"\",site_id=\""+site_id+"\"}&start="+start+"Z&end="+end+"Z&step=300s";
        URL url = new URL(prom_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);

        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.flush();
        out.close();
     
        int status = con.getResponseCode();
        //System.out.println("PROM: "+prom_url);
        //System.out.println(status);
        List<Map<String, Object>> sites = new ArrayList<Map<String, Object>>();
        if(status==204||status==201||status==200){
            StringBuffer contBuffer = new StringBuffer();
            String inputLine = null;
    
            BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((inputLine = in.readLine())!=null){
                contBuffer.append(inputLine);
            } 
            con.disconnect();
            

            JSONObject data = new JSONObject(contBuffer.toString()).getJSONObject("data");
            JSONObject result = data.getJSONArray("result").getJSONObject(0);
            
            try {
                JSONArray array = new JSONArray(result.getString("values"));
                
                
                for(int i = 0; i < array.length(); i++){
                    Map<String, Object> obj = new HashMap<String, Object>();
                    String[] value = array.getString(i).replace("[","").replace("]","").split(",");

                    try {
                        Calendar cal = Calendar.getInstance();
                        Double d = Double.parseDouble(value[0]);
                        //System.out.println(value[0]);
                        Long l = (long) (d * 1000);
                        cal.setTimeInMillis(l);
                        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
                        df.setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")));
                        //System.out.println(cal.getTime());
                        String date = df.format(cal.getTime());
                        obj.put("date", date);
                        obj.put("status", Integer.parseInt(value[1].replace("\"", "")));
                        sites.add(obj);

                        Long x = Long.parseLong(value[0]);
                        cal.setTimeInMillis(x*1000);
                        

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                                    
                }
                return sites;    
            } catch (Exception e) {
                System.out.println(e);
                return sites;
            }
            
        }
        return sites;
    }

    public Object get_site_in_network_traffic(String site_id,String str_end_date, Integer hours, Integer interval_in_min) throws IOException, InterruptedException, JSONException, ParseException{

        str_end_date = str_end_date+"T23:59:59.99";
        SimpleDateFormat str_end_date_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        str_end_date_format.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Manila")));
        Date end_date = str_end_date_format.parse(str_end_date);
        LocalDateTime end = LocalDateTime.ofInstant(end_date.toInstant(), ZoneId.of("Asia/Manila"));
        LocalDateTime start = end.minusHours(hours);

        String prom_url = influx_url+"/query?q=SELECT non_negative_derivative(max(\"in-octets\"), 1s) * 8 FROM \"interfaces\" WHERE (\"Device\" =~ /^"+site_id+"$/) AND time >= "+ZonedDateTime.of(start,ZoneId.systemDefault()).toInstant().toEpochMilli()+"ms and time <= "+ZonedDateTime.of(end,ZoneId.systemDefault()).toInstant().toEpochMilli()+"ms GROUP BY time("+interval_in_min+"m), \"hostname\", \"Name\"&db=influxdb";
        prom_url = prom_url.replace(" ", "%20");
        URL url = new URL(prom_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);

        System.out.println(prom_url);

        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.flush();
        out.close();
     
        int status = con.getResponseCode();
        List<Map<String, Object>> sites = new ArrayList<Map<String, Object>>();
        if(status==204||status==201||status==200){
            StringBuffer contBuffer = new StringBuffer();
            String inputLine = null;
    
            BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((inputLine = in.readLine())!=null){
                contBuffer.append(inputLine);
            } 
            con.disconnect();
            
            JSONObject data = new JSONObject(contBuffer.toString());   
                          
            try {
                JSONArray array = data.getJSONArray("results").getJSONObject(0).getJSONArray("series");
                
                for(int i = 0; i < array.length(); i++){
                    JSONObject current_data = array.getJSONObject(i);
                    Map<String, Object> obj = new HashMap<String, Object>();
                    
                    obj.put("name",current_data.getJSONObject("tags").getString("Name"));
                    
                    
                    List<Map<String, Object>> traffic_data  = new ArrayList<Map<String, Object>>();
                    JSONArray val_array = current_data.getJSONArray("values");
                    for(int j = 0; j < val_array.length(); j++){
                        Map<String, Object> data_kbps = new HashMap<String, Object>();
                        String[] value = val_array.getString(j).replace("[","").replace("]","").split(",");
                        
                        
                        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date date = isoFormat.parse(value[0].replace("\"", ""));
                        LocalDateTime date_data = LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("Asia/Manila"));
                        
                        
                        Float kbps = Float.parseFloat(value[1]);
                        DecimalFormat df = new DecimalFormat("#.####");
                        data_kbps.put("time", value[0].replace("\"", ""));
                        data_kbps.put("kbps", Float.parseFloat(df.format((kbps/1000))));
                        traffic_data.add(data_kbps);
                        /*
                        System.out.println("Original datetime:" + value[0].replace("\"", ""));
                        System.out.println("GMT+8 datetime:" + date_data.plusHours(8));
                        System.out.println("Datetime now:" + LocalDateTime.now().toString());
                        */
                    }
                    obj.put("traffic",traffic_data);
                    sites.add(obj);
                }
                return sites;    
            } catch (Exception e) {
                System.out.println(e);
                Map<String, Object> obj = new HashMap<String, Object>();
                obj.put("status",HttpStatus.OK);
                obj.put("error_message", "[SNMP] "+e.getMessage());
                return obj;
            }
        }
        return sites;
    }

    public Object get_site_out_network_traffic(String site_id,String str_end_date,Integer hours, Integer interval_in_min) throws IOException, InterruptedException, JSONException, ParseException{
        str_end_date = str_end_date+"T23:59:59.99";
        SimpleDateFormat str_end_date_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        str_end_date_format.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Manila")));
        Date end_date = str_end_date_format.parse(str_end_date);
        LocalDateTime end = LocalDateTime.ofInstant(end_date.toInstant(), ZoneId.of("Asia/Manila"));
        LocalDateTime start = end.minusHours(hours);

        String prom_url = influx_url+"/query?q=SELECT non_negative_derivative(max(\"out-octets\"), 1s) * 8 FROM \"interfaces\" WHERE (\"Device\" =~ /^"+site_id+"$/) AND time >= "+ZonedDateTime.of(start,ZoneId.systemDefault()).toInstant().toEpochMilli()+"ms and time <= "+ZonedDateTime.of(end,ZoneId.systemDefault()).toInstant().toEpochMilli()+"ms GROUP BY time("+interval_in_min+"m), \"hostname\", \"Name\"&db=influxdb";
        prom_url = prom_url.replace(" ", "%20");
        URL url = new URL(prom_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);

        System.out.println(prom_url);

        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.flush();
        out.close();
     
        int status = con.getResponseCode();
        
        List<Map<String, Object>> sites = new ArrayList<Map<String, Object>>();
        if(status==204||status==201||status==200){
            StringBuffer contBuffer = new StringBuffer();
            String inputLine = null;
    
            BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((inputLine = in.readLine())!=null){
                contBuffer.append(inputLine);
            } 
            con.disconnect();
            

            JSONObject data = new JSONObject(contBuffer.toString());   
                          
            try {
                JSONArray array = data.getJSONArray("results").getJSONObject(0).getJSONArray("series");
                
                for(int i = 0; i < array.length(); i++){
                    JSONObject current_data = array.getJSONObject(i);
                    Map<String, Object> obj = new HashMap<String, Object>();
                    
                    obj.put("name",current_data.getJSONObject("tags").getString("Name"));
                    
                    
                    List<Map<String, Object>> traffic_data  = new ArrayList<Map<String, Object>>();
                    JSONArray val_array = current_data.getJSONArray("values");
                    for(int j = 0; j < val_array.length(); j++){
                        Map<String, Object> data_kbps = new HashMap<String, Object>();
                        String[] value = val_array.getString(j).replace("[","").replace("]","").split(",");
                        
                        
                        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date date = isoFormat.parse(value[0].replace("\"", ""));
                        LocalDateTime date_data = LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("Asia/Manila"));
                        

                        Float kbps = Float.parseFloat(value[1]);
                        DecimalFormat df = new DecimalFormat("#.####");
                        data_kbps.put("time", value[0].replace("\"", ""));
                        data_kbps.put("kbps", Float.parseFloat(df.format((kbps/1000))));
                        traffic_data.add(data_kbps);
                        
                        /*
                        System.out.println("Original datetime:" + value[0].replace("\"", ""));
                        System.out.println("GMT+8 datetime:" + date_data.plusHours(8));
                        System.out.println("Datetime now:" + LocalDateTime.now().toString());
                        */

                    }
                    obj.put("traffic",traffic_data);
                    System.out.println(obj.toString());
                    sites.add(obj);
                }
                return sites;    
            } catch (Exception e) {
                System.out.println(e);
                Map<String, Object> obj = new HashMap<String, Object>();
                obj.put("status",HttpStatus.OK);
                obj.put("error_message", "[SNMP] "+e.getMessage());
                return obj;
            }
        }
        return sites;
    }

    public Map<String,Object> get_circuit_status(String loopback, String site_id) throws IOException, JSONException{
        //String prom_url = prometheus_url+"/api/v1/query?query=lo_status{job=\""+loopback+"\",site_id=\""+site_id+"\"}";
        String prom_url = prometheus_url+"/api/v1/query?query=lo_status{job=\""+loopback+"\",site_id=\""+site_id+"\"}";
        //System.out.println(prom_url);
        URL url = new URL(prom_url.replace(" ", "%20"));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);

        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.flush();
        out.close();
        
        int status = con.getResponseCode();
        //System.out.println(status);
        Map<String, Object> object = new HashMap<String, Object>();
        if(status==204||status==201||status==200){
            try {
                StringBuffer contBuffer = new StringBuffer();
                String inputLine = null;
        
                BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
                while((inputLine = in.readLine())!=null){
                    contBuffer.append(inputLine);
                } 
                con.disconnect();
                //System.out.println(contBuffer.toString());
               
                JSONObject data = new JSONObject(contBuffer.toString()).getJSONObject("data");
                JSONArray array = new JSONArray(data.get("result").toString());
                JSONObject current_data = array.getJSONObject(0);
                JSONObject metric = current_data.getJSONObject("metric");
                String value = current_data.getString("value").replace("[","").replace("]","");
                //System.out.println(value);
                String ip = metric.getString("instance");
                String site_status = metric.getString("site_status");
                Integer stat = Integer.parseInt(value.split(",")[1].replace("\"",""));
                
                object.put("ip",ip);
                object.put("site_status",site_status);
                object.put("stat",stat);
                return object;
            } catch (Exception e) {
                //System.out.println("ERROR "+ site_id+": "+e);
                object.put("ip","");
                object.put("stat",0);
                return object;
            }
        }
        object.put("ip","");
        object.put("stat",0);
        return object;
    }
    
}
