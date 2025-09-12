package com.tracker_app.tracker.Controller;

import com.tracker_app.tracker.DataSource_Atis.Entity.sites_site;
import com.tracker_app.tracker.DataSource_Atis.Helper.products_helper;
import com.tracker_app.tracker.DataSource_Atis.Repo.atis_repo;
import com.tracker_app.tracker.DataSource_WorkConnect.Entity.site_location;
import com.tracker_app.tracker.DataSource_WorkConnect.Entity.sites_summary;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.site_circuit_status_repo;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.site_location_repo;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.sites_summary_repo;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.tenant_lookup_repo;
// import com.tracker_app.tracker.DataSource_elasticsearch.model.ElasticSiteSummary;
// import com.tracker_app.tracker.DataSource_elasticsearch.service.ElasticLogService;
// import com.tracker_app.tracker.DataSource_elasticsearch.service.ElasticSiteSummary_service;
import com.tracker_app.tracker.Helper.AuthService;
import com.tracker_app.tracker.Helper.atis_helper;
import com.tracker_app.tracker.Helper.prometheus_helper;
import com.tracker_app.tracker.Helper.zabbix_helper;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/atis")
public class atis_controller {

  @Value("${ATIS.URL}")
  String atis_url;

  @Autowired
  private products_helper product_helper;

  @Autowired
  private atis_helper atishelper;

  @Autowired
  private prometheus_helper prometheus;

  @Autowired
  private site_location_repo site_loc_repo;

  @Autowired
  private site_circuit_status_repo site_circuit_repo;

  @Autowired
  private AuthService authService;

  @Autowired
  private tenant_lookup_repo tenant_repo;

  // @Autowired
  // private ElasticSiteSummary_service elksite_sum_service;

  @Autowired
  private sites_summary_repo site_sum_repo;
  
  @Autowired
  private atis_repo atisrepo;

  @Autowired
  private zabbix_helper zabbix_help;

  // @Autowired
  // private ElasticLogService elasticLogService;

  @Async("asyncExecutor")
  @GetMapping("/site")
  public CompletableFuture<ResponseEntity<Object>> getSiteDetail(
      @RequestHeader(value = "Authorization", required = false) String auth,
      @RequestParam("site_id") String siteId) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");

    Object result = atisrepo.findInfoBySiteId(siteId);
    HttpStatus status = (result == null) ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK;

    // Optional: add logging logic here if needed
    return CompletableFuture.completedFuture(new ResponseEntity<>(result, headers, status));
  }

  @GetMapping(path = "/site/list")
  public CompletableFuture<ResponseEntity<Object>> find_site_sum(
      @RequestParam(value = "site_id", required = false) String site_id,
      @RequestParam(value = "tenant", required = false) String tenant,
      @RequestParam(value = "siteName", required = false) String siteName,
      @RequestParam(value = "siteStatus", required = false) String siteStatus,
      @RequestParam(value = "no_links_up", required = false) String no_links_up,
      @RequestParam(value = "region", required = false) String region,
      @RequestParam(value = "group", required = false) String group,
      @RequestParam(value = "circuit_provider", required = false) String circuit_provider,
      @RequestParam(value = "pageNo") Integer pageNo,
      @RequestParam(value = "pageSize") Integer pageSize,
      @RequestHeader(value = "Authorization") String auth) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    System.out.println("Empty: " + site_id.isEmpty());

    if (site_id.isEmpty())
      site_id = null;
    else
      site_id = site_id.toLowerCase();

    if (tenant.isEmpty())
      tenant = null;
    else
      tenant = tenant.toLowerCase();

    if (siteName.isEmpty())
      siteName = null;
    else
      siteName = siteName.toLowerCase();

    if (siteStatus.isEmpty())
      siteStatus = null;
    else
      siteStatus = siteStatus.toLowerCase();

    if (no_links_up.isEmpty())
      no_links_up = null;
    else
      no_links_up = no_links_up.toLowerCase();

    if (region.isEmpty())
      region = null;
    else
      region = region.toLowerCase();

    if (group.isEmpty())
      group = null;
    else
      group = group.toLowerCase();

    if (circuit_provider.isEmpty())
      circuit_provider = null;
    else
      circuit_provider = circuit_provider.toLowerCase();

    String tenant_name = null;
    try {
      Map<String, List<String>> attrib = authService.getUserAttributes(auth);
      System.out.println("TENANT: " + attrib);
      String slug = attrib.get("tenant").get(0);
      tenant_name = tenant_repo.get_tenant_by_slug(slug);
    } catch (Exception e) {
      // TODO: handle exception
      tenant_name = tenant;
    }

    if (tenant_name != null) {
      tenant_name = tenant_name.toLowerCase();
    }

    Object result = getSiteDetailsPaged(
        site_id, tenant_name, siteName, siteStatus, no_links_up,
        region, group, circuit_provider, pageSize, pageNo); // REPLACED ELASTICSEARCH CALL WITH MOCK
    if (result.toString().contains("ERROR")) {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
              result,
              headers,
              HttpStatus.INTERNAL_SERVER_ERROR));
    } else {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(result, headers, HttpStatus.OK));
    }
  }

  // Mocked method to simulate fetching site location details (replace with actual
  // logic)
  // In place of the Elasticsearch service, this could query a database or another
  // service.
  private Object getSiteLocation(String siteId, String tenantName, String siteName, String siteStatus, String noLinksUp, String region, String group, String circuitProvider) {
    // Replace with actual logic for fetching site location details
    // This could be a database query or another service call instead of
    // Elasticsearch
    return Map.of(
        "site_location", Map.of(
            "site_id", siteId,
            "tenant", tenantName,
            "site_name", siteName,
            "status", siteStatus,
            "region", region,
            "location", Map.of("latitude", "12.34", "longitude", "56.78")));
  }

  @GetMapping(path = "/site/list/all")
  public CompletableFuture<ResponseEntity<Object>> find_site_sum_all(
      @RequestParam(value = "site_id", required = false) String site_id,
      @RequestParam(value = "tenant", required = false) String tenant,
      @RequestParam(value = "siteName", required = false) String siteName,
      @RequestParam(value = "siteStatus", required = false) String siteStatus,
      @RequestParam(value = "no_links_up", required = false) String no_links_up,
      @RequestParam(value = "region", required = false) String region,
      @RequestParam(value = "group", required = false) String group,
      @RequestParam(value = "circuit_provider", required = false) String circuit_provider,
      @RequestHeader(value = "Authorization") String auth) throws IOException, InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    System.out.println("Empty: " + site_id.isEmpty());

    if (site_id.isEmpty())
      site_id = null;
    else
      site_id = site_id.toLowerCase();

    if (tenant.isEmpty())
      tenant = null;
    else
      tenant = tenant.toLowerCase();

    if (siteName.isEmpty())
      siteName = null;
    else
      siteName = siteName.toLowerCase();

    if (siteStatus.isEmpty())
      siteStatus = null;
    else
      siteStatus = siteStatus.toLowerCase();

    if (no_links_up.isEmpty())
      no_links_up = null;
    else
      no_links_up = no_links_up.toLowerCase();

    if (region.isEmpty())
      region = null;
    else
      region = region.toLowerCase();

    if (group.isEmpty())
      group = null;
    else
      group = group.toLowerCase();

    if (circuit_provider.isEmpty())
      circuit_provider = null;
    else
      circuit_provider = circuit_provider.toLowerCase();

    String tenant_name = null;
    try {
      Map<String, List<String>> attrib = authService.getUserAttributes(auth);
      System.out.println("TENANT: " + attrib);
      String slug = attrib.get("tenant").get(0);
      tenant_name = tenant_repo.get_tenant_by_slug(slug);
    } catch (Exception e) {
      // TODO: handle exception
      tenant_name = tenant;
    }

    if (tenant_name != null) {
      tenant_name = tenant_name.toLowerCase();
    }

    Object result = getSiteDetails(site_id, tenant_name, siteName, siteStatus, no_links_up, region, group,
        circuit_provider); // REPLACED ELASTICSEARCH CALL WITH MOCK

    if (result.toString().contains("ERROR")) {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
              result,
              headers,
              HttpStatus.INTERNAL_SERVER_ERROR));
    } else {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(result, headers, HttpStatus.OK));
    }
  }

  @Async("asyncExecutor")
  @GetMapping(path = "/site/assets")
  public CompletableFuture<ResponseEntity<Object>> get_site_assets(
      @RequestParam(value = "site_id") String site_id) throws IOException, InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    Object result;

    result = atishelper.get_site_assets(atis_url, site_id);
    System.out.println(result);
    if (result.toString().contains("ERROR")) {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
              result,
              headers,
              HttpStatus.INTERNAL_SERVER_ERROR));
    } else {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(result, headers, HttpStatus.OK));
    }
  }

  @Async("asyncExecutor")
  @GetMapping(path = "/site/interface")
  public CompletableFuture<ResponseEntity<Object>> get_site_interface(
      @RequestParam(value = "site_id") String site_id) throws IOException, InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    Object result = atishelper.get_site_interface(atis_url, site_id);

    if (result.toString().contains("ERROR")) {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
              result,
              headers,
              HttpStatus.INTERNAL_SERVER_ERROR));
    } else {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(result, headers, HttpStatus.OK));
    }
  }

  @Async("asyncExecutor")
  @GetMapping(path = "/site/circuits")
  public CompletableFuture<ResponseEntity<Object>> get_site_circuits(
      @RequestParam(value = "site_id") String site_id) throws IOException, InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    Object result = atishelper.get_site_circuits(atis_url, site_id);

    if (result.toString().contains("ERROR")) {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
              result,
              headers,
              HttpStatus.INTERNAL_SERVER_ERROR));
    } else {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(result, headers, HttpStatus.OK));
    }
  }

  @Async("asyncExecutor")
  @GetMapping(path = "/site/prometheus/circuits")
  public CompletableFuture<ResponseEntity<Object>> get_site_prometheus_circuits(
      @RequestParam(value = "site_id") String site_id,
      @RequestParam(value = "str_end_date") String str_end_date,
      @RequestParam(value = "hours_interval") Long hours_interval)
      throws IOException, InterruptedException, JSONException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    Object result = prometheus.get_circuit_data(
        site_id,
        str_end_date,
        hours_interval);

    return CompletableFuture.completedFuture(
        new ResponseEntity<Object>(result, headers, HttpStatus.OK));
  }

  @GetMapping(path = "/site/location")
  public CompletableFuture<ResponseEntity<Object>> get_site_location_endpoint(
      @RequestParam(value = "site_id", required = false) String site_id,
      @RequestParam(value = "tenant", required = false) String tenant,
      @RequestParam(value = "siteName", required = false) String siteName,
      @RequestParam(value = "siteStatus", required = false) String siteStatus,
      @RequestParam(value = "no_links_up", required = false) String no_links_up,
      @RequestParam(value = "region", required = false) String region,
      @RequestParam(value = "group", required = false) String group,
      @RequestParam(value = "circuit_provider", required = false) String circuit_provider,
      @RequestHeader(value = "Authorization") String auth) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    System.out.println("Empty: " + site_id.isEmpty());

    if (site_id.isEmpty())
      site_id = null;
    else
      site_id = site_id.toLowerCase();

    if (tenant.isEmpty())
      tenant = null;
    else
      tenant = tenant.toLowerCase();

    if (siteName.isEmpty())
      siteName = null;
    else
      siteName = siteName.toLowerCase();

    if (siteStatus.isEmpty())
      siteStatus = null;
    else
      siteStatus = siteStatus.toLowerCase();

    if (no_links_up.isEmpty())
      no_links_up = null;

    if (region.isEmpty())
      region = null;
    else
      region = region.toLowerCase();

    if (group.isEmpty())
      group = null;
    else
      group = group.toLowerCase();

    if (circuit_provider.isEmpty())
      circuit_provider = null;
    else
      circuit_provider = circuit_provider.toLowerCase();

    String tenant_name = null;
    try {
      Map<String, List<String>> attrib = authService.getUserAttributes(auth);
      System.out.println("TENANT: " + attrib);
      String slug = attrib.get("tenant").get(0);
      tenant_name = tenant_repo.get_tenant_by_slug(slug);
    } catch (Exception e) {
      // TODO: handle exception
      tenant_name = tenant;
    }

    if (tenant_name != null) {
      tenant_name = tenant_name.toLowerCase();
    }

    Object result = getSiteLocation(site_id, tenant_name, siteName, siteStatus, no_links_up, region, group,
        circuit_provider); // REPLACED ELASTICSEARCH CALL WITH MOCK
    if (result.toString().contains("ERROR")) {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
              result,
              headers,
              HttpStatus.INTERNAL_SERVER_ERROR));
    } else {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(result, headers, HttpStatus.OK));
    }
  }

  @GetMapping(path = "/site/v2/list")
  public CompletableFuture<ResponseEntity<Object>> find_v2_site_sum(
      @RequestParam(value = "site_id", required = false) String site_id,
      @RequestParam(value = "tenant", required = false) String tenant,
      @RequestParam(value = "siteName", required = false) String siteName,
      @RequestParam(value = "siteStatus", required = false) String siteStatus,
      @RequestParam(value = "no_links_up", required = false) String no_links_up,
      @RequestParam(value = "region", required = false) String region,
      @RequestParam(value = "group", required = false) String group,
      @RequestParam(value = "circuit_provider", required = false) String circuit_provider) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    System.out.println("Empty: " + site_id.isEmpty());

    if (site_id.isEmpty())
      site_id = null;
    else
      site_id = site_id.toLowerCase();

    if (tenant.isEmpty())
      tenant = null;
    else
      tenant = tenant.toLowerCase();

    if (siteName.isEmpty())
      siteName = null;
    else
      siteName = siteName.toLowerCase();

    if (siteStatus.isEmpty())
      siteStatus = null;
    else
      siteStatus = siteStatus.toLowerCase();

    if (no_links_up.isEmpty())
      no_links_up = null;

    if (region.isEmpty())
      region = null;
    else
      region = region.toLowerCase();

    if (group.isEmpty())
      group = null;
    else
      group = group.toLowerCase();

    if (circuit_provider.isEmpty())
      circuit_provider = null;
    else
      circuit_provider = circuit_provider.toLowerCase();

    String tenant_name = null;
    try {
      Map<String, List<String>> attrib = authService.getUserAttributes(null);
      System.out.println("TENANT: " + attrib);
      String slug = attrib.get("tenant").get(0);
      tenant_name = tenant_repo.get_tenant_by_slug(slug);
    } catch (Exception e) {
      // TODO: handle exception
      tenant_name = tenant;
    }

    if (tenant_name != null) {
      tenant_name = tenant_name.toLowerCase();
    }

    Object result = getAllSites(site_id, tenant_name, siteName, siteStatus, no_links_up, region, group,
        circuit_provider); // REPLACED ELASTICSEARCH CALL WITH MOCK
    if (result.toString().contains("ERROR")) {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
              result,
              headers,
              HttpStatus.INTERNAL_SERVER_ERROR));
    } else {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(result, headers, HttpStatus.OK));
    }
  }

  @GetMapping(path = "/site/v2/count")
  public CompletableFuture<ResponseEntity<Object>> v2_site_count(
      @RequestParam(value = "tenant", required = false) String tenant) throws IOException, JSONException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");

    String tenant_name = null;
    try {
      Map<String, List<String>> attrib = authService.getUserAttributes(null);
      System.out.println("TENANT: " + attrib);
      String slug = attrib.get("tenant").get(0);
      tenant_name = tenant_repo.get_tenant_by_slug(slug);
    } catch (Exception e) {
      // TODO: handle exception
      tenant_name = tenant;
    }

    if (tenant_name != null) {
      tenant_name = tenant_name.toLowerCase();
    }

    Object result = getSiteCount(tenant_name); // REPLACED ELASTICSEARCH CALL WITH MOCK
    if (result.toString().contains("ERROR")) {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
              result,
              headers,
              HttpStatus.INTERNAL_SERVER_ERROR));
    } else {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(result, headers, HttpStatus.OK));
    }
  }

  @Async("asyncExecutor")
  @GetMapping(path = "/circuit/providers")
  public CompletableFuture<ResponseEntity<Object>> get_circuit_providers()
      throws IOException, InterruptedException, JSONException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    Object result = atishelper.get_circuit_providers(atis_url);
    JSONArray jarr = new JSONArray(result.toString());
    List<Map<String, Object>> mapped_result = new ArrayList<Map<String, Object>>();
    for (int i = 0; i < jarr.length(); i++) {
      JSONObject jobj = jarr.getJSONObject(i);
      Map<String, Object> map = new HashMap<String, Object>();
      if (jobj.getString("name").contains("DICT-APOLLO"))
        map.put(
            "name",
            "DICT");
      else if (jobj.getString("name").contains("DICT-AZCOM"))
        continue;
      else if (jobj.getString("name").contains("DICT-GLOBE"))
        continue;
      else if (jobj.getString("name").contains("DICT-PLANET CABLE"))
        continue;
      else if (jobj.getString("name").contains("DICT-PLDT"))
        continue;
      else if (jobj.getString("name").contains("GLOBE DSL"))
        map.put("name", "GLOBE");
      else if (jobj.getString("name").contains("GLOBE FO"))
        continue;
      else if (jobj.getString("name").contains("GLOBE SIM"))
        continue;
      else if (jobj.getString("name").contains("Globe SIM"))
        continue;
      else if (jobj.getString("name").contains("PLDT DSL"))
        map.put(
            "name",
            "PLDT");
      else if (jobj.getString("name").contains("PLDT FO"))
        continue;
      else if (jobj.getString("name").contains("SMART SIM"))
        map.put("name", "SMART");
      else if (jobj.getString("name").contains("Smart sim"))
        continue;
      else
        map.put("name", jobj.getString("name"));
      mapped_result.add(map);
    }
    System.out.println(jarr.length());

    if (result.toString().contains("ERROR")) {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
              result,
              headers,
              HttpStatus.INTERNAL_SERVER_ERROR));
    } else {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(mapped_result, headers, HttpStatus.OK));
    }
  }

  @Async("asyncExecutor")
  @GetMapping(path = "/site/status/list")
  public CompletableFuture<ResponseEntity<Object>> get_site_status_list()
      throws IOException, InterruptedException, JSONException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    Object result = atishelper.get_all_site_status(atis_url);
    System.out.println(result);
    JSONArray jarr = new JSONArray(result.toString());
    List<Map<String, Object>> mapped_result = new ArrayList<Map<String, Object>>();
    for (int i = 0; i < jarr.length(); i++) {
      JSONObject jobj = jarr.getJSONObject(i);
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("name", jobj.getString("name"));
      mapped_result.add(map);
    }
    if (result.toString().contains("ERROR")) {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
              result,
              headers,
              HttpStatus.INTERNAL_SERVER_ERROR));
    } else {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(mapped_result, headers, HttpStatus.OK));
    }
  }

  @Async("asyncExecutor")
  @GetMapping(path = "/site/region/list")
  public CompletableFuture<ResponseEntity<Object>> get_site_region_list()
      throws IOException, InterruptedException, JSONException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    Object result = atishelper.get_all_regions(atis_url);
    JSONArray jarr = new JSONArray(result.toString());
    List<Map<String, Object>> mapped_result = new ArrayList<Map<String, Object>>();
    for (int i = 0; i < jarr.length(); i++) {
      JSONObject jobj = jarr.getJSONObject(i);
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("name", jobj.getString("name"));
      mapped_result.add(map);
    }
    if (result.toString().contains("ERROR")) {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
              result,
              headers,
              HttpStatus.INTERNAL_SERVER_ERROR));
    } else {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(mapped_result, headers, HttpStatus.OK));
    }
  }

  @Async("asyncExecutor")
  @GetMapping(path = "/site/island_group/list")
  public CompletableFuture<ResponseEntity<Object>> get_site_island_group_list()
      throws IOException, InterruptedException, JSONException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    Object result = atishelper.get_all_islandGroups(atis_url);
    JSONArray jarr = new JSONArray(result.toString());
    List<Map<String, Object>> mapped_result = new ArrayList<Map<String, Object>>();
    for (int i = 0; i < jarr.length(); i++) {
      JSONObject jobj = jarr.getJSONObject(i);
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("name", jobj.getString("name"));
      mapped_result.add(map);
    }
    if (result.toString().contains("ERROR")) {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
              result,
              headers,
              HttpStatus.INTERNAL_SERVER_ERROR));
    } else {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(mapped_result, headers, HttpStatus.OK));
    }
  }

  @Async("asyncExecutor")
  @GetMapping(path = "/site/tenants")
  public CompletableFuture<ResponseEntity<Object>> get_tenants()
      throws IOException, InterruptedException, JSONException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Content-Type-Options", "nosniff");
    Object result = atishelper.get_site_tenants(atis_url);

    if (result.toString().contains("ERROR")) {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(
              result,
              headers,
              HttpStatus.INTERNAL_SERVER_ERROR));
    } else {
      return CompletableFuture.completedFuture(
          new ResponseEntity<Object>(result, headers, HttpStatus.OK));
    }
  }

  /*
   * @Scheduled(fixedRate = (30*60000))
   * 
   * @Async("asyncExecutor")
   * public void update_site_sum() throws InterruptedException
   * {
   * System.out.
   * println("----------------START UPDATING SITES SUMMARY----------------"
   * +LocalDateTime.now());
   * long startTime = System.currentTimeMillis();
   * List<String> tenants_monitored = new ArrayList<String>();
   * tenants_monitored.add("philippine seven corporation");
   * tenants_monitored.add("ultra mega multi-Sales inc");
   * tenants_monitored.add("dict");
   * for (String tenant_monitored : tenants_monitored){
   * List<String> site_status_monitored = new ArrayList<String>();
   * site_status_monitored.add("active");
   * 
   * Iterable<ElasticSiteSummary> site_loc =
   * elksite_sum_service.get_all_ActiveAndTempClosed(site_status_monitored,
   * tenant_monitored);
   * if(site_loc != null){
   * for (ElasticSiteSummary site_location : site_loc) {
   * try {
   * sites_summary site_sum =
   * site_sum_repo.find_site_by_site_id(site_location.getSite_id());
   * site_sum.setAddress(site_location.getAddress());
   * site_sum.setContact_nos(site_location.getContact_nos());
   * site_sum.setContact_person(site_location.getContact_person());
   * site_sum.setDate_created(Timestamp.valueOf(site_location.getDate_created()));
   * site_sum.setDate_last_updated(Timestamp.valueOf(site_location.
   * getDate_last_updated()));
   * site_sum.setIsland_group(site_location.getIsland_group());
   * site_sum.setName(site_location.getName());
   * site_sum.setRegion(site_location.getRegion());
   * site_sum.setSite_id(site_location.getSite_id());
   * site_sum.setStatus(site_location.getStatus());
   * site_sum.setTenant_name(site_location.getTenant_name());
   * if(site_location.getLinks_up()<1){
   * site_sum.setNetwork_status("DOWN");
   * }else if(site_location.getLinks_up()<0){
   * site_sum.setNetwork_status("UNMONITORED");
   * }else{
   * site_sum.setNetwork_status("UP");
   * }
   * site_sum_repo.save(site_sum);
   * } catch (Exception e) {
   * sites_summary site_sum = new sites_summary();
   * site_sum.setAddress(site_location.getAddress());
   * site_sum.setContact_nos(site_location.getContact_nos());
   * site_sum.setContact_person(site_location.getContact_person());
   * site_sum.setDate_created(Timestamp.valueOf(site_location.getDate_created()));
   * site_sum.setDate_last_updated(Timestamp.valueOf(site_location.
   * getDate_last_updated()));
   * site_sum.setIsland_group(site_location.getIsland_group());
   * site_sum.setName(site_location.getName());
   * site_sum.setRegion(site_location.getRegion());
   * site_sum.setSite_id(site_location.getSite_id());
   * site_sum.setStatus(site_location.getStatus());
   * site_sum.setTenant_name(site_location.getTenant_name());
   * if(site_location.getLinks_up()<1){
   * site_sum.setNetwork_status("DOWN");
   * }else if(site_location.getLinks_up()<0){
   * site_sum.setNetwork_status("UNMONITORED");
   * }else{
   * site_sum.setNetwork_status("UP");
   * }
   * site_sum_repo.save(site_sum);
   * 
   * }
   * Thread.sleep(1000);
   * }
   * }
   * 
   * }
   * 
   * 
   * 
   * long endtime = System.currentTimeMillis();
   * System.out.
   * println("----------------DONE UPDATING SITES SUMMARY----------------Duration: "
   * +(endtime-startTime)+"ms----------------");
   * }
   */

  @Async("asyncExecutor")
  @GetMapping(path = "/assets")
  @Cacheable("product_assets")
  public CompletableFuture<ResponseEntity<Object>> get_productsassets(
      @RequestParam(value = "pageNo", required = false) Integer pageNo,
      @RequestParam(value = "pageSize", required = false) Integer pageSize,
      @RequestParam(value = "site_id", required = false) String site_id) {
    if (pageNo == null) {
      pageNo = 0;
    }
    if (pageSize == null) {
      pageSize = 10;
    }
    return CompletableFuture.completedFuture(
        product_helper.getAssets(pageNo, pageSize, site_id));
  }

  // METHODS FOR MOCK ELASTICSEARCH RESPONSE

  private Object getSiteDetails(String siteId) {
    return "{\"site_id\":\"" + siteId + "\", \"name\":\"Mock Site\", \"location\":\"Mock Location\"}";
  }

  private Object getSiteDetailsPaged(
      String siteId, String tenantName, String siteName, String siteStatus,
      String noLinksUp, String region, String group, String circuitProvider,
      Integer pageSize, Integer pageNo) {
        
    Map<String, Object> result = new HashMap<>();
    result.put("page", pageNo);
    result.put("pageSize", pageSize);
    result.put("sites", Arrays.asList(
        Map.of("site_id", "site1", "site_name", "Mock Site 1", "status", "active"),
        Map.of("site_id", "site2", "site_name", "Mock Site 2", "status", "inactive")));

    return result;
  }

  private Object getSiteDetails(String siteId, String tenantName, String siteName, String siteStatus, String noLinksUp,
      String region, String group, String circuitProvider) {
    return Map.of(
        "sites", Arrays.asList(
            Map.of("site_id", "site1", "site_name", "Mock Site 1", "status", "active"),
            Map.of("site_id", "site2", "site_name", "Mock Site 2", "status", "inactive")));
  }

  private Object getAllSites(String siteId, String tenantName, String siteName, String siteStatus, String noLinksUp,
      String region, String group, String circuitProvider) {
    return Map.of(
        "sites", List.of(
            Map.of("site_id", siteId, "tenant", tenantName, "site_name", siteName, "status", siteStatus, "region",
                region)));
  }

  private Object getSiteCount(String tenantName) {
    return Map.of(
        "site_count", 100 
    );
  }
}
