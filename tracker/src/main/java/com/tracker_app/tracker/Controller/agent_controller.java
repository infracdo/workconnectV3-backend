package com.tracker_app.tracker.Controller;



import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.tracker_app.tracker.DataSource_Tracker.Entity.tracker_agent;
import com.tracker_app.tracker.DataSource_Tracker.Repo.auth_user_repo;
import com.tracker_app.tracker.DataSource_Tracker.Repo.tracker_agent_repo;
import com.tracker_app.tracker.DataSource_Tracker.Repo.tracker_location_repo;
import com.tracker_app.tracker.DataSource_WorkConnect.Entity.agent_latest_location;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.agent_latest_location_repo;
import com.tracker_app.tracker.Helper.GPXHelper;
import com.tracker_app.tracker.Helper.rt_helper_rest1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/agent")
public class agent_controller {
    
    @Autowired
    private tracker_agent_repo tracker_repo;
    @Autowired
    private tracker_location_repo tracker_loc_repo;
    @Autowired
    private GPXHelper helpergpx;
    @Autowired
    private agent_latest_location_repo agent_latest_loc_repo;
    @Autowired
    private auth_user_repo user_auth_repo;
    @Autowired
    rt_helper_rest1 rt = new rt_helper_rest1();

    @Value("${RT.URL}")
    String rt_url;

    @Value("${download_path}")
    private String UploadPath;
    @Value("${download_path_tem}")
    private String UploadPath_temp;

    @GetMapping(path = "/sync_location")
    @Transactional("TrackerTransactionManager")
    public ResponseEntity<Object> sync_user_location( ) throws IOException, ParseException, InterruptedException
    {
        List<Map<String, Object>> agents = tracker_repo.get_agent_name();
        for (Map<String,Object> map : agents) {
            new Thread(()->{
                sync_gpx(map);
            }).start();
            Thread.sleep(200);
        }
        return new ResponseEntity<Object>("Added agents",HttpStatus.OK);
    }
    
    @GetMapping(path = "/list")
    @Transactional("TrackerTransactionManager")
    public ResponseEntity<Object> get_agent_list( )
    {
        List<Map<String, Object>> agents = tracker_repo.get_agent_name();
        return new ResponseEntity<Object>(agents,HttpStatus.OK);
    }

    @GetMapping(path = "/route")
    @Transactional("TrackerTransactionManager")
    public ResponseEntity<Object> get_agent_route(
        @RequestParam(value="first_name") String first_name,
        @RequestParam(value="last_name") String last_name,
        @RequestParam(value="date") String date
    ) throws IOException
    {
        List<Map<String, Object>> empty =new ArrayList<Map<String, Object>>();
        LocalDate date1 = null;
        try {
            date1 = LocalDate.parse(date);
        } catch (Exception e) {
            return new ResponseEntity<Object>("Not a valid date",HttpStatus.CONFLICT);
        }

        String first_namez = first_name.replaceAll("\\W", "").replace(" ", "_"); 
        String last_namez = last_name.replaceAll("\\W", "").replace(" ", "_");
        String agent = first_namez+"_"+last_namez;
        String ParentDir = UploadPath;        
        String fileName = "/"+agent + "/" +date1.toString()+ ".gpx";
        String filepath = new java.io.File(ParentDir+fileName).getAbsolutePath();
        System.out.println(filepath);
        if(! new java.io.File(filepath).exists()){
            return new ResponseEntity<Object>(empty,HttpStatus.OK);
        }
        
        return new ResponseEntity<Object>(helpergpx.get_agent_location(filepath),HttpStatus.OK);
    }

    
    @GetMapping(path = "/latest_location")
    @Transactional("TrackerTransactionManager")
    public ResponseEntity<Object> get_latest_location()
    {
        List<agent_latest_location> agentz = agent_latest_loc_repo.get_agent_location();
        return new ResponseEntity<Object>(agentz,HttpStatus.OK);
    }

    
    @GetMapping(path = "/latest_ticket")
    @Transactional("TrackerTransactionManager")
    public ResponseEntity<Object> get_latest_ticket(
        @RequestParam(value="first_name") String first_name,
        @RequestParam(value="last_name") String last_name,
        @RequestParam(value="date") String date
    ) throws IOException, ParseException
    {
        date = date.replace("-", "/");
        Date converted_date = new SimpleDateFormat("yyyy/MM/dd").parse(date);
        String[] datestr = converted_date.toString().split(" ");
        date = datestr[1]+"%20"+datestr[2]+"%20"+datestr[5];    
           
        String email = user_auth_repo.get_agent_email(first_name, last_name);
        Object tickets = rt.get_agent_tickets(rt_url, email, date);
        return new ResponseEntity<Object>(tickets,HttpStatus.OK);
    }
    /*
    @Scheduled(fixedRate = (5*60000))
    @Async("asyncExecutor")
    public void update_latest_agent_loc() throws ParseException, InterruptedException{
        System.out.println("----------------START UPDATING AGENT LOCATION----------------"+LocalDateTime.now());
        long startTime = System.currentTimeMillis();
        try {
            List<Map<String, Object>> agents = tracker_repo.get_agent_name();
            for (Map<String,Object> map : agents) {
                List<Map<String,Object>> agent_loc = tracker_loc_repo.get_agent_latest_location(map.get("first_name").toString(), map.get("last_name").toString());
                new Thread(()->{
                    for (Map<String,Object> map2 : agent_loc) {
                        if(map2.size()>0){
                                try {
                                    agent_latest_location agentz = agent_latest_loc_repo.get_agent(map.get("first_name").toString()+" "+map.get("last_name").toString());
                                    SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                    Date loc_datetime = in.parse(map2.get("date").toString());
                                    
                                    if(agentz!=null){
                                        agentz.setTime(loc_datetime.toString());
                                        agentz.setName(map.get("first_name").toString()+" "+map.get("last_name").toString());
                                        agentz.setLatitude(Double.parseDouble(map2.get("latitude").toString()));
                                        agentz.setLongitude(Double.parseDouble(map2.get("longitude").toString()));
                                    }else{
                                        agentz = new agent_latest_location();
                                        agentz.setTime(loc_datetime.toString());
                                        agentz.setName(map.get("first_name").toString()+" "+map.get("last_name").toString());
                                        agentz.setLatitude(Double.parseDouble(map2.get("latitude").toString()));
                                        agentz.setLongitude(Double.parseDouble(map2.get("longitude").toString()));
                                    }
                                    agent_latest_loc_repo.save(agentz);   
                                    
                                } catch (Exception e) {
                                    //TODO: handle exception
                                }   
                            break;
                        }
                        else System.out.println(map.get("first_name").toString()+" "+map.get("last_name").toString());
                    }
                }).start();
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        
        long endtime = System.currentTimeMillis();
        System.out.println("----------------DONE UPDATING AGENT LOCATION----------------Duration: "+(endtime-startTime)+"ms----------------");
    }
    */
    public void sync_gpx(Map<String,Object> map){
        System.out.println(Thread.currentThread().getName());
        String first_name = map.get("first_name").toString().replaceAll("\\W", "").replace(" ", "_"); 
        String last_name = map.get("last_name").toString().replaceAll("\\W", "").replace(" ", "_");
            
        List<Map<String, Object>> locations = tracker_loc_repo.get_agent_location(map.get("first_name").toString(), map.get("last_name").toString()); 

        if(!(locations == null||locations.isEmpty())){
            String agent = first_name+"_"+last_name;
            String ParentDir = UploadPath;  
            if(! new java.io.File(ParentDir).exists()){
                new java.io.File(ParentDir).mkdir();    
            }
            for (Map<String,Object> loc : locations) {
                String loc_date = loc.get("date").toString().split(" ")[0];
                
                String fileName = "/"+agent + "/" +loc_date+ ".gpx";
                String folderpath = new java.io.File(ParentDir+"/"+agent).getAbsolutePath();
                String filepath = new java.io.File(ParentDir+fileName).getAbsolutePath();
                
                if(! new java.io.File(folderpath).exists()){
                    new java.io.File(folderpath).mkdir();    
                }

                double latitude = Double.parseDouble(loc.get("latitude").toString());
                double longitude = Double.parseDouble(loc.get("longitude").toString());
                String datetime = loc.get("date").toString();
                
                
                try {
                    helpergpx.addPoints(latitude, longitude, filepath, datetime);
                } catch (IOException e) {
                    System.out.println(agent);
                } catch (ParseException e) {
                    System.out.println(loc.get("date").toString());
                } 
                                         
            }
            System.out.println(agent+" DONE SYNCING");
        }
        
    }
}
