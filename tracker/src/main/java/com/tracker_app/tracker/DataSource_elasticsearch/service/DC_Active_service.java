package com.tracker_app.tracker.DataSource_elasticsearch.service;

import com.tracker_app.tracker.DataSource_elasticsearch.model.DC_Active;
import com.tracker_app.tracker.DataSource_elasticsearch.model.ElasticSiteSummary;
import com.tracker_app.tracker.DataSource_elasticsearch.repo.DC_Active_repository;
import com.tracker_app.tracker.DataSource_elasticsearch.repo.ElasticSiteSummary_repository;

import java.util.Optional;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class DC_Active_service  {
  @Autowired
  DC_Active_repository dc_Active_repository;
  @Autowired
  ElasticSiteSummary_repository elasticSiteSummary_repository;


  public ResponseEntity<Object> createDC_Active( @RequestBody String params) throws JSONException{
      JSONObject output = new JSONObject();
      try {    
      JSONObject body = new JSONObject(params);
      String site_id = body.getString("site_id");
      String status = body.getString("status");
      if(site_id.isBlank() || status.isBlank()){    
        output.put("statusCode", "400");
        output.put("message", "Incomplete Data.");
        return new ResponseEntity<Object>(output.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
      }

      DC_Active temp = new DC_Active();
      temp.setId(site_id);
      temp.setSite_id(site_id);
      temp.setStatus(status);
      dc_Active_repository.save(temp);
      output.put("statusCode", "200");
      output.put("message", "Success Data Entry.");
      return new ResponseEntity<Object>(output.toString(), HttpStatus.ACCEPTED);

    } catch (Exception e) {
        output.put("statusCode", "400");
        output.put("message", "Incomplete Data.");
        return new ResponseEntity<Object>(output.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<Object> createDC_ActiveByList( @RequestBody String params) throws JSONException{
    JSONObject output = new JSONObject();
    try {    
    JSONObject body = new JSONObject(params);
    JSONArray list =  body.getJSONArray("sites");

    if(list == null || list.length() == 0){    
      output.put("statusCode", "400");
      output.put("message", "Incomplete Data.");
      return new ResponseEntity<Object>(output.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    for(int i=0; i<list.length();i++){
      JSONObject item = list.getJSONObject(i);

      String site_id = item.getString("site_id");
      String status = item.getString("status");    
      DC_Active temp = new DC_Active();
      temp.setId(site_id);
      temp.setSite_id(site_id);
      temp.setStatus(status);
      dc_Active_repository.save(temp);
  }
    output.put("statusCode", "200");
    output.put("message", "Success Data Entry.");
    return new ResponseEntity<Object>(output.toString(), HttpStatus.ACCEPTED);

  } catch (Exception e) {
      output.put("statusCode", "400");
      output.put("message", "Incomplete Data.");
      return new ResponseEntity<Object>(output.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

  public ResponseEntity<Object> removeDC_Active( @RequestBody String params) throws JSONException{
      JSONObject output = new JSONObject();
      try {    
      JSONObject body = new JSONObject(params);
      String site_id = body.getString("site_id");
      if(site_id.isBlank()){    
        output.put("statusCode", "400");
        output.put("message", "Not Found");
        return new ResponseEntity<Object>(output.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
      }
      dc_Active_repository.deleteById(site_id);
      output.put("statusCode", "200");
      output.put("message", "Success Data remove.");
      return new ResponseEntity<Object>(output.toString(), HttpStatus.ACCEPTED);
    } catch (Exception e) {
        output.put("statusCode", "400");
        output.put("message", "Not Found.");
        return new ResponseEntity<Object>(output.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }  

  public ResponseEntity<Object> removeAll_DC_Active() throws JSONException{
    JSONObject output = new JSONObject();
    dc_Active_repository.deleteAll();
    output.put("statusCode", "200");
    output.put("message", "All Data removed.");
    return new ResponseEntity<Object>(output.toString(), HttpStatus.ACCEPTED);
  }    

  public Iterable<DC_Active> getAll_DC_Active(){
    BoolQueryBuilder query = QueryBuilders.boolQuery();
    Iterable<DC_Active> list = dc_Active_repository.search(query);
    System.out.println("---->"+list);
    return list;
  }

  public DC_Active get_DC_Active(String site_id){
    Optional<DC_Active> site = dc_Active_repository.findById(site_id);
    return site.get();
  }

  public ResponseEntity<Object> getDC_Active(String site_id) throws JSONException{
    JSONObject output = new JSONObject();
      DC_Active item = get_DC_Active(site_id);
      JSONObject temp = new JSONObject();
      temp.put("site_id", item.getSite_id());
      temp.put("status",item.getStatus());

    output.put("statusCode", "200");
    output.put("sites", temp);
    return new ResponseEntity<Object>(output.toString(), HttpStatus.ACCEPTED);
 }   

  public ResponseEntity<Object> getAllDC_Active() throws JSONException{
    Iterable<DC_Active> list = getAll_DC_Active();
    JSONObject output = new JSONObject();
    JSONArray listOut = new JSONArray();

    for (DC_Active item : list) {
      JSONObject temp = new JSONObject();
      temp.put("site_id", item.getSite_id());
      temp.put("status",item.getStatus());
      listOut.put(temp);
    }
    output.put("statusCode", "200");
    output.put("sites", listOut);
    return new ResponseEntity<Object>(output.toString(), HttpStatus.ACCEPTED);
 }      

  public ResponseEntity<Object> updateSiteSummaryIndex() throws JSONException{
    try{
      JSONObject output = new JSONObject();
      JSONArray listOut = new JSONArray();
        Iterable<DC_Active> list = getAll_DC_Active();
        for (DC_Active item : list) {
          System.out.println("update_site "+item.getSite_id());
            ElasticSiteSummary site = elasticSiteSummary_repository.findBySiteID(item.getSite_id());
            if(site == null)
              continue;
            System.out.println("update_site1 "+site.getSite_id());
            site.setStatus(item.getStatus());
            elasticSiteSummary_repository.save(site);

            JSONObject temp = new JSONObject();
            temp.put("site_id", site.getSite_id());
            temp.put("status",site.getStatus());
            listOut.put(temp);
        }
        output.put("statusCode", "200");
        output.put("sites", listOut);
        return new ResponseEntity<Object>(output.toString(), HttpStatus.ACCEPTED);
      }catch(Exception e){
        return new ResponseEntity<Object>("Error on DC update Site summary index "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
}
