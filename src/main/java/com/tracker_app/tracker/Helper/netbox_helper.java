package com.tracker_app.tracker.Helper;

import com.tracker_app.tracker.CustomObject.netbox_image_attachments;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Flow.Publisher;
import javax.net.ssl.HttpsURLConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class netbox_helper {

  //@Autowired
  //kafka_helper kafka = new kafka_helper();

  @Value("${kafka.logs.topic}")
  private String kafka_topic;

  public String get_all_sites(String authToken, String netbox_url)
    throws IOException, InterruptedException {
    String conunt = "50";

    HttpClient client1 = HttpClient.newHttpClient();
    /*
        HttpRequest request1 = HttpRequest.newBuilder()
            .uri(URI.create(netbox_url+"/api/dcim/sites/?tenant=psc&limit=1"))
            .GET()
            .header("Authorization", "Token "+authToken)
            .build();
        */
    HttpRequest request1 = HttpRequest
      .newBuilder()
      .uri(URI.create(netbox_url + "/api/dcim/sites/?limit=1"))
      .GET()
      .header("Authorization", "Token " + authToken)
      .build();
    HttpResponse<String> response1 = client1.send(
      request1,
      HttpResponse.BodyHandlers.ofString()
    );

    try {
      JSONObject jobject = new JSONObject(response1.body());
      conunt = jobject.getString("count");
    } catch (JSONException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    /*
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(netbox_url+"/api/dcim/sites/?tenant=psc&limit="+conunt))
            .GET()
            .header("Authorization", "Token "+authToken)
            .build();
        */

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(URI.create(netbox_url + "/api/dcim/sites/?limit=" + conunt))
      .GET()
      .header("Authorization", "Token " + authToken)
      .build();

    HttpResponse<String> response = client.send(
      request,
      HttpResponse.BodyHandlers.ofString()
    );

    if (response.statusCode() == 200) {
      try {
        JSONObject jsonobect = new JSONObject(response.body());
        String sites = "{\"data\":" + jsonobect.getString("results") + "}";
        return sites;
      } catch (JSONException e) {
        return "JSON ERROR";
      }
    } else {
      return "ERROR CODE " + response.statusCode();
    }
  }

  public String find_sites(
    String authToken,
    String netbox_url,
    String SiteName
  ) throws IOException, InterruptedException {
    /*
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(netbox_url+"/api/dcim/sites/?tenant=psc&name__ic="+SiteName))
            .GET()
            .header("Authorization", "Token "+authToken)
            .build();
        */
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(URI.create(netbox_url + "/api/dcim/sites/?name__ic=" + SiteName))
      .GET()
      .header("Authorization", "Token " + authToken)
      .build();
    HttpResponse<String> response = client.send(
      request,
      HttpResponse.BodyHandlers.ofString()
    );
    System.out.println(response.statusCode());
    System.out.println(response.body());
    if (response.statusCode() == 200) {
      try {
        JSONObject jsonobect = new JSONObject(response.body());
        String sites = "{\"data\":" + jsonobect.getString("results") + "}";
        return sites;
      } catch (JSONException e) {
        return "JSON ERROR";
      }
    } else {
      return "ERROR CODE " + response.statusCode();
    }
  }

  public String get_sites_realname(
    String authToken,
    String netbox_url,
    String SiteName
  ) throws IOException, InterruptedException {
    /*
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(netbox_url+"/api/dcim/sites/?tenant=psc&name__ic="+SiteName))
            .GET()
            .header("Authorization", "Token "+authToken)
            .build();
        */
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(URI.create(netbox_url + "/api/dcim/sites/?name__ic=" + SiteName))
      .GET()
      .header("Authorization", "Token " + authToken)
      .build();
    HttpResponse<String> response = client.send(
      request,
      HttpResponse.BodyHandlers.ofString()
    );
    System.out.println(response.statusCode());
    System.out.println(response.body());
    if (response.statusCode() == 200) {
      try {
        JSONObject jsonobect = new JSONObject(response.body());
        String name = jsonobect
          .getJSONArray("results")
          .getJSONObject(0)
          .getString("slug");
        return name;
      } catch (JSONException e) {
        return "JSON ERROR";
      }
    } else {
      return "ERROR CODE " + response.statusCode();
    }
  }

  public String get_device_modem(
    String authToken,
    String netbox_url,
    String SiteName
  ) throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    /*
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(netbox_url+"/api/dcim/devices/?site="+SiteName+"&role=modem"))
            .GET()
            .header("Authorization", "Token "+authToken)
            .build();
        */
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(URI.create(netbox_url + "/api/dcim/devices/?site=" + SiteName))
      .GET()
      .header("Authorization", "Token " + authToken)
      .build();

    HttpResponse<String> response = client.send(
      request,
      HttpResponse.BodyHandlers.ofString()
    );
    // System.out.println(response.statusCode());
    // System.out.println(response.body());
    if (response.statusCode() == 200) {
      try {
        JSONObject jsonobect = new JSONObject(response.body());
        String sites = "{\"data\":" + jsonobect.getString("results") + "}";
        return sites;
      } catch (JSONException e) {
        return "JSON ERROR";
      }
    } else {
      return "ERROR CODE " + response.statusCode();
    }
  }

  public String get_site_id_via_ipam(
    String authToken,
    String netbox_url,
    String ip
  ) throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    /*
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(netbox_url+"/api/dcim/devices/?site="+SiteName+"&role=modem"))
            .GET()
            .header("Authorization", "Token "+authToken)
            .build();
        */
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(URI.create(netbox_url + "/api/ipam/ip-addresses/?address=" + ip))
      .GET()
      .header("Authorization", "Token " + authToken)
      .build();

    HttpResponse<String> response = client.send(
      request,
      HttpResponse.BodyHandlers.ofString()
    );
    //System.out.println(response.statusCode());
    //System.out.println(response.body());
    if (response.statusCode() == 200) {
      try {
        JSONObject jsonobect = new JSONObject(response.body());
        //System.out.println(jsonobect.getString("display_name").replace("CPE-", ""));
        JSONObject result = jsonobect.getJSONArray("results").getJSONObject(0);
        //String sites = "{\"data\":"+jsonobect.getString("results")+"}";
        return result
          .getJSONObject("assigned_object")
          .getJSONObject("device")
          .getString("display_name")
          .replace("CPE-", "");
      } catch (JSONException e) {
        System.out.println(e);
        return "JSON ERROR";
      }
    } else {
      return "ERROR CODE " + response.statusCode();
    }
  }

  public String get_device_detail(
    String authToken,
    String netbox_url,
    String device_id
  ) throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(URI.create(netbox_url + "/api/dcim/devices/" + device_id + "/"))
      .GET()
      .header("Authorization", "Token " + authToken)
      .build();

    HttpResponse<String> response = client.send(
      request,
      HttpResponse.BodyHandlers.ofString()
    );

    if (response.statusCode() == 200) {
      try {
        JSONObject jsonobect = new JSONObject(response.body());
        JSONObject custom_fields = new JSONObject(
          jsonobect.getString("custom_fields")
        );

        String name = jsonobect.getString("name");
        String imei = custom_fields.getString("IMEI");
        String imsi = custom_fields.getString("IMSI");
        String device_type = jsonobect.getString("device_type");
        System.out.println(imei);
        StringBuilder sb = new StringBuilder();
        sb.append("{\"name\": \"" + name + "\",");
        sb.append("\"device_type\":" + device_type + ",");
        sb.append("\"custom_fields\":{");
        if (imei.isEmpty()) {
          sb.append("\"IMEI\":null,");
        } else {
          sb.append("\"IMEI\":\"" + imei + "\",");
        }
        if (imsi.isEmpty()) {
          sb.append("\"IMSI\":null");
        } else {
          sb.append("\"IMSI\":\"" + imsi + "\"");
        }
        sb.append("}}");
        return sb.toString();
      } catch (JSONException e) {
        return "JSON ERROR";
      }
    } else {
      return "ERROR CODE " + response.statusCode();
    }
  }

  public String edit_device(
    String authToken,
    String netbox_url,
    String payload
  ) throws IOException, InterruptedException, JSONException {
    JSONObject jsonobect;
    try {
      jsonobect = new JSONObject(payload);
    } catch (JSONException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
      return "PAYLOAD JSON ERROR";
    }
    String id = jsonobect.getString("id");
    String name = jsonobect.getString("name");
    String imei = jsonobect.getString("imei");
    String imsi = jsonobect.getString("imsi");
    String device_type = jsonobect.getString("device_type");
    String user = jsonobect.getString("user");

    StringBuilder sb = new StringBuilder();
    sb.append("{\"name\": \"" + name + "\",");
    if (device_type.isEmpty()) {
      sb.append("\"device_type\":" + device_type + ",");
    }
    sb.append("\"custom_fields\":{");

    if (imei.isEmpty()) {
      sb.append("\"IMEI\":null,");
    } else {
      sb.append("\"IMEI\":\"" + imei + "\",");
    }
    if (imsi.isEmpty()) {
      sb.append("\"IMSI\":null");
    } else {
      sb.append("\"IMSI\":\"" + imsi + "\"");
    }
    sb.append("}}");

    String new_values = sb.toString();
    String old_values = get_device_detail(authToken, netbox_url, id);

    System.out.println(sb.toString());

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(URI.create(netbox_url + "/api/dcim/devices/" + id + "/"))
      .method("PATCH", HttpRequest.BodyPublishers.ofString(sb.toString()))
      .header("Authorization", "Token " + authToken)
      .header("Content-Type", "application/json")
      .build();

    HttpResponse<String> response = client.send(
      request,
      HttpResponse.BodyHandlers.ofString()
    );

    if (response.statusCode() == 200) {
      /*
            StringBuilder kafka_message = new StringBuilder();
            kafka_message.append("{");
            kafka_message.append("\"user\":\""+user+"\",");
            kafka_message.append("\"workconnect_component\":\"netbox\",");
            kafka_message.append("\"log_type\":\"device_edit\",");
            kafka_message.append("\"old_value\":"+old_values+",");
            kafka_message.append("\"new_value\":"+new_values+"");
            kafka_message.append("}");

            //kafka.sendMessage(kafka_message.toString(), "test_rt_topic");
            kafka.sendMessage(kafka_message.toString(), kafka_topic);
            */
      return "device updated successfully";
    } else {
      return "ERROR CODE " + response.statusCode();
    }
  }

  public HttpStatus upload_image_attachment(
    String authToken,
    String netbox_url,
    String content_type,
    Integer object_id,
    Integer image_height,
    Integer image_width,
    java.io.File image_path
  ) throws IOException, InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Token " + authToken);
    headers.add(
      "Content-Type",
      "multipart/form-data; boundary=----WebKitFormBoundarymcm5XSZwdvAXEoLT"
    );

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("image", new FileSystemResource(image_path));
    body.add("content_type", content_type);
    //body.add("name", data.getname());
    body.add("object_id", object_id);
    body.add("image_height", image_height);
    body.add("image_width", image_width);
    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(
      body,
      headers
    );

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.postForEntity(
      netbox_url + "/api/extras/image-attachments/",
      requestEntity,
      String.class
    );

    System.out.println(response.getBody());

    return response.getStatusCode();
  }

  public String get_device_image_attachments(
    String authToken,
    String netbox_url,
    String device_id
  ) throws IOException, InterruptedException, JSONException {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(
        URI.create(
          netbox_url + "/api/extras/image-attachments/?object_id=" + device_id
        )
      )
      .GET()
      .header("Authorization", "Token " + authToken)
      .build();
    HttpResponse<String> response = client.send(
      request,
      HttpResponse.BodyHandlers.ofString()
    );

    if (response.statusCode() == 200) {
      JSONObject json = new JSONObject(response.body());
      return json.getString("results");
    } else {
      return "ERROR " + response.body();
    }
  }

  public String delete_device_image_attachments(
    String authToken,
    String netbox_url,
    String device_id
  ) throws IOException, InterruptedException, JSONException {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(
        URI.create(
          netbox_url + "/api/extras/image-attachments/" + device_id + "/"
        )
      )
      .DELETE()
      .header("Authorization", "Token " + authToken)
      .build();
    HttpResponse<String> response = client.send(
      request,
      HttpResponse.BodyHandlers.ofString()
    );

    if (response.statusCode() == 204) {
      return "Deleted";
    } else {
      return "ERROR " + response.body();
    }
  }

  public String get_manufacturer(
    String authToken,
    String netbox_url,
    String name
  ) throws IOException, InterruptedException, JSONException {
    HttpClient client1 = HttpClient.newHttpClient();
    HttpResponse<String> response1;
    HttpRequest request1;
    request1 =
      HttpRequest
        .newBuilder()
        .uri(URI.create(netbox_url + "/api/dcim/manufacturers/?limit=1"))
        .GET()
        .header("Authorization", "Token " + authToken)
        .build();

    response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
    JSONObject json_reponse = new JSONObject(response1.body());

    String count = json_reponse.getString("count");

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response;
    HttpRequest request;
    if (name == null) {
      request =
        HttpRequest
          .newBuilder()
          .uri(
            URI.create(netbox_url + "/api/dcim/manufacturers/?limit=" + count)
          )
          .GET()
          .header("Authorization", "Token " + authToken)
          .build();
    } else {
      request =
        HttpRequest
          .newBuilder()
          .uri(
            URI.create(netbox_url + "/api/dcim/manufacturers/?name__ic=" + name)
          )
          .GET()
          .header("Authorization", "Token " + authToken)
          .build();
    }

    response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 204 || response.statusCode() == 200) {
      JSONObject json = new JSONObject(response.body());
      return json.getString("results");
    } else {
      return "ERROR " + response.body();
    }
  }

  public Object get_site_interface(
    String authToken,
    String netbox_url,
    String SiteName
  ) throws IOException, InterruptedException, JSONException {
    List<Map<String, Object>> interfaces = new ArrayList<Map<String, Object>>();

    //==================get interface count==============
    HttpClient client1 = HttpClient.newHttpClient();
    HttpResponse<String> response1;
    HttpRequest request1;
    request1 =
      HttpRequest
        .newBuilder()
        .uri(
          URI.create(
            netbox_url + "/api/dcim/interfaces/?site=" + SiteName + "&limit=1"
          )
        )
        .GET()
        .header("Authorization", "Token " + authToken)
        .build();

    response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
    System.out.println(response1.statusCode());
    if (
      response1.statusCode() >= 300 && response1.statusCode() <= 399
    ) return "Network Error";
    JSONObject json_reponse = new JSONObject(response1.body());
    String count = null;
    if (!(response1.body().contains("not one of the available choices"))) {
      try {
        count = json_reponse.get("count").toString();
      } catch (Exception e) {
        return "Empty";
        //TODO: handle exception
      }
    } else return "Data Error";
    //==================interface count==============
    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response;
    HttpRequest request;
    request =
      HttpRequest
        .newBuilder()
        .uri(
          URI.create(
            netbox_url +
            "/api/dcim/interfaces/?site=" +
            SiteName +
            "&limit=" +
            count
          )
        )
        .GET()
        .header("Authorization", "Token " + authToken)
        .build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() == 200) {
      JSONObject jbody = new JSONObject(response.body());
      JSONArray jbody_arr = new JSONArray(jbody.getString("results"));
      if (jbody_arr.length() > 0) {
        for (int i = 0; i < jbody_arr.length(); i++) {
          JSONObject data = jbody_arr.getJSONObject(i);
          Map<String, Object> obj = new HashMap<String, Object>();
          String id = data.getString("id").toString();
          String name = data.getString("name").toString();
          String label = data.getString("label").toString();
          String enabled = data.getString("enabled").toString();
          JSONObject jobj_type = new JSONObject(data.getString("type"));
          String type = jobj_type.getString("value");
          String ip = "";
          String status = "";

          HttpClient client2 = HttpClient.newHttpClient();
          HttpResponse<String> response2;
          HttpRequest request2;
          request2 =
            HttpRequest
              .newBuilder()
              .uri(
                URI.create(
                  netbox_url + "/api/ipam/ip-addresses/?interface_id=" + id
                )
              )
              .GET()
              .header("Authorization", "Token " + authToken)
              .build();

          response2 =
            client2.send(request2, HttpResponse.BodyHandlers.ofString());
          JSONObject jbody1 = new JSONObject(response2.body());
          if (jbody1.getInt("count") > 0) {
            JSONArray iptm = new JSONArray(jbody1.getString("results"));
            try {
              jbody1 = iptm.getJSONObject(0);
              ip = jbody1.getString("address");
              JSONObject jobj = new JSONObject(jbody1.getString("status"));
              status = jobj.getString("value");
            } catch (Exception e) {
              //TODO: handle exception
              return jbody1.getInt("count");
            }
          }

          obj.put("name", name);
          obj.put("label", label);
          obj.put("enabled", enabled);
          obj.put("type", type);
          obj.put("ip", ip);
          obj.put("status", status);
          interfaces.add(obj);
        }
      }
      return interfaces;
    } else if (
      response1.statusCode() >= 300 && response1.statusCode() <= 399
    ) return "Network Error"; else if (
      response1.statusCode() >= 400 && response1.statusCode() <= 499
    ) return "Network Error"; else return "Data Error";
  }

  public String get_device_type(
    String authToken,
    String netbox_url,
    String manufacturer_id,
    String id
  ) throws IOException, InterruptedException, JSONException {
    HttpClient client1 = HttpClient.newHttpClient();
    HttpResponse<String> response1;
    HttpRequest request1;
    request1 =
      HttpRequest
        .newBuilder()
        .uri(
          URI.create(
            netbox_url +
            "/api/dcim/device-types/?manufacturer_id=" +
            manufacturer_id +
            "&limit=1"
          )
        )
        .GET()
        .header("Authorization", "Token " + authToken)
        .build();

    response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());

    JSONObject json_reponse = new JSONObject(response1.body());

    String count = json_reponse.getString("count");

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response;
    HttpRequest request;
    if (id == null) {
      request =
        HttpRequest
          .newBuilder()
          .uri(
            URI.create(
              netbox_url +
              "/api/dcim/device-types/?manufacturer_id=" +
              manufacturer_id +
              "&limit=" +
              count
            )
          )
          .GET()
          .header("Authorization", "Token " + authToken)
          .build();
    } else {
      request =
        HttpRequest
          .newBuilder()
          .uri(
            URI.create(
              netbox_url +
              "/api/dcim/device-types/?id=" +
              id +
              "&manufacturer_id=" +
              manufacturer_id
            )
          )
          .GET()
          .header("Authorization", "Token " + authToken)
          .build();
    }

    response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 204 || response.statusCode() == 200) {
      JSONObject json = new JSONObject(response.body());
      return json.getString("results");
    } else {
      return "ERROR " + response.body();
    }
  }

  public String add_device_type(
    String authToken,
    String netbox_url,
    JSONObject payload
  ) throws IOException, InterruptedException, JSONException {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append("\"manufacturer\": " + payload.getString("manufacturer") + ",");
    sb.append("\"model\": \"" + payload.getString("model") + "\",");
    sb.append("\"slug\": \"" + payload.getString("model") + "\",");
    sb.append("\"part_number\": \"" + payload.getString("part_number") + "\",");
    sb.append("\"u_height\": " + payload.getString("u_height") + ",");
    sb.append("\"is_full_depth\": " + payload.getString("is_full_depth") + ",");

    if (payload.getString("subdevice_role").contains("null")) {
      sb.append("\"subdevice_role\": null,");
    } else {
      sb.append(
        "\"subdevice_role\": " + payload.getString("subdevice_role") + ","
      );
    }

    sb.append("\"comments\":\"" + payload.getString("comments") + "\",");
    sb.append("\"tags\": [");
    /*
        if(!payload.getString("tags").contentEquals("[]")){
            JSONObject tags = new JSONObject(payload.getString("tags"));
            sb.append("{\"name\": \""+tags.getString("name")+"\",");
            sb.append("\"slug\": \""+tags.getString("slug")+"\",");
            sb.append("\"color\": \""+tags.getString("color")+"\"}");
        }
        */
    sb.append("],");
    sb.append("\"custom_fields\": {");
    sb.append("}");
    sb.append("}");
    System.out.println(payload);
    System.out.println(sb.toString());

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response;
    HttpRequest request;
    request =
      HttpRequest
        .newBuilder()
        .uri(URI.create(netbox_url + "/api/dcim/device-types/"))
        .POST(HttpRequest.BodyPublishers.ofString(sb.toString()))
        .header("Authorization", "Token " + authToken)
        .header("Content-Type", "application/json")
        .build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (
      response.statusCode() == 204 ||
      response.statusCode() == 200 ||
      response.statusCode() == 201
    ) {
      return response.body();
    } else {
      return "ERROR " + response.body();
    }
  }

  public String delete_device_type(
    String authToken,
    String netbox_url,
    String device_id
  ) throws IOException, InterruptedException, JSONException {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest
      .newBuilder()
      .uri(URI.create(netbox_url + "/api/dcim/device-types/" + device_id + "/"))
      .DELETE()
      .header("Authorization", "Token " + authToken)
      .build();
    HttpResponse<String> response = client.send(
      request,
      HttpResponse.BodyHandlers.ofString()
    );

    if (response.statusCode() == 204) {
      return "Deleted";
    } else {
      return "ERROR " + response.body();
    }
  }
}
