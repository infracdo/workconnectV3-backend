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
public class rt_helper_rest2 {

    public String rt_auth(String rt_url, String payload){
        try {
            JSONObject json_payload = new JSONObject(payload);
            String rt_pass = json_payload.getString("rt_pass");
            String rt_user = json_payload.getString("rt_user");

            String rtauth_url = rt_url+"/REST/1.0/search/queue?query=&user="+rt_user+"&pass="+rt_pass;
            URL url = new URL(rtauth_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.flush();
            out.close();
         
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

    public String rt_get_queue_all(String rt_url, String payload){
        try {
            JSONObject json_payload = new JSONObject(payload);
            String cookie = json_payload.getString("cookie");

            String rtauth_url = rt_url+"/REST/2.0/queues/all?fields=Name,Description";
            URL url = new URL(rtauth_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Cookie", cookie);
            con.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.flush();
            out.close();
         
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
                return contBuffer.toString();
            }else{
                return "Error";
            }
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Format Error";
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "I/0 Error";
        }
    }

    public String rt_get_tickets(String rt_url, String payload){
        try {
            JSONObject json_payload = new JSONObject(payload);
            String cookie = json_payload.getString("cookie");

            String[] Status_array = json_payload.getString("status").split(",");
            String Status = "";
            for (int i = 0; i < Status_array.length; i++){
                if(i==0) Status += "STATUS=" + "'"+Status_array[i]+"'";
                else Status += "OR"+"STATUS=" + "'"+ Status_array[i]+"'";
            }
            System.out.println("Status");
            System.out.println(Status);

            String Queue = "";
            try {
                Queue = json_payload.getString("queue");
            } catch (Exception e) {
                //TODO: handle exception
                Queue = "";
            }

            System.out.println(Queue);
            if(Queue!=""&&Queue!=null&&Queue!=" "){
                if(Status_array.length>0) Queue = "QUEUE=" + "'"+ Queue +"'" +"AND";
                else Queue ="QUEUE=" + "'"+ Queue +"'";
            }
            else Queue = "";

            String rtauth_url = rt_url+"/REST/2.0/tickets?query="+Queue.replace(" ", "%20")+Status.replace(" ", "%20")
            +"&fields=Owner,Status,Created,Subject,Queue,CustomFields&fields[Queue]=Name,Description";

            System.out.println(rtauth_url);
            
            URL url = new URL(rtauth_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Cookie", cookie);
            con.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.flush();
            out.close();
         
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
                System.out.println(contBuffer.toString());
                return contBuffer.toString();
            }else{
                return "Error";
            }
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Format Error";
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "I/0 Error";
        }
    }

    public String rt_create_ticket(String rt_url, String payload){
        try {
            JSONObject json_payload = new JSONObject(payload);
            String json_data = json_payload.getString("data");
            String cookie = json_payload.getString("cookie");

            String rtauth_url = rt_url+"/REST/2.0/ticket";

            System.out.println(rtauth_url);
            
            URL url = new URL(rtauth_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Cookie", cookie);
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            byte[] input = json_data.getBytes("utf-8");
            out.write(input, 0, input.length);
            out.flush();
            out.close();
         
            int status = con.getResponseCode();
            System.out.println(status);
            if (status == 201){
                StringBuffer contBuffer = new StringBuffer();
                String inputLine = null;
        
                BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
                while((inputLine = in.readLine())!=null){
                    contBuffer.append(inputLine);
                    contBuffer.append("\r\n");
                } 
                con.disconnect();
                System.out.println(contBuffer.toString());
                return contBuffer.toString();
            }else{
                return "Error";
            }
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Format Error";
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "I/0 Error";
        }
    }

    public String rt_update_ticket(String rt_url, String payload){
        try {
            JSONObject json_payload = new JSONObject(payload);
            String json_data = json_payload.getString("data");
            String cookie = json_payload.getString("cookie");
            String ticket_id = json_payload.getString("ticket_id");

            String rtauth_url = rt_url+"/REST/2.0/ticket/"+ticket_id;

            System.out.println(rtauth_url);
            
            URL url = new URL(rtauth_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Cookie", cookie);
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            byte[] input = json_data.getBytes("utf-8");
            out.write(input, 0, input.length);
            out.flush();
            out.close();
         
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
                System.out.println(contBuffer.toString());
                return contBuffer.toString();
            }else{
                return "Error";
            }
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Format Error";
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "I/0 Error";
        }
    }

    public String rt_comment_ticket(String rt_url, String payload){
        try {
            JSONObject json_payload = new JSONObject(payload);
            String comment = json_payload.getString("comment");
            String cookie = json_payload.getString("cookie");
            String ticket_id = json_payload.getString("ticket_id");

            String rtauth_url = rt_url+"/REST/2.0/ticket/"+ticket_id+"/comment";

            System.out.println(rtauth_url);
            
            URL url = new URL(rtauth_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Cookie", cookie);
            con.setRequestProperty("Content-Type", "text/plain; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            byte[] input = comment.getBytes("utf-8");
            out.write(input, 0, input.length);
            out.flush();
            out.close();
         
            int status = con.getResponseCode();
            System.out.println(status);
            if (status == 201){
                StringBuffer contBuffer = new StringBuffer();
                String inputLine = null;
        
                BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
                while((inputLine = in.readLine())!=null){
                    contBuffer.append(inputLine);
                    contBuffer.append("\r\n");
                } 
                con.disconnect();
                System.out.println(contBuffer.toString());
                return contBuffer.toString();
            }else{
                return "Error";
            }
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Format Error";
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "I/0 Error";
        }
    }

}
