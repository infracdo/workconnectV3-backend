package com.tracker_app.tracker.Helper;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.Segment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.springframework.stereotype.Service;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;
import io.jenetics.jpx.GPX.Builder;

@Service
public class GPXHelper {

    public String getXML(String xml_payload) throws IOException {
        InputStream inputgpx = new ByteArrayInputStream(xml_payload.getBytes());
        GPX gpx = GPX.read(inputgpx);
        List<WayPoint> points = gpx.getWayPoints(); 
        StringBuilder sb = new StringBuilder();
        for (WayPoint wayPoint : points) {
            sb.append(wayPoint.getTime().get()+",");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    public String addPoints(double latitude, double longitude, String filepath, String datetime) throws IOException, ParseException{
        
        Path path = Path.of(filepath);
        long time = 0;
        try {
            time = Instant.parse(datetime).toEpochMilli();    
        } catch (Exception e) {
       
            SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date loc_datetime = in.parse(datetime);
            time = loc_datetime.toInstant().toEpochMilli();
            //TODO: handle exception
        }
        

        WayPoint point = WayPoint.of(latitude, longitude, time);
        
        if(! new java.io.File(filepath).exists()){
            new java.io.File(filepath).createNewFile();
            GPX newgpx = GPX.builder().addWayPoint(point).build();
            GPX.write(newgpx, path.toAbsolutePath());

            StringBuilder sb = new StringBuilder();
            List<WayPoint> points = newgpx.getWayPoints();
            for (WayPoint wayPoint : points) {
                sb.append(wayPoint.toString()+",");
            }
            
            return sb.toString();
        }else{
            GPX gpx = GPX.read(filepath);
            GPX newgpx = gpx.toBuilder().addWayPoint(point).build();
            
            gpx.write(newgpx, path.toAbsolutePath());

            StringBuilder sb = new StringBuilder();
            List<WayPoint> points = newgpx.getWayPoints();
            for (WayPoint wayPoint : points) {
                sb.append(wayPoint.toString()+",");
            }
            
            return sb.toString();
        }
            
    }

    public String getCurrentLocation(String path_to_file, String name) throws IOException{
        GPX gpx = GPX.read(path_to_file);
        System.out.println("Creator: " + gpx.getCreator());
        System.out.println("Version: " + gpx.getCreator());
        List<WayPoint> points = gpx.getWayPoints();
        StringBuilder sb = new StringBuilder();
        WayPoint latest_point = points.get(points.size()-1);
        sb.append("{");
        sb.append("\"long\":\""+latest_point.getLongitude()+"\",");
        sb.append("\"lat\":\""+latest_point.getLatitude()+"\",");
        sb.append("\"time\":\""+latest_point.getTime()+"\",");
        sb.append("\"name\":\""+name+"\"");
        sb.append("}");
        return sb.toString();
    }

    public String getUserLocations(String path_to_file, String name) throws IOException{
        GPX gpx = GPX.read(path_to_file);
        List<WayPoint> points = gpx.getWayPoints();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = 0; i < points.size(); i++){
            WayPoint current_point = points.get(i);
            if(i>0){
                sb.append(",{");
                sb.append("\"long\":\""+current_point.getLongitude()+"\",");
                sb.append("\"lat\":\""+current_point.getLatitude()+"\",");
                sb.append("\"time\":\""+current_point.getTime()+"\",");
                sb.append("\"name\":\""+name+"\"");
                sb.append("}");        
            }else{
                sb.append("{");
                sb.append("\"long\":\""+current_point.getLongitude()+"\",");
                sb.append("\"lat\":\""+current_point.getLatitude()+"\",");
                sb.append("\"time\":\""+current_point.getTime()+"\",");
                sb.append("\"name\":\""+name+"\"");
                sb.append("}");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public Object get_agent_location(String path_to_file) throws IOException{
        GPX gpx = GPX.read(path_to_file);
        List<WayPoint> points = gpx.getWayPoints();
        List<Map<String, Object>> agent_locs =new ArrayList<Map<String, Object>>();
        for(int i = 0; i < points.size(); i++){
            WayPoint current_point = points.get(i);
            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put("latitude", current_point.getLatitude());
            obj.put("longitude", current_point.getLongitude());
            obj.put("time", current_point.getTime().get().toLocalDateTime());
            agent_locs.add(obj);
        }
        return agent_locs;
    }
    

}
