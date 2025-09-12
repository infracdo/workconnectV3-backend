package com.tracker_app.tracker.Controller;

import com.tracker_app.tracker.DataSource_elasticsearch.model.ElasticSiteSummary;
// import com.tracker_app.tracker.DataSource_elasticsearch.model.ElasticSiteSummary;
// import com.tracker_app.tracker.DataSource_elasticsearch.service.ElasticSiteSummary_service;
//import com.tracker_app.tracker.Helper.kafka_helper;
import com.tracker_app.tracker.Helper.netbox_helper;
import com.tracker_app.tracker.Helper.zabbix_helper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/netbox")
public class netbox_controller {

  @Autowired
  private netbox_helper netboxhelper;

  //@Autowired
  //kafka_helper kafka = new kafka_helper();

  @Autowired
  private zabbix_helper zabbix_help;

  // @Autowired
  // private ElasticSiteSummary_service elksite_sum_service;

  @Value("${NETBOX.URL}")
  String netbox_url;

  @Value("${NETBOX.TOKEN}")
  String netbox_auth_token;

  @Value("${NETBOX.SANDBOX.URL}")
  String netbox_sandbox_url;

  @Value("${NETBOX.SANDBOX.TOKEN}")
  String netbox_sandbox_auth_token;

  @Value("${download_path}")
  private String UploadPath;

  @Value("${download_path_tem}")
  private String UploadPath_temp;

  @Async("asyncExecutor")
  @GetMapping(path = "/site/all")
  public CompletableFuture<ResponseEntity<String>> get_all_netbox_sites()
    throws IOException, InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");

    String new_payload = netboxhelper.get_all_sites(
      netbox_auth_token,
      netbox_url
    );

    if (new_payload.contains("ERROR CODE 403")) {
      return CompletableFuture.completedFuture(
        new ResponseEntity<String>(HttpStatus.FORBIDDEN)
      );
    } else if (new_payload.contains("ERROR CODE 401")) {
      return CompletableFuture.completedFuture(
        new ResponseEntity<String>(HttpStatus.UNAUTHORIZED)
      );
    } else {
      return CompletableFuture.completedFuture(
        new ResponseEntity<String>(new_payload, headers, HttpStatus.OK)
      );
    }
  }

  @PostMapping(path = "/find/site")
  public CompletableFuture<ResponseEntity<String>> find_netbox_sites(
    @RequestBody String payload
  ) throws IOException, InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");

    String new_payload = "";

    try {
      JSONObject json = new JSONObject(payload);
      String SiteName = json.optString("site_name");

      new_payload =
        netboxhelper.find_sites(
          netbox_auth_token,
          netbox_url,
          SiteName.replace(" ", "%20")
        );
    } catch (JSONException e) {
      return CompletableFuture.completedFuture(
        new ResponseEntity<String>("ERROR JSON PAYLOAD", headers, HttpStatus.OK)
      );
    }

    if (new_payload.contains("ERROR CODE 403")) {
      return CompletableFuture.completedFuture(
        new ResponseEntity<String>(HttpStatus.FORBIDDEN)
      );
    } else if (new_payload.contains("ERROR CODE 401")) {
      return CompletableFuture.completedFuture(
        new ResponseEntity<String>(HttpStatus.UNAUTHORIZED)
      );
    } else {
      return CompletableFuture.completedFuture(
        new ResponseEntity<String>(new_payload, headers, HttpStatus.OK)
      );
    }
  }

  @PostMapping(path = "/find/site/device/modem")
  public CompletableFuture<ResponseEntity<Object>> find_netbox_device_modem(
    @RequestBody String payload
  ) throws IOException, InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");

    String new_payload = "";

    try {
      JSONObject json = new JSONObject(payload);
      String SiteName = json.optString("site_name");

      // System.out.println(SiteName.toLowerCase().replace(" ", "-"));
      new_payload =
        netboxhelper.get_device_modem(
          netbox_auth_token,
          netbox_url,
          SiteName.toLowerCase().replace(" ", "-")
        );
      /*
            ElasticSiteSummary site = elksite_sum_service.findBySiteId(SiteName);
            if(site.getTenant_name().contains("DICT")){
                String[] name_arr = SiteName.split("-");
                String ip = null;
                String newname = null;
                String netboxname = null;
                if(name_arr[0].equalsIgnoreCase("l1")){
                    newname = name_arr[0] +"%20-%20"+site.getZipcode();
                }else if(name_arr[0].equalsIgnoreCase("l2")){
                    newname = name_arr[0] +"%20-%20"+site.getZipcode();
                }else if(name_arr[0].equalsIgnoreCase("l3")){
                    ip = zabbix_help.getSiteIP(SiteName+".");
                    newname = name_arr[0] +"%20-%20"+ip;
                }else if(name_arr[0].equalsIgnoreCase("l4")){
                    ip = zabbix_help.getSiteIP(SiteName+".");
                    newname = name_arr[0] +"%20-%20"+ip;
                }else{
                    List<Map<String, Object>> empty = new ArrayList<Map<String, Object>>();
                    return CompletableFuture.completedFuture(new ResponseEntity<Object>("Not Maped",headers,HttpStatus.OK));
                }
                netboxname = netboxhelper.get_sites_realname(netbox_auth_token, netbox_url,newname);
                if(netboxname.contains("ERROR")){
                    netboxname = netboxhelper.get_sites_realname(netbox_sandbox_auth_token, netbox_sandbox_url,newname);
                    if(netboxname.contains("ERROR")){
                        List<Map<String, Object>> empty = new ArrayList<Map<String, Object>>();
                        return CompletableFuture.completedFuture(new ResponseEntity<Object>("Missing in Netbox "+newname,headers,HttpStatus.OK));
                    }else{
                        new_payload = netboxhelper.get_device_modem(netbox_sandbox_auth_token, netbox_sandbox_url, netboxname);
                    }
                }else{
                    new_payload = netboxhelper.get_device_modem(netbox_auth_token, netbox_url, netboxname);
                }
            }
            else{
                System.out.println(SiteName.toLowerCase().replace(" ", "-"));
                new_payload = netboxhelper.get_device_modem(netbox_auth_token, netbox_url, SiteName.toLowerCase().replace(" ", "-"));
            }
            */
    } catch (JSONException e) {
      List<Map<String, Object>> empty = new ArrayList<Map<String, Object>>();
      return CompletableFuture.completedFuture(
        new ResponseEntity<Object>(empty, headers, HttpStatus.OK)
      );
    }

    if (new_payload.contains("ERROR")) {
      List<Map<String, Object>> empty = new ArrayList<Map<String, Object>>();
      return CompletableFuture.completedFuture(
        new ResponseEntity<Object>(empty, headers, HttpStatus.OK)
      );
    } else {
      return CompletableFuture.completedFuture(
        new ResponseEntity<Object>(new_payload, headers, HttpStatus.OK)
      );
    }
    /* 
        if (new_payload.contains("ERROR CODE 403")){
            List<Map<String, Object>> empty = new ArrayList<Map<String, Object>>();
            return CompletableFuture.completedFuture(new ResponseEntity<Object>(empty,headers,HttpStatus.OK));
        }else if(new_payload.contains("ERROR CODE 401")){
            List<Map<String, Object>> empty = new ArrayList<Map<String, Object>>();
            return CompletableFuture.completedFuture(new ResponseEntity<Object>(empty,headers,HttpStatus.OK));
        }else{
            return CompletableFuture.completedFuture(new ResponseEntity<Object>(new_payload,headers,HttpStatus.OK));
        }
        */
  }

  @PostMapping(path = "/edit/device/modem")
  public CompletableFuture<ResponseEntity<String>> update_netbox_device_modem(
    @RequestBody String payload
  ) throws IOException, InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");

    String new_payload;
    try {
      new_payload =
        netboxhelper.edit_device(netbox_auth_token, netbox_url, payload);
    } catch (JSONException e) {
      new_payload = "PAYLOAD JSON ERROR";
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    if (new_payload.contains("ERROR CODE 403")) {
      return CompletableFuture.completedFuture(
        new ResponseEntity<String>(HttpStatus.FORBIDDEN)
      );
    } else if (new_payload.contains("ERROR CODE 401")) {
      return CompletableFuture.completedFuture(
        new ResponseEntity<String>(HttpStatus.UNAUTHORIZED)
      );
    } else {
      return CompletableFuture.completedFuture(
        new ResponseEntity<String>(new_payload, headers, HttpStatus.OK)
      );
    }
  }

  @RequestMapping(
    path = "/device/image_attachments",
    method = RequestMethod.POST
  )
  public CompletableFuture<ResponseEntity<String>> upload_image_attachments(
    @RequestPart String content_type,
    @RequestPart Integer object_id,
    @RequestPart Integer image_height,
    @RequestPart Integer image_width,
    @RequestPart MultipartFile image
  ) throws IOException, InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");

    String path = new java.io.File(UploadPath_temp).getAbsolutePath();

    if (!new java.io.File(path).exists()) {
      new java.io.File(path).mkdir();
    }
    java.io.File dest = new java.io.File(
      path + "/" + image.getOriginalFilename()
    );
    try {
      image.transferTo(dest);
    } catch (Exception e) {
      return CompletableFuture.completedFuture(
        new ResponseEntity<String>(
          "Server Error Saving Image",
          headers,
          HttpStatus.CONFLICT
        )
      );
    }

    HttpStatus result = netboxhelper.upload_image_attachment(
      netbox_auth_token,
      netbox_url,
      content_type,
      object_id,
      image_height,
      image_width,
      dest
    );

    dest.delete();
    if (result.value() == 200 || result.value() == 201) {
      return CompletableFuture.completedFuture(
        new ResponseEntity<String>("Upload successful", headers, result)
      );
    } else {
      return CompletableFuture.completedFuture(
        new ResponseEntity<String>("Upload failed", headers, result)
      );
    }
  }

  @GetMapping(path = "/device/image_attachments")
  public CompletableFuture<ResponseEntity<Object>> getImageAttachmentsget_device_image_attachments(
    @RequestParam(value = "id") String id
  ) throws IOException, InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    String result;
    try {
      result =
        netboxhelper.get_device_image_attachments(
          netbox_auth_token,
          netbox_url,
          id
        );
      String data = "{\"data\":" + result + "}";
      if (result.toString().contains("ERROR")) {
        return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
            result,
            headers,
            HttpStatus.INTERNAL_SERVER_ERROR
          )
        );
      } else {
        return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(data, headers, HttpStatus.OK)
        );
      }
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return CompletableFuture.completedFuture(
        new ResponseEntity<Object>(
          "JSON ERROR",
          headers,
          HttpStatus.INTERNAL_SERVER_ERROR
        )
      );
    }
  }

  @DeleteMapping(path = "/device/image_attachments")
  public CompletableFuture<ResponseEntity<Object>> DeleteImageAttachmentsget_device_image_attachments(
    @RequestParam(value = "id") String id
  ) throws IOException, InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    String result;
    try {
      result =
        netboxhelper.delete_device_image_attachments(
          netbox_auth_token,
          netbox_url,
          id
        );
      if (result.toString().contains("ERROR")) {
        return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
            result,
            headers,
            HttpStatus.INTERNAL_SERVER_ERROR
          )
        );
      } else {
        return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(result, headers, HttpStatus.OK)
        );
      }
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return CompletableFuture.completedFuture(
        new ResponseEntity<Object>(
          "JSON ERROR",
          headers,
          HttpStatus.INTERNAL_SERVER_ERROR
        )
      );
    }
  }

  @GetMapping(path = "/manufacturer")
  public CompletableFuture<ResponseEntity<Object>> get_manufacturer(
    @RequestParam(value = "name", required = false) String name
  ) throws IOException, InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    String result;
    try {
      result =
        netboxhelper.get_manufacturer(netbox_auth_token, netbox_url, name);

      String data = "{\"data\":" + result + "}";
      if (result.toString().contains("ERROR")) {
        return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
            result,
            headers,
            HttpStatus.INTERNAL_SERVER_ERROR
          )
        );
      } else {
        return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(data, headers, HttpStatus.OK)
        );
      }
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return CompletableFuture.completedFuture(
        new ResponseEntity<Object>(
          "JSON ERROR",
          headers,
          HttpStatus.INTERNAL_SERVER_ERROR
        )
      );
    }
  }

  @GetMapping(path = "/manufacturer/device-type")
  public CompletableFuture<ResponseEntity<Object>> get_device_type(
    @RequestParam(value = "manufacturer_id") String manufacturer_id,
    @RequestParam(value = "id", required = false) String id
  ) throws IOException, InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    String result;
    try {
      result =
        netboxhelper.get_device_type(
          netbox_auth_token,
          netbox_url,
          manufacturer_id,
          id
        );

      String data = "{\"data\":" + result + "}";
      if (result.toString().contains("ERROR")) {
        return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
            result,
            headers,
            HttpStatus.INTERNAL_SERVER_ERROR
          )
        );
      } else {
        return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(data, headers, HttpStatus.OK)
        );
      }
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return CompletableFuture.completedFuture(
        new ResponseEntity<Object>(
          "JSON ERROR",
          headers,
          HttpStatus.INTERNAL_SERVER_ERROR
        )
      );
    }
  }

  @PostMapping(path = "/manufacturer/device-type")
  public CompletableFuture<ResponseEntity<Object>> add_device_type(
    @RequestBody String payload
  ) throws IOException, InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    String result;
    try {
      JSONObject json = new JSONObject(payload);

      result =
        netboxhelper.add_device_type(netbox_auth_token, netbox_url, json);

      String data = "{\"data\":" + result + "}";
      if (result.toString().contains("ERROR")) {
        return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
            result,
            headers,
            HttpStatus.INTERNAL_SERVER_ERROR
          )
        );
      } else {
        return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(data, headers, HttpStatus.OK)
        );
      }
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return CompletableFuture.completedFuture(
        new ResponseEntity<Object>(
          "JSON ERROR",
          headers,
          HttpStatus.INTERNAL_SERVER_ERROR
        )
      );
    }
  }

  @DeleteMapping(path = "/manufacturer/device-type")
  public CompletableFuture<ResponseEntity<Object>> delete_device_type(
    @RequestParam(value = "id") String id
  ) throws IOException, InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    String result;
    try {
      result =
        netboxhelper.delete_device_type(netbox_auth_token, netbox_url, id);
      if (result.toString().contains("ERROR")) {
        return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
            result,
            headers,
            HttpStatus.INTERNAL_SERVER_ERROR
          )
        );
      } else {
        return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(result, headers, HttpStatus.OK)
        );
      }
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return CompletableFuture.completedFuture(
        new ResponseEntity<Object>(
          "JSON ERROR",
          headers,
          HttpStatus.INTERNAL_SERVER_ERROR
        )
      );
    }
  }

  @GetMapping(path = "/site/interfaces")
  public CompletableFuture<ResponseEntity<Object>> get_site_interfaces(
    @RequestParam(value = "site_id") String site_id
  ) throws IOException, InterruptedException, JSONException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    String newSite_id = site_id;
    Object site = getSiteDetails(site_id); // REPLACED ELASTICSEARCH CALL WITH MOCK
    /*
        if(site.getTenant_name().contains("DICT")){
            String[] name_arr = site_id.split("-");
            String ip = null;
            String newname = null;
            String netboxname = null;
            if(name_arr[0].equalsIgnoreCase("l1")){
                newname = name_arr[0] +"%20-%20"+site.getZipcode();
            }else if(name_arr[0].equalsIgnoreCase("l2")){
                newname = name_arr[0] +"%20-%20"+site.getZipcode();
            }else if(name_arr[0].equalsIgnoreCase("l3")){
                ip = zabbix_help.getSiteIP(site_id+".");
                newname = name_arr[0] +"%20-%20"+ip;
            }else if(name_arr[0].equalsIgnoreCase("l4")){
                ip = zabbix_help.getSiteIP(site_id+".");
                newname = name_arr[0] +"%20-%20"+ip;
            }else{
                List<Map<String, Object>> empty = new ArrayList<Map<String, Object>>();
                return CompletableFuture.completedFuture(new ResponseEntity<Object>("Not Maped",headers,HttpStatus.OK));
            }
            netboxname = netboxhelper.get_sites_realname(netbox_auth_token, netbox_url,newname);
            if(netboxname.contains("ERROR")){
                netboxname = netboxhelper.get_sites_realname(netbox_sandbox_auth_token, netbox_sandbox_url,newname);
                if(netboxname.contains("ERROR")){
                    List<Map<String, Object>> empty = new ArrayList<Map<String, Object>>();
                    return CompletableFuture.completedFuture(new ResponseEntity<Object>("Missing in Netbox "+newname,headers,HttpStatus.OK));
                }else{
                    newSite_id = netboxname;
                }
            }else{
                newSite_id = netboxname;
            }
        }
        */
    Object obj = netboxhelper.get_site_interface(
      netbox_auth_token,
      netbox_url,
      newSite_id
    );
    if (obj.toString().contains("Error")) {
      obj =
        netboxhelper.get_site_interface(
          netbox_sandbox_auth_token,
          netbox_sandbox_url,
          newSite_id
        );
    }
    System.out.println(obj);
    if (obj.toString().contains("Data Error")) {
      List<Map<String, Object>> empty = new ArrayList<Map<String, Object>>();
      return CompletableFuture.completedFuture(
        new ResponseEntity<Object>(empty, headers, 2000)
      );
    } else if (obj.toString().contains("Network Error")) {
      List<Map<String, Object>> empty = new ArrayList<Map<String, Object>>();
      return CompletableFuture.completedFuture(
        new ResponseEntity<Object>(empty, headers, 50003)
      );
    } else if (obj.toString().contains("Empty")) {
      List<Map<String, Object>> empty = new ArrayList<Map<String, Object>>();
      return CompletableFuture.completedFuture(
        new ResponseEntity<Object>(empty, headers, HttpStatus.OK)
      );
    } else {
      return CompletableFuture.completedFuture(
        new ResponseEntity<Object>(obj, headers, HttpStatus.OK)
      );
    }
  }

  @GetMapping(path = "/test_env_variable")
  public String env_var() {
    return "UploadPath=" + UploadPath + ": UploadPathTemp=" + UploadPath_temp;
  }

    // METHODS FOR MOCK ELASTICSEARCH RESPONSE

  private Object getSiteDetails(String siteId) {
    return "{\"site_id\":\"" + siteId + "\", \"name\":\"Mock Site\", \"location\":\"Mock Location\"}";
  }
}
