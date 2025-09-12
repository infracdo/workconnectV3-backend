package com.tracker_app.tracker.Helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class rt_helper_rest1 {

    public String rt_auth(String rt_url, String payload){
        try {
            JSONObject json_payload = new JSONObject(payload);
            String rt_pass = json_payload.getString("rt_pass");
            String rt_user = json_payload.getString("rt_user");

            String rtauth_url = rt_url+"/REST/1.0/search/queue?query=&user="+rt_user+"&pass="+rt_pass;
            URL url = new URL(rtauth_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setUseCaches(false);
            con.setAllowUserInteraction(false);
         
            int status = con.getResponseCode();
            System.out.println(status);
            if (status == 200){
                StringBuffer contBuffer = new StringBuffer();
                String inputLine = null;
        
                BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
                while((inputLine = in.readLine())!=null){
                    contBuffer.append(inputLine);
                    contBuffer.append("\r\n");
                } 
                con.disconnect();
                String[] c = contBuffer.toString().split("\\r?\\n");
                for (int i = 0; i < c.length; i++){
                    System.out.println(i+":"+c[i]);
                }
                
                if(c[0].contains("200 Ok") ){

                    String[] cookie = con.getHeaderField("Set-Cookie").split(";");
                    System.out.println(con.getHeaderField("Set-Cookie"));
                    return cookie[0];
                
                }else{
                    return c[0];
                }
            } else{
                return "Error";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Format Error";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "I/0 Error";
        }
    }

    public String rt_get_queue_all(String rt_url, String payload, String rt_user){
        try {
            JSONObject json_payload = new JSONObject(payload);
            String cookie = json_payload.getString("cookie");

            String rtauth_url = rt_url+"/REST/1.0/search/queue?query=";
            URL url = new URL(rtauth_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Cookie", cookie);
            con.setUseCaches(false);
            con.setAllowUserInteraction(false);
         
            int status = con.getResponseCode();
            if (status == 200){
                StringBuffer contBuffer = new StringBuffer();
                String inputLine = null;
        
                BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
                while((inputLine = in.readLine())!=null){
                    contBuffer.append(inputLine);
                    contBuffer.append("\r\n");
                } 
                con.disconnect();
                String[] c = contBuffer.toString().split("\\r?\\n");
                System.out.println(c.length);
                if(c[0].contains("200 Ok") ){
                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    for (int i = 2; i < (c.length-2); i++){
                        String name = c[i].split(":")[1];
                        System.out.println(name);
                        //String data = rt_get_tickets_all(rt_url, name, cookie);
                        String data = rt_get_tickets_all(rt_url, cookie, rt_user);
                        String que_json = "{\"name\":\""+name+"\",\"data\":\""+data+"\"}";
                        if(que_json.contains("401 Credentials required")){
                            return "401 Credentials required";
                        }else if(data.contains("Error")){
                            que_json = "{\"name\":\""+name+"\",\"data\":[]}";
                        }else if(data.contains("No matching results.")){
                            que_json = "{\"name\":\""+name+"\",\"data\":[]}";
                        }else{
                            sb.append(que_json);
                            if(i < c.length-3){
                                sb.append(",");
                            }
                        }
                    }
                    sb.append("]");
                    return sb.toString();                
                }else{
                    return c[0];
                }

            } else{
                return "Error";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Format Error";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "I/0 Error";
        }
    }

    public String rt_get_tickets_all(String rt_url, String payload, String rt_user){        
        try {
            JSONObject json_payload = new JSONObject(payload);
            String cookie = json_payload.getString("cookie");
            String queue_name = json_payload.getString("queue_name");
            String rtauth_url = rt_url+"/REST/1.0/search/ticket?query=(Status='open'ORStatus='new')ANDOwner='"+rt_user+"'ANDQueue='"+queue_name.strip().replace(" ", "%20")+"'\'&fields=Created,Subject,Requestors,Status,CF.{Site%20ID},CF.{Ticket_Check_In},CF.{Ticket_Check_out}";
            //String rtauth_url = rt_url+"/REST/1.0/search/ticket?query=Queue='TEST%20Tracker'";
            System.out.println(rtauth_url);
            URL url = new URL(rtauth_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Cookie", cookie);
            con.setUseCaches(false);
            con.setAllowUserInteraction(false);
         
            int status = con.getResponseCode();
            if (status == 200){
                StringBuffer contBuffer = new StringBuffer();
                String inputLine = null;
        
                BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
                while((inputLine = in.readLine())!=null){
                    contBuffer.append(inputLine);
                    contBuffer.append("\r\n");
                } 
                con.disconnect();
                
                String[] c = contBuffer.toString().split("\\r?\\n");
                for(int i = 0; i < c.length;i++){
                    System.out.println(c[i]);
                }
                String[] d = contBuffer.toString().split("--");
                
                System.out.println(c[0].contains("200"));
                if(c[0].contains("200")){
                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    for(int i = 0; i < d.length; i++){
                        String[] e = d[i].split("\\r?\\n");
                        
                        if(i > 0) sb.append(",{");
                        else sb.append("{");
                        for(int j = 2; j < e.length; j++){
                            String[] current_ticketdata = e[j].split(":");
                            String id = e[j].split(":")[0];
                            StringBuilder data = new StringBuilder();
                            for(int x = 1;x<current_ticketdata.length;x++){
                                if(x>1){
                                    data.append(":");
                                    data.append(current_ticketdata[x]);
                                }else{
                                    data.append(current_ticketdata[x]);
                                }
                            }
                            System.out.println(id);
                            if(id.contains("id")) sb.append("\"id\":\""+data.toString().split("/")[1]+"\"");
                            else if(id.contains("Subject")) sb.append(",\"Subject\":\""+data.toString()+"\"");
                            else if(id.contains("Requestors")) sb.append(",\"Requestors\":\""+data.toString()+"\"");
                            else if(id.contains("Created")) sb.append(",\"Created\":\""+data.toString()+"\"");
                            else if(id.contains("Status")) sb.append(",\"Status\":\""+data.toString()+"\"");
                            else if(id.contains("CF.{Site ID}")) sb.append(",\"Site ID\":\""+data.toString()+"\"");
                            else if(id.contains("CF.{Ticket_Check_In}")) sb.append(",\"Check In\":\""+data.toString()+"\"");
                            else if(id.contains("CF.{Ticket_Check_out}")) sb.append(",\"Check Out\":\""+data.toString()+"\"");
                            else{
                                continue;
                            }
                        }
                        sb.append("}");
                        System.out.println(sb.toString());
                    }
                    sb.append("]");
                    return sb.toString();                
                }else{
                    if(c[2].contains("No matching results.")){
                        return c[2];
                    }else{
                        return c[0];
                    }
                }

            } else{
                return "Error";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Format Error";
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "I/0 Error";
        }
    }

    public String rt_get_tickets(String rt_url, String payload){
        try {
            JSONObject json_payload = new JSONObject(payload);
            String ticket_id = json_payload.getString("id");
            String cookie = json_payload.getString("cookie");

            String rtauth_url = rt_url+"/REST/1.0/ticket/"+ticket_id+"/show";
            URL url = new URL(rtauth_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Cookie", cookie);
            con.setUseCaches(false);
            con.setAllowUserInteraction(false);
         
            int status = con.getResponseCode();
            if (status == 200){
                StringBuffer contBuffer = new StringBuffer();
                String inputLine = null;
        
                BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
                while((inputLine = in.readLine())!=null){
                    contBuffer.append(inputLine);
                    contBuffer.append("\r\n");
                } 
                con.disconnect();
                String[] c = contBuffer.toString().split("\\r?\\n");
            
                if(c[0].contains("200 Ok") ){
                    StringBuilder sb = new StringBuilder();
                    sb.append("{");
                    for (int i = 2; i < (c.length-2); i++){
                        String[] current_ticketdata = c[i].split(":");
                        StringBuilder data = new StringBuilder();
                        for(int x = 1;x<current_ticketdata.length;x++){
                            if(x>1){
                                data.append(":");
                                data.append(current_ticketdata[x]);
                            }else{
                                data.append(current_ticketdata[x]);
                            }
                        }
                        if(current_ticketdata[0].contains("id")) sb.append( "\"id\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("Queue")) sb.append( ",\"Queue\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("Owner")) sb.append( ",\"Owner\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("Creator")) sb.append( ",\"Creator\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("Subject")) sb.append( ",\"Subject\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("Status")) sb.append( ",\"Status\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("Priority")) sb.append( ",\"Priority\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("InitialPriority")) sb.append( ",\"InitialPriority\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("FinalPriority")) sb.append( ",\"FinalPriority\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("Requestors")) sb.append( ",\"Requestors\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("Created")) sb.append( ",\"Created\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("Starts")) sb.append( ",\"Starts\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("Started")) sb.append( ",\"Started\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("Due")) sb.append( ",\"Due\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("Resolved")) sb.append( ",\"Resolved\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("Told")) sb.append( ",\"Told\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("TimeEstimated")) sb.append( ",\"TimeEstimated\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("TimeWorked")) sb.append( ",\"TimeWorked\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("TimeLeft")) sb.append( ",\"TimeLeft\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("Site ID")) sb.append( ",\"Site ID\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("Ticket_Check_In")) sb.append( ",\"Check In\":\""+data.toString()+"\"");
                        else if(current_ticketdata[0].contains("Ticket_Check_out")) sb.append( ",\"Check Out\":\""+data.toString()+"\"");
                        else{
                            continue;
                        }
                        /*
                        if(i < c.length-3){
                            sb.append(",");
                        }
                        */
                    }
                    sb.append("}");
                    return sb.toString();                
                }else{
                    return c[0];
                }

            } else{
                return "Error";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Format Error";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "I/0 Error";
        }
    }    


    public String rt_change_tickets_status(String rt_url, String payload){
        try {
            JSONObject json_payload = new JSONObject(payload);
            String ticket_id = json_payload.getString("id");
            String cookie = json_payload.getString("cookie");
            String ticket_status = json_payload.getString("status");
            System.out.println(ticket_id+":"+ticket_status);
            
            String rtauth_url = rt_url+"/REST/1.0/ticket/"+ticket_id+"/edit?content=Status:%20"+ticket_status;
            URL url = new URL(rtauth_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Cookie", cookie);
            con.setUseCaches(false);
            con.setAllowUserInteraction(false);
            
            int status = con.getResponseCode();
            StringBuffer contBuffer = new StringBuffer();
            String inputLine = null;
    
            BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((inputLine = in.readLine())!=null){
                contBuffer.append(inputLine);
                contBuffer.append("\r\n");
            } 
            System.out.println(contBuffer.toString());

            if (status == 200){
                
                con.disconnect();
                
                String[] c = contBuffer.toString().split("\\r?\\n");
            
                if(c[0].contains("200 Ok") ){
                    return "Ok";              
                }else{
                    return c[0];
                }

            } else{
                return "Error";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Format Error";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "I/0 Error";
        }
    }    

    public String rt_tickets_CheckInOut(String rt_url, String payload){
        try {
            JSONObject json_payload = new JSONObject(payload);
            String ticket_id = json_payload.getString("id");
            String cookie = json_payload.getString("cookie");
            String longitude = json_payload.getString("lon");
            String latitude = json_payload.getString("lat");
            String datetime = json_payload.getString("time");
            String log_type = json_payload.getString("log_type");
            
            String value = datetime+"/"+latitude+","+longitude;
            
            String rtauth_url = rt_url+"/REST/1.0/ticket/"+ticket_id+"/edit?content=CF.{"+log_type+"}:%20"+value;
            URL url = new URL(rtauth_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Cookie", cookie);
            con.setUseCaches(false);
            con.setAllowUserInteraction(false);
            
            int status = con.getResponseCode();
            StringBuffer contBuffer = new StringBuffer();
            String inputLine = null;
    
            BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((inputLine = in.readLine())!=null){
                contBuffer.append(inputLine);
                contBuffer.append("\r\n");
            } 
            System.out.println(contBuffer.toString());

            if (status == 200){
                
                con.disconnect();
                
                String[] c = contBuffer.toString().split("\\r?\\n");
            
                if(c[0].contains("200 Ok") ){
                    return "Ok";              
                }else{
                    return c[0];
                }

            } else{
                return "Error";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Format Error";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "I/0 Error";
        }
    }  

    public String rt_get_1stComment(String rt_url, String payload){
        try {
            JSONObject json_payload = new JSONObject(payload);
            String ticket_id = json_payload.getString("id");
            String cookie = json_payload.getString("cookie");

            String rtauth_url = rt_url+"/REST/1.0/ticket/"+ticket_id+"/history?format=l";
            URL url = new URL(rtauth_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Cookie", cookie);
            con.setUseCaches(false);
            con.setAllowUserInteraction(false);
         
            int status = con.getResponseCode();
            if (status == 200){
                StringBuffer contBuffer = new StringBuffer();
                String inputLine = null;
        
                BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
                while((inputLine = in.readLine())!=null){
                    contBuffer.append(inputLine);
                    contBuffer.append("\r\n");
                } 
                con.disconnect();
                String[] c = contBuffer.toString().split("\\r?\\n");
            
                if(c[0].contains("200 Ok") ){
                    String[] history = contBuffer.toString().split("#");
                    String[] historydata = history[1].split("\\r?\\n");
                    
                    System.out.println(historydata.length);

                    StringBuilder sb = new StringBuilder();
                    sb.append("{");
                    for (int i = 2; i < (historydata.length-2); i++){
                        String[] current_ticketdata = historydata[i].split(":");
                        StringBuilder data = new StringBuilder();
                        for(int x = 1;x<current_ticketdata.length;x++){
                            if(x>1){
                                data.append(":");
                                data.append(current_ticketdata[x]);
                            }else{
                                data.append(current_ticketdata[x]);
                            }
                        }
                        System.out.println(data.toString());
                        
                        if(current_ticketdata[0].contains("Content")){
                            StringBuilder sb1 = new StringBuilder();
                            sb1.append(data.toString());
                            while(true){
                                i++;
                                current_ticketdata = historydata[i].split(":");
                                System.out.println(current_ticketdata[0]);
                                if(historydata[i].split(":")[0].contains("Creator")){
                                    i = i-1;
                                    break;
                                }
                                else{
                                    sb1.append(historydata[i]);
                                }
                            }
                            sb.append( "\"Content\":\""+sb1.toString()+"\"");
                        }
                        
                        
                        else{
                            continue;
                        }                        
                    }
                    sb.append("}");
                    return sb.toString();                
                }else{
                    return c[0];
                }

            } else{
                return "Error";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Format Error";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "I/0 Error";
        }
    }    


    public String rt_comment_ticket(String rt_url, String payload){
        try {
            JSONObject json_payload = new JSONObject(payload);
            String ticket_id = json_payload.getString("id");
            String cookie = json_payload.getString("cookie");
            String comment = json_payload.getString("comment");
            comment = comment.replaceAll(" ","%20");
            comment = comment.replaceAll("\n","%20%0A%20");

            
            String rtauth_url = rt_url+"/REST/1.0/ticket/"+ticket_id+"/comment?content=Action:%20comment%0AText:%20"+comment;
            URL url = new URL(rtauth_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Cookie", cookie);
            con.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.flush();
            out.close();
            
            int status = con.getResponseCode();
            StringBuffer contBuffer = new StringBuffer();
            String inputLine = null;
    
            BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((inputLine = in.readLine())!=null){
                contBuffer.append(inputLine);
                contBuffer.append("\r\n");
            } 
            System.out.println(contBuffer.toString());

            if (status == 200){
                
                con.disconnect();
                
                String[] c = contBuffer.toString().split("\\r?\\n");
            
                if(c[0].contains("200 Ok") ){
                    return "Ok";              
                }else{
                    return c[0];
                }

            } else{
                return "Error";
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Format Error";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "I/0 Error";
        }
    }    

    public Object get_site_tickets(String rt_url,String site_id) throws IOException{
        String rtauth_url = rt_url+"/REST/1.0/search/ticket?query='CF.{Site%20ID}'='"+site_id+"'&fields=id,Subject,Creator,Queue,Owner,Created,LastUpdated,Status,CF.{Site%20ID}&user=rtdash&pass=rt_collector1&format=l";
        URL url = new URL(rtauth_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setUseCaches(false);
        con.setAllowUserInteraction(false);
        
        int status = con.getResponseCode();
        if (status == 200){
            StringBuffer contBuffer = new StringBuffer();
            String inputLine = null;
    
            BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((inputLine = in.readLine())!=null){
                contBuffer.append(inputLine);
                contBuffer.append("\r\n");
            } 
            con.disconnect();
            
            String[] c = contBuffer.toString().split("\\r?\\n");
            if(c[0].contains("200 Ok")){
                String[] d = contBuffer.toString().replace("RT/5.0.0 200 Ok","").split("--");
                List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
                if(c[2].contains("No matching results.")){
                    return data;
                }
                for (String ticket : d) {
                    if(!(ticket.isBlank()||ticket.isEmpty())){
                        String[] fields = ticket.split("\\r?\\n");
                        Map<String, Object> obj = new HashMap<String, Object>();
                        for (String field : fields) {
                            String[] x = field.split(":");
                            StringBuilder datax = new StringBuilder();
                            for(int y = 1;y<x.length;y++){
                                if(y>1){
                                    datax.append(":");
                                    datax.append(x[y]);
                                }else{
                                    datax.append(x[y]);
                                }
                            }
                            //&fields=id,Subject,Creator,Queue,Owner,Created,LastUpdated,Status,CF.{Site%20ID}
                            if(
                                x[0].contains("id")||x[0].contains("Subject")||x[0].contains("Creator")||x[0].contains("Queue")||x[0].contains("Owner")
                                ||x[0].contains("Created")||x[0].contains("LastUpdated")||x[0].contains("Status")
                            ){
                                obj.put(x[0], datax.toString());
                            } 
                            if(x[0].contains("Site ID")){
                                obj.put("Site ID", datax.toString());
                            }

                        }
                        obj.put("link", rt_url+"/Ticket/Display.html?id="+obj.get("id").toString().split("/")[1]);
                        data.add(obj);
                    }
                }
                return data;
            }
        }
        else{
            return "error";
        }
        return "error";
    }

    public List<Map<String, Object>> get_site_ticket_url(String rt_url,String custom_field,String queue_name) throws IOException{
        //Telco%20Escalation%20|%20Site%20ID
        String rtauth_url = rt_url+"/REST/1.0/search/ticket?query=(Status='open'ORStatus='new')ANDQueue='"+queue_name.strip().replace(" ", "%20")+"'&orderby=-Created&fields=id,CF.{"+custom_field.replace(" ", "%20")+"}&user=rtdash&pass=rt_collector1&format=l";
        //System.out.println("URL: "+rtauth_url);
        URL url = new URL(rtauth_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setUseCaches(false);
        con.setAllowUserInteraction(false);
         
        int status = con.getResponseCode();
        
        if (status == 200){
            StringBuffer contBuffer = new StringBuffer();
            String inputLine = null;
    
            BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((inputLine = in.readLine())!=null){
                contBuffer.append(inputLine);
                contBuffer.append("\r\n");
            } 
            con.disconnect();
            //System.out.println("DATA FOR RT URL: " + contBuffer.toString()); 
            String[] c = contBuffer.toString().split("\\r?\\n");
            if(c[0].contains("200 Ok")){
                String[] d = contBuffer.toString().replace("RT/5.0.0 200 Ok","").split("--");
                List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
                if(c[2].contains("No matching results.")){
                    return data;
                }
                for (String ticket : d) {
                    if(!(ticket.isBlank()||ticket.isEmpty())){
                        String[] fields = ticket.split("\\r?\\n");
                        Map<String, Object> obj = new HashMap<String, Object>();
                        for (String field : fields) {
                            String[] x = field.split(":");
                            StringBuilder datax = new StringBuilder();
                            for(int y = 1;y<x.length;y++){
                                if(y>1){
                                    datax.append(":");
                                    datax.append(x[y]);
                                }else{
                                    datax.append(x[y]);
                                }
                            }
                            //&fields=id,Subject,Creator,Queue,Owner,Created,LastUpdated,Status,CF.{Site%20ID}
                            if(
                                x[0].contains("id")||x[0].contains("Subject")||x[0].contains("Creator")||x[0].contains("Queue")||x[0].contains("Owner")
                                ||x[0].contains("Created")||x[0].contains("LastUpdated")||x[0].contains("Status")
                            ){
                                obj.put(x[0], datax.toString());
                            } 
                            if(x[0].contains(custom_field)){
                                obj.put("Site ID", datax.toString());
                            }
                        }
                        obj.put("link", rt_url+"/Ticket/Display.html?id="+obj.get("id").toString().split("/")[1]);
                        data.add(obj);
                    }
                }
                return data;
            }
        }
        else{
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
            return data;
        }
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        return data;
    }

    public Object get_agent_tickets(String rt_url,String agent, String date) throws IOException{
        String rtauth_url = rt_url+"/REST/1.0/search/ticket?query='Owner'='"+agent+"'AND'Created'='"+date+"'&fields=id,Subject,Creator,Queue,Owner,Created,LastUpdated,Status,CF.{Site%20ID}&user=rtdash&pass=rt_collector1&format=l";
        URL url = new URL(rtauth_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setUseCaches(false);
        con.setAllowUserInteraction(false);
         
        int status = con.getResponseCode();
        if (status == 200){
            StringBuffer contBuffer = new StringBuffer();
            String inputLine = null;
    
            BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
            while((inputLine = in.readLine())!=null){
                contBuffer.append(inputLine);
                contBuffer.append("\r\n");
            } 
            con.disconnect();
            
            String[] c = contBuffer.toString().split("\\r?\\n");
            if(c[0].contains("200 Ok")){
                String[] d = contBuffer.toString().replace("RT/5.0.0 200 Ok","").split("--");
                List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
                if(c[2].contains("No matching results.")){
                    return data;
                }
                for (String ticket : d) {
                    if(!(ticket.isBlank()||ticket.isEmpty())){
                        String[] fields = ticket.split("\\r?\\n");
                        Map<String, Object> obj = new HashMap<String, Object>();
                        for (String field : fields) {
                            String[] x = field.split(":");
                            StringBuilder datax = new StringBuilder();
                            for(int y = 1;y<x.length;y++){
                                if(y>1){
                                    datax.append(":");
                                    datax.append(x[y]);
                                }else{
                                    datax.append(x[y]);
                                }
                            }
                            //&fields=id,Subject,Creator,Queue,Owner,Created,LastUpdated,Status,CF.{Site%20ID}
                            if(
                                x[0].contains("id")||x[0].contains("Subject")||x[0].contains("Creator")||x[0].contains("Queue")||x[0].contains("Owner")
                                ||x[0].contains("Created")||x[0].contains("LastUpdated")||x[0].contains("Status")
                            ){
                                obj.put(x[0], datax.toString().trim());
                            } 
                            if(x[0].contains("Site ID")){
                                obj.put("Site ID", datax.toString());
                            }

                        }
                        obj.put("link", rt_url+"/Ticket/Display.html?id="+obj.get("id").toString().split("/")[1]);
                        data.add(obj);
                    }
                }
                return data;
            }
        }
        else{
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
            return data;
        }
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        return data;
    }
}
