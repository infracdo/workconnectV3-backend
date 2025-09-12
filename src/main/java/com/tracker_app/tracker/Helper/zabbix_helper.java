package com.tracker_app.tracker.Helper;

import com.fasterxml.jackson.annotation.JsonValue;
import com.tracker_app.tracker.DataSource_NMS.Repo.provider_circuits_today_repo;
import com.tracker_app.tracker.DataSource_WorkConnect.Entity.site_circuit_status;
import com.tracker_app.tracker.DataSource_WorkConnect.Entity.site_circuit_status_history;
import com.tracker_app.tracker.DataSource_WorkConnect.Entity.site_location;
import com.tracker_app.tracker.DataSource_WorkConnect.Entity.site_tickets;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.site_circuit_status_history_repo;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.site_circuit_status_repo;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.site_location_repo;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.site_tickets_repo;
import com.tracker_app.tracker.DataSource_WorkConnect.Repo.tenant_lookup_repo;
import com.tracker_app.tracker.DataSource_Zabbix.Repo.events_repo;
import com.tracker_app.tracker.DataSource_Zabbix.Repo.hostmacro_repo;
import com.tracker_app.tracker.DataSource_Zabbix.Repo.hosts_repo;
import com.tracker_app.tracker.DataSource_Zabbix.Repo.zabbix_interface_repo;
import com.tracker_app.tracker.DataSource_elasticsearch.model.ElasticSiteSummary;
import com.tracker_app.tracker.DataSource_elasticsearch.service.ElasticSiteSummary_service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class zabbix_helper {

  @Autowired
  private prometheus_helper prometheus_help;

  @Autowired
  private site_tickets_repo ticket_repo;

  @Autowired
  rt_helper_rest1 rt = new rt_helper_rest1();

  @Value("${RT.URL}")
  String rt_url;

  @Autowired
  private hosts_repo hostsRepo;

  @Autowired
  private zabbix_interface_repo interface_repo;

  @Autowired
  private hostmacro_repo macro_repo;

  @Autowired
  private events_repo events_repo;

  @Autowired
  private site_location_repo site_loc_repo;

  @Autowired
  private site_circuit_status_repo site_circuit_stat_repo;

  @Autowired
  private site_circuit_status_history_repo site_circuit_stat_history_repo;

  @Value("${ATIS.URL}")
  private String atis_url;

  @Autowired
  private ElasticSiteSummary_service elksite_sum_service;

  @Autowired
  private atis_helper atishelper;

  @Autowired
  private tenant_lookup_repo tenant_repo;

  @Autowired
  private provider_circuits_today_repo provider_circuits_today_repo;

  // @Transactional("ZabbixTransactionManager")
  // public Object classifySites(String host_name, String month, String year)
  //   throws JSONException, ParseException {
  //   //        ElasticSiteSummary current_site_sum = elksite_sum_service.findBySiteId(host_name);
  //   //        String siteTenant = current_site_sum.getTenant_name();
  //   List<Map<String, Object>> fetchedData = null;
  //   //        List<Integer> host_groupid = new ArrayList<Integer>();

  //   String modified_hostname = host_name;

  //   //        Integer[] host_ids = tenant_repo.get_lookup_by_name(siteTenant).getZabbix_group_id();
  //   //        for (Integer host_id : host_ids){
  //   //            host_groupid.add(host_id);
  //   //        }

  //   //        if(siteTenant.contains("DICT")){
  //   //            modified_hostname = host_name+".";
  //   //        }

  //   //        int a = 0;
  //   //        while(fetchedData == null||fetchedData.isEmpty()) {
  //   //            if((a+1)>host_groupid.size()){
  //   //                return fetchedData;
  //   //            }
  //   //            fetchedData = hostsRepo.fetchPSCSites(modified_hostname,host_groupid.get(a));
  //   //            if(fetchedData.isEmpty()){
  //   //                fetchedData = hostsRepo.fetchSites(modified_hostname,host_groupid.get(a));
  //   //            }
  //   //            System.out.println(a);
  //   //            a++;
  //   //        }
  //   int a = 0;
  //   while (fetchedData == null || fetchedData.isEmpty()) {
  //     fetchedData = hostsRepo.fetchZabbixV6Sites(modified_hostname);
  //     System.out.println(a);
  //     a++;
  //   }

  //   List<Map<String, Object>> sites = new ArrayList<Map<String, Object>>();

  //   TimeZone tz = TimeZone.getTimeZone("Asia/Manila");
  //   SimpleDateFormat originalFormat = new SimpleDateFormat(
  //     "yyyy-MM-dd HH:mm:ss.SS"
  //   );
  //   originalFormat.setTimeZone(tz);
  //   System.out.println("fetchDataSize: " + fetchedData.size());

  //   for (int i = 0; i < fetchedData.size(); i++) {
  //     Map<String, Object> current_data = fetchedData.get(i);
  //     Map<String, Object> obj = new HashMap<String, Object>();

  //     System.out.println(current_data.get("name"));

  //     obj.put("name", current_data.get("name"));
  //     //            try {
  //     //                obj.put("status", current_data.get("poc_2_phone_a"));
  //     //            } catch (Exception e) {
  //     //                System.out.println("no poc_2_phone_a for "+ host_name);
  //     //            }
  //     obj.put("host_id", current_data.get("hostid"));
  //     obj.put("site_id", current_data.get("host"));

  //     Long hostid = Long.parseLong(String.valueOf(current_data.get("hostid")));
  //     Long triggerid = Long.parseLong(
  //       String.valueOf(current_data.get("triggerid"))
  //     );
  //     Date date = new Date((Integer) current_data.get("lastchange") * 1000L);
  //     System.out.println(date);
  //     if ((Integer) current_data.get("priority") == 5) {
  //       obj.put("vip", current_data.get("value"));
  //       obj.put("vip_lastchange", originalFormat.format(date));
  //       obj.put("vip_trigger_id", current_data.get("triggerid"));
  //       obj.put("vip_ip", getStoreVirtualIP(hostid));
  //       obj.put("vip_events", getEvents(triggerid, month, year));
  //     }
  //     if ((Integer) current_data.get("priority") == 4) {
  //       //                if(siteTenant.contains("DICT")){
  //       //                    obj.put("vip", current_data.get("value"));
  //       //                    obj.put("vip_lastchange", originalFormat.format(date));
  //       //                    obj.put("vip_trigger_id", current_data.get("triggerid"));
  //       //                    obj.put("vip_ip", getStoreVirtualIP(hostid));
  //       //                    obj.put("vip_events", getEvents(triggerid));
  //       //                }else{
  //       //                    obj.put("primary", current_data.get("value"));
  //       //                    obj.put("primary_lastchange", originalFormat.format(date));
  //       //                    obj.put("primary_provider", current_data.get("vendor"));
  //       //                    obj.put("primary_trigger_id", current_data.get("triggerid"));
  //       //                    obj.put("primary_ip", getStoreCircuitIP("%IP1%",hostid));
  //       //                    obj.put("primary_events", getEvents(triggerid));
  //       //                }
  //       //                System.out.println(current_data.get("vendor"));
  //       obj.put("primary", current_data.get("value"));
  //       obj.put("primary_lastchange", originalFormat.format(date));
  //       obj.put("primary_provider", current_data.get("vendor"));
  //       obj.put("primary_trigger_id", current_data.get("triggerid"));
  //       obj.put("primary_ip", getStoreCircuitIP("%IP1%", hostid));
  //       obj.put("primary_events", getEvents(triggerid, month, year));
  //     }
  //     if ((Integer) current_data.get("priority") == 3) {
  //       obj.put("backup", current_data.get("value"));
  //       obj.put("backup_lastchange", originalFormat.format(date));
  //       obj.put("backup_provider", current_data.get("chassis"));
  //       obj.put("backup_trigger_id", current_data.get("triggerid"));
  //       obj.put("backup_ip", getStoreCircuitIP("%IP2%", hostid));
  //       obj.put("backup_events", getEvents(triggerid, month, year));
  //       System.out.println(current_data.get("chassis"));
  //     }
  //     if ((Integer) current_data.get("priority") == 2) {
  //       obj.put("thrdcircuit", current_data.get("value"));
  //       obj.put("thrdcircuit_lastchange", originalFormat.format(date));
  //       obj.put("thrdcircuit_provider", current_data.get("model"));
  //       obj.put("thrdcircuit_trigger_id", current_data.get("triggerid"));
  //       obj.put("thrdcircuit_ip", getStoreCircuitIP("%IP3%", hostid));
  //       obj.put("thrdcircuit_events", getEvents(triggerid, month, year));
  //       System.out.println(current_data.get("model"));
  //     }
  //     /*
  //           obj.put("priority",current_data.get("priority"));
  //           obj.put("value", current_data.get("value"));
  //           obj.put("lastchange", current_data.get("lastchange"));
  //           obj.put("provider_primary", current_data.get("vendor"));
  //           obj.put("provider_backup", current_data.get("chassis"));
  //           obj.put("provider_3rdcircuit", current_data.get("model"));
  //           */
  //     sites.add(obj);
  //   }
  //   return sites;
  // }
  @Transactional("ZabbixTransactionManager")
  public Object classifySites(String host_name, String fromDate, String toDate)
    throws JSONException, ParseException {
    List<Map<String, Object>> fetchedData = null;
    List<Map<String, Object>> sites = new ArrayList<>();

    String modified_hostname = host_name;

    // int a = 0;
    // while (fetchedData == null || fetchedData.isEmpty() || a < 500) {
    fetchedData = hostsRepo.fetchZabbixV6Sites(modified_hostname);
    // System.out.println(fetchedData);
    //   System.out.println("Loop iteration: " + a);
    //   a++;
    // }

    TimeZone tz = TimeZone.getTimeZone("Asia/Manila");
    SimpleDateFormat originalFormat = new SimpleDateFormat(
      "yyyy-MM-dd HH:mm:ss.SS"
    );

    originalFormat.setTimeZone(tz);
    // System.out.println("fetchDataSize: " + fetchedData.size());

    for (int i = 0; i < fetchedData.size(); i++) {
      // System.out.println("Zabbix classify sites: " + i);
      Map<String, Object> current_data = fetchedData.get(i);
      // System.out.println("Get Current Data: " + current_data);
      Map<String, Object> obj = new HashMap<>();
      // System.out.println(
      //   "Zabbix events, Current data name: " + current_data.get("name")
      // );

      obj.put("name", current_data.get("name"));
      obj.put("host_id", current_data.get("hostid"));
      obj.put("site_id", current_data.get("host"));

      Long hostid = Long.parseLong(String.valueOf(current_data.get("hostid")));
      Long triggerid = Long.parseLong(
        String.valueOf(current_data.get("triggerid"))
      );
      // System.out.println(triggerid);
      Date date = new Date((Integer) current_data.get("lastchange") * 1000L);
      // System.out.println("Date: " + date);

      // Check if the date falls within the specified range
      // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      // Date fromDateObj = sdf.parse(fromDate);
      // Date toDateObj = sdf.parse(toDate);
      // System.out.println("From date: " + fromDateObj);
      // System.out.println("To date: " + toDateObj);
      // System.out.println(date.after(fromDateObj));
      // System.out.println(date.before(toDateObj));

      if ((Integer) current_data.get("priority") == 5) {
        obj.put("vip", current_data.get("value"));
        obj.put("vip_lastchange", originalFormat.format(date));
        obj.put("vip_trigger_id", current_data.get("triggerid"));
        obj.put("vip_ip", getStoreVirtualIP(hostid));
        obj.put("vip_events", getEvents(triggerid, fromDate, toDate)); // Pass fromDate and toDate here
      }
      if ((Integer) current_data.get("priority") == 4) {
        obj.put("primary", current_data.get("value"));
        obj.put("primary_lastchange", originalFormat.format(date));
        obj.put("primary_provider", current_data.get("vendor"));
        obj.put("primary_trigger_id", current_data.get("triggerid"));
        obj.put("primary_ip", getStoreCircuitIP("%IP1%", hostid));
        obj.put("primary_events", getEvents(triggerid, fromDate, toDate)); // Pass fromDate and toDate here
      }
      if ((Integer) current_data.get("priority") == 3) {
        obj.put("backup", current_data.get("value"));
        obj.put("backup_lastchange", originalFormat.format(date));
        obj.put("backup_provider", current_data.get("chassis"));
        obj.put("backup_trigger_id", current_data.get("triggerid"));
        obj.put("backup_ip", getStoreCircuitIP("%IP2%", hostid));
        obj.put("backup_events", getEvents(triggerid, fromDate, toDate)); // Pass fromDate and toDate here
        // System.out.println(current_data.get("chassis"));
      }
      if ((Integer) current_data.get("priority") == 2) {
        obj.put("thrdcircuit", current_data.get("value"));
        obj.put("thrdcircuit_lastchange", originalFormat.format(date));
        obj.put("thrdcircuit_provider", current_data.get("model"));
        obj.put("thrdcircuit_trigger_id", current_data.get("triggerid"));
        obj.put("thrdcircuit_ip", getStoreCircuitIP("%IP3%", hostid));
        obj.put("thrdcircuit_events", getEvents(triggerid, fromDate, toDate)); // Pass fromDate and toDate here
        // System.out.println(current_data.get("model"));
      }
      sites.add(obj);
    }

    // System.out.println("classify sites" + sites);
    return sites;
  }

  // @Transactional("ZabbixTransactionManager")
  // public Object classifySitesLastChange(String host_name)
  //   throws JSONException, ParseException {
  //   List<Map<String, Object>> fetchedData = null;
  //   Map<String, Object> result = new HashMap<>();
  //   List<Map<String, Object>> sites = new ArrayList<>();

  //   String modified_hostname = host_name;

  //   fetchedData = hostsRepo.fetchZabbixV6Sites(modified_hostname);

  //   TimeZone tz = TimeZone.getTimeZone("Asia/Manila");
  //   SimpleDateFormat originalFormat = new SimpleDateFormat(
  //     "yyyy-MM-dd HH:mm:ss.SS"
  //   );

  //   originalFormat.setTimeZone(tz);

  //   for (int i = 0; i < fetchedData.size(); i++) {
  //     Map<String, Object> current_data = fetchedData.get(i);

  //     Map<String, Object> obj = new HashMap<>();

  //     obj.put("site_id", current_data.get("host"));

  //     Date date = new Date((Integer) current_data.get("lastchange") * 1000L);

  //     if ((Integer) current_data.get("priority") == 5) {
  //       obj.put("vip", current_data.get("value"));
  //       obj.put("vip_lastchange", originalFormat.format(date));
  //     }
  //     if ((Integer) current_data.get("priority") == 4) {
  //       obj.put("primary", current_data.get("value"));
  //       obj.put("primary_lastchange", originalFormat.format(date));
  //     }
  //     if ((Integer) current_data.get("priority") == 3) {
  //       obj.put("backup", current_data.get("value"));
  //       obj.put("backup_lastchange", originalFormat.format(date));
  //     }
  //     if ((Integer) current_data.get("priority") == 2) {
  //       obj.put("thrdcircuit", current_data.get("value"));
  //       obj.put("thrdcircuit_lastchange", originalFormat.format(date));
  //     }
  //     sites.add(obj);
  //   }

  //   result.put("sites", sites);
  //   return sites;
  // }
  // @Transactional("ZabbixTransactionManager")
  // public Map<String, Object> classifySitesLastChange(List<String> storeIds)
  //   throws JSONException, ParseException {
  //   List<Map<String, Object>> fetchedData = null;

  //   // String modified_hostname = host_name;

  //   fetchedData = hostsRepo.fetchZabbixSites(storeIds);

  //   TimeZone tz = TimeZone.getTimeZone("Asia/Manila");
  //   SimpleDateFormat originalFormat = new SimpleDateFormat(
  //     "yyyy-MM-dd HH:mm:ss.SS"
  //   );

  //   originalFormat.setTimeZone(tz);

  //   Map<String, Object> obj = new HashMap<>();

  //   for (int i = 0; i < fetchedData.size(); i++) {
  //     Map<String, Object> current_data = fetchedData.get(i);

  //     obj.put("site_id", current_data.get("host"));

  //     Date date = new Date((Integer) current_data.get("lastchange") * 1000L);

  //     if ((Integer) current_data.get("priority") == 4) {
  //       obj.put("primary", current_data.get("value"));
  //       obj.put("primary_lastchange", originalFormat.format(date));
  //     }
  //     if ((Integer) current_data.get("priority") == 3) {
  //       obj.put("backup", current_data.get("value"));
  //       obj.put("backup_lastchange", originalFormat.format(date));
  //     }
  //     if ((Integer) current_data.get("priority") == 2) {
  //       obj.put("thrdcircuit", current_data.get("value"));
  //       obj.put("thrdcircuit_lastchange", originalFormat.format(date));
  //     }
  //   }

  //   return obj;
  // }
  @Transactional("ZabbixTransactionManager")
  public List<Map<String, Object>> classifySitesLastChange(
    List<String> storeIds,
    String circuit
  ) throws JSONException, ParseException {
    List<Map<String, Object>> fetchedData = null;
    if (circuit.equals("primary")) {
      fetchedData = hostsRepo.fetchZabbixSitesPrimary(storeIds);
    }
    if (circuit.equals("backup")) {
      fetchedData = hostsRepo.fetchZabbixSitesBackup(storeIds);
    }
    TimeZone tz = TimeZone.getTimeZone("Asia/Manila");
    SimpleDateFormat originalFormat = new SimpleDateFormat(
      "yyyy-MM-dd HH:mm:ss.SS"
    );
    originalFormat.setTimeZone(tz);

    List<Map<String, Object>> resultList = new ArrayList<>();

    for (Map<String, Object> data : fetchedData) {
      Map<String, Object> obj = new HashMap<>();
      String storeId = (String) data.get("host");

      obj.put("site_id", storeId);
      int priority = (int) data.get("priority");
      String circuitType = (priority == 4)
        ? "Primary"
        : (priority == 3) ? "Backup" : "Unknown";
      obj.put("circuitType", circuitType);

      obj.put("lastchange", data.get("lastchange"));

      resultList.add(obj);
    }

    return resultList;
  }

  @Transactional("ZabbixTransactionManager")
  public String getStoreVirtualIP(Long hostid) {
    return interface_repo.getStoreVIP(hostid);
  }

  @Transactional("ZabbixTransactionManager")
  public String getStoreCircuitIP(String key, Long hostid) {
    return macro_repo.getStoreCircuitIP(key, hostid);
  }

  @Transactional("ZabbixTransactionManager")
  public List<Map<String, Object>> getZabbixReport(
    String storeId,
    String fromDate,
    String toDate
  ) throws ParseException {
    List<Map<String, Object>> sites = new ArrayList<Map<String, Object>>();

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));

    // Parse the fromDate and toDate strings into Date objects
    Date fromDateObj = dateFormat.parse(fromDate + " 00:00:00");
    Date toDateObj = dateFormat.parse(toDate + " 23:59:59");

    // Convert Date objects to epoch timestamps in seconds
    long fromDateEpoch = fromDateObj.getTime() / 1000;
    long toDateEpoch = toDateObj.getTime() / 1000;

    sites = hostsRepo.get_downtime_report(storeId, fromDateEpoch, toDateEpoch);
    return sites;
  }

  // @Transactional("ZabbixTransactionManager")
  // public Object getEvents(Long triggerid, String month, String year)
  //   throws ParseException {
  //   List<Map<String, Object>> events = new ArrayList<Map<String, Object>>();
  //   List<Map<String, Object>> fetchedEvents = events_repo.getevents(
  //     triggerid,
  //     month,
  //     year
  //   );

  //   /*
  //       String[] timezones = TimeZone.getAvailableIDs();
  //       for (String string : timezones) {
  //           System.out.println(string);
  //       }
  //       */
  //   System.out.println(fetchedEvents.size());
  //   for (Map<String, Object> event : fetchedEvents) {
  //     Map<String, Object> event_data = new HashMap<String, Object>();
  //     //SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  //     TimeZone tz = TimeZone.getTimeZone("Asia/Manila");
  //     SimpleDateFormat originalFormat = new SimpleDateFormat(
  //       "yyyy-MM-dd HH:mm:ss.SS"
  //     );
  //     originalFormat.setTimeZone(tz);

  //     Long eventid = Long.parseLong(String.valueOf(event.get("eventid")));
  //     event_data.put("eventid", eventid);

  //     //Integer end = Integer.parseInt(String.valueOf(event.get("clock")));
  //     Date date = new Date((Integer) event.get("clock") * 1000L);
  //     event_data.put("end", originalFormat.format(date));

  //     Integer start = events_repo.getEventsEventRecovery(eventid);
  //     if (start == null) {
  //       event_data.put("start", "NA");
  //       continue;
  //     }
  //     date = new Date(start * 1000L);
  //     event_data.put("start", originalFormat.format(date));

  //     events.add(event_data);
  //   }

  //   return events;
  // }
  public Object getEvents(Long triggerid, String fromDate, String toDate)
    throws ParseException {
    List<Map<String, Object>> events = new ArrayList<Map<String, Object>>();
    // System.out.println(fromDate + " " + toDate);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));

    // Parse the fromDate and toDate strings into Date objects
    Date fromDateObj = dateFormat.parse(fromDate + " 00:00:00");
    Date toDateObj = dateFormat.parse(toDate + " 23:59:59");

    // Convert Date objects to epoch timestamps in seconds
    long fromDateEpoch = fromDateObj.getTime() / 1000;
    long toDateEpoch = toDateObj.getTime() / 1000;

    // System.out.println(fromDateEpoch + " " + toDateEpoch);
    List<Map<String, Object>> fetchedEvents = events_repo.getEventsInRange(
      triggerid,
      fromDateEpoch,
      toDateEpoch
    );

    // System.out.println(fetchedEvents.size());
    for (Map<String, Object> event : fetchedEvents) {
      Map<String, Object> event_data = new HashMap<String, Object>();
      TimeZone tz = TimeZone.getTimeZone("Asia/Manila");
      SimpleDateFormat originalFormat = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss.SS"
      );
      originalFormat.setTimeZone(tz);

      Long eventid = Long.parseLong(String.valueOf(event.get("eventid")));
      event_data.put("eventid", eventid);

      Date date = new Date((Integer) event.get("clock") * 1000L);
      event_data.put("end", originalFormat.format(date));

      Integer start = events_repo.getEventsEventRecovery(eventid);
      if (start == null) {
        event_data.put("start", "NA");
        continue;
      }
      date = new Date(start * 1000L);
      event_data.put("start", originalFormat.format(date));

      events.add(event_data);
    }
    // System.out.println("get events " + events);
    return events;
  }

  public String getSiteIP(String site_id) {
    List<Map<String, Object>> fetchedData = null;
    List<Integer> host_groupid = new ArrayList<Integer>();
    host_groupid.add(113);
    host_groupid.add(59);
    host_groupid.add(93);
    host_groupid.add(95);
    host_groupid.add(99);
    host_groupid.add(66);
    host_groupid.add(112);
    host_groupid.add(47);
    host_groupid.add(19);
    host_groupid.add(21);
    host_groupid.add(29);
    host_groupid.add(28);
    host_groupid.add(27);
    host_groupid.add(24);
    host_groupid.add(23);
    host_groupid.add(31);
    host_groupid.add(32);
    host_groupid.add(30);
    host_groupid.add(34);
    host_groupid.add(35);
    host_groupid.add(36);
    host_groupid.add(37);
    host_groupid.add(38);
    host_groupid.add(40);
    host_groupid.add(41);
    host_groupid.add(39);
    host_groupid.add(42);

    int a = 0;
    System.out.println(site_id);
    while (fetchedData == null || fetchedData.isEmpty()) {
      if ((a + 1) > host_groupid.size()) {
        return "Site not found in zabbix";
      }
      int group_id = host_groupid.get(a);
      fetchedData = hostsRepo.fetchSites(site_id, group_id);
      if (fetchedData.isEmpty()) {
        fetchedData = hostsRepo.fetchPSCSites(site_id, group_id);
      }
      a = a + 1;
    }

    System.out.println(host_groupid.get(a));
    System.out.println(fetchedData.size());
    String ip = null;
    for (int i = 0; i < fetchedData.size(); i++) {
      //System.out.println("Updating: "+fetchedData.get(i).get("priority"));
      //System.out.println("Updating: "+fetchedData.get(i).get("value"));
      //Long hostid = Long.parseLong(String.valueOf(fetchedData.get(i).get("hostid")));
      //System.out.println("Updating: "+ getStoreVirtualIP(hostid));
      Long hostid = Long.parseLong(
        String.valueOf(fetchedData.get(i).get("hostid"))
      );
      try {
        System.out.println("Updating: " + getStoreVirtualIP(hostid));
        if (
          (!getStoreVirtualIP(hostid).isEmpty()) &&
          (!getStoreVirtualIP(hostid).isBlank())
        ) {
          ip = getStoreVirtualIP(hostid);
        }
      } catch (Exception e) {
        System.out.println("Error!!! zabbix cant find ip" + site_id);
      }
    }

    return ip;
  }

  public Integer UpdateDICTNetworkStatus(String site_id)
    throws InterruptedException {
    List<Map<String, Object>> fetchedData = null;
    List<Integer> host_groupid = new ArrayList<Integer>();
    host_groupid.add(113);
    host_groupid.add(59);
    host_groupid.add(93);
    host_groupid.add(95);
    host_groupid.add(99);
    host_groupid.add(66);
    host_groupid.add(112);
    host_groupid.add(47);
    host_groupid.add(19);
    host_groupid.add(21);
    host_groupid.add(29);
    host_groupid.add(28);
    host_groupid.add(27);
    host_groupid.add(24);
    host_groupid.add(23);
    host_groupid.add(31);
    host_groupid.add(32);
    host_groupid.add(30);
    host_groupid.add(34);
    host_groupid.add(35);
    host_groupid.add(36);
    host_groupid.add(37);
    host_groupid.add(38);
    host_groupid.add(40);
    host_groupid.add(41);
    host_groupid.add(39);
    host_groupid.add(42);

    int a = 0;
    System.out.println(site_id);
    while (fetchedData == null || fetchedData.isEmpty()) {
      if ((a + 1) > host_groupid.size()) {
        return 0;
      }
      int group_id = host_groupid.get(a);
      fetchedData = hostsRepo.fetchSites(site_id, group_id);
      if (fetchedData.isEmpty()) {
        fetchedData = hostsRepo.fetchPSCSites(site_id, group_id);
      }
      a = a + 1;
    }

    System.out.println(host_groupid.get(a));
    System.out.println(fetchedData.size());
    for (int i = 0; i < fetchedData.size(); i++) {
      //System.out.println("Updating: "+fetchedData.get(i).get("priority"));
      //System.out.println("Updating: "+fetchedData.get(i).get("value"));
      //Long hostid = Long.parseLong(String.valueOf(fetchedData.get(i).get("hostid")));
      //System.out.println("Updating: "+ getStoreVirtualIP(hostid));
      Long hostid = Long.parseLong(
        String.valueOf(fetchedData.get(i).get("hostid"))
      );
      try {
        System.out.println("Updating: " + getStoreVirtualIP(hostid));
      } catch (Exception e) {}

      if ((Integer) fetchedData.get(i).get("priority") == 4) {
        if ((Integer) fetchedData.get(i).get("value") == 0) {
          return 1;
        } else {
          return 0;
        }
      }
    }

    return null;
  }

  public Integer update_link_status(ElasticSiteSummary site)
    throws IOException, JSONException, InterruptedException {
    String site_id = site.getSite_id();
    System.out.println("Site ID FUUUUUUUUk");
    Integer links_up = 0;
    Integer stat0 = 0;
    if (site.getTenant_name().contains("Ultra Mega Multi-Sales Inc")) {
      Map<String, Object> object0 = prometheus_help.get_circuit_status(
        "ultramega_tcp",
        site_id
      );
      String ip0 = object0.get("ip").toString();
      stat0 = Integer.parseInt(object0.get("stat").toString());
      if (stat0 == 0) {
        stat0 = Integer.parseInt(object0.get("stat").toString());
      }
      update_site_link(
        "LOOPBACK 0",
        site_id,
        "NONE",
        stat0,
        ip0,
        "loopback 0",
        ""
      );
    } /*
        else if(site.getTenant_name().contains("DICT")){
            Map<String,Object> object0 = prometheus_help.get_circuit_status("sma2_sites", site_id);
            String ip0 = object0.get("ip").toString();
            stat0 = Integer.parseInt(object0.get("stat").toString());
            if(stat0==0){
                stat0 = Integer.parseInt(object0.get("stat").toString());
            }
            update_site_link("LOOPBACK 0", site_id, "NONE", stat0, ip0, "loopback 0","");
        }
        */else {
      Map<String, Object> object0 = prometheus_help.get_circuit_status(
        "loopback 0",
        site_id
      );
      String ip0 = object0.get("ip").toString();
      stat0 = Integer.parseInt(object0.get("stat").toString());
      if (stat0 == 0) {
        stat0 = Integer.parseInt(object0.get("stat").toString());
      }
      try {
        site.setStatus(object0.get("site_status").toString());
        elksite_sum_service.save(site);
      } catch (Exception e) {
        e.printStackTrace();
      }

      update_site_link(
        "LOOPBACK 0",
        site_id,
        "NONE",
        stat0,
        ip0,
        "loopback 0",
        ""
      );
    }

    if (site.getTenant_name().contains("DICT")) {
      return stat0;
    }

    for (int i = 1; i < 4; i++) {
      String circuit_type = null;
      String vendor = null;

      if (i == 1) {
        circuit_type = "Primary";
      } else if (i == 2) {
        circuit_type = "Backup";
      } else if (i == 3) {
        circuit_type = "ThirdCircuit";
      } else {
        continue;
      }
      vendor = "";

      Map<String, Object> object = prometheus_help.get_circuit_status(
        "loopback " + i,
        site_id
      );
      String ip = object.get("ip").toString();
      Integer stat = Integer.parseInt(object.get("stat").toString());
      if (stat == 0) {
        object = prometheus_help.get_circuit_status("loopback " + i, site_id);
        ip = object.get("ip").toString();
        stat = Integer.parseInt(object.get("stat").toString());
      }
      links_up = links_up + stat;
      update_site_link(
        circuit_type,
        site_id,
        vendor,
        stat,
        ip,
        "loopback " + i,
        ""
      );
    }

    //        Object obj_circuits = atishelper.get_site_circuits(atis_url, site_id);
    //        JSONArray circuits = new JSONArray(obj_circuits.toString());
    //        if(circuits.length()<1){
    //            obj_circuits = atishelper.get_site_interface(atis_url, site_id);
    //            circuits = new JSONArray(obj_circuits.toString());
    //            for (int i = 0; i < circuits.length(); i++){
    //                JSONObject circuit = circuits.getJSONObject(i);
    //                String circuit_type=null;
    //                String vendor = null;
    //
    //                if(circuit.getString("name").contains("loopback 1")){
    //                    circuit_type = "Primary";
    //                }
    //                else if(circuit.getString("name").contains("loopback 2")){
    //                    circuit_type = "Backup";
    //                }
    //                else if(circuit.getString("name").contains("loopback 3")){
    //                    circuit_type = "ThirdCircuit";
    //                }
    //                else{
    //                    continue;
    //                }
    //                vendor = circuit.getString("provider");
    //
    //                Map<String,Object> object = prometheus_help.get_circuit_status(circuit.getString("name"), site_id);
    //                String ip = object.get("ip").toString();
    //                Integer stat = Integer.parseInt(object.get("stat").toString());
    //                if(stat == 0){
    //                    object = prometheus_help.get_circuit_status(circuit.getString("name"), site_id);
    //                    ip = object.get("ip").toString();
    //                    stat = Integer.parseInt(object.get("stat").toString());
    //                }
    //                links_up = links_up + stat;
    //                update_site_link(circuit_type, site_id, vendor, stat, ip, circuit.getString("name"),circuit.getString("account_number"));
    //            }
    //        }
    //        else{
    //            for (int i = 1; i < 4; i++){
    //                JSONObject circuit = circuits.getJSONObject(i);
    //                String circuit_type=null;
    //                String vendor = null;
    //
    //                if(circuit.getInt("loopback")==1){
    //                    circuit_type = "Primary";
    //                }
    //                else if(circuit.getInt("loopback")==2){
    //                    circuit_type = "Backup";
    //                }
    //                else if(circuit.getInt("loopback")==3){
    //                    circuit_type = "ThirdCircuit";
    //                }
    //                else{
    //                    continue;
    //                }
    //                vendor = circuit.getString("name");
    //
    //                Map<String,Object> object = prometheus_help.get_circuit_status("loopback "+circuit.getInt("loopback"), site_id);
    //                String ip = object.get("ip").toString();
    //                Integer stat = Integer.parseInt(object.get("stat").toString());
    //                if(stat == 0){
    //                    object = prometheus_help.get_circuit_status("loopback "+circuit.getInt("loopback"), site_id);
    //                    ip = object.get("ip").toString();
    //                    stat = Integer.parseInt(object.get("stat").toString());
    //                }
    //                links_up = links_up + stat;
    //                update_site_link(circuit_type, site_id, vendor, stat, ip, "loopback "+circuit.getInt("loopback"),circuit.getString("account_number"));
    //            }
    //        }

    if (links_up < 1) {
      links_up = links_up + stat0;
    } else {
      links_up = links_up * stat0;
    }
    return links_up;
  }

  public void update_site_link(
    String circuit_type,
    String site_id,
    String vendor,
    Integer status,
    String ip,
    String loopback,
    String account_number
  ) {
    if (vendor == null || vendor.isEmpty() || vendor.isBlank()) {
      vendor = "undefined provider";
    }
    String uptime = "0";
    try {
      uptime = prometheus_help.getUptimeAvailability(site_id, loopback);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      site_circuit_status circuit = site_circuit_stat_repo.find_circuit_by_site_id(
        site_id,
        circuit_type
      );
      if (status == 1) {
        circuit.setCircuit_status("UP");
      } else {
        circuit.setCircuit_status("DOWN");
      }
      circuit.setCircuit_provider(vendor.toUpperCase());
      circuit.setCircuit_ip(ip);
      circuit.setCircuit_account_number(account_number);
      circuit.setCircuit_uptime(uptime);
      site_circuit_stat_repo.save(circuit);
      try {
        get_circuit_downtime(circuit, site_id, loopback, status);
      } catch (
        NumberFormatException
        | IOException
        | InterruptedException
        | JSONException
        | ParseException e1
      ) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    } catch (NullPointerException e) {
      e.printStackTrace();

      site_circuit_status circuit = new site_circuit_status();
      circuit.setSite_id(site_id);
      circuit.setCircuit_type(circuit_type);
      if (status == 1) {
        circuit.setCircuit_status("UP");
      } else {
        circuit.setCircuit_status("DOWN");
      }
      circuit.setMinutes_down(Long.valueOf(0));
      circuit.setCircuit_ip(ip);
      circuit.setCircuit_provider(vendor.toUpperCase());
      circuit.setCircuit_account_number(account_number);
      circuit.setCircuit_uptime(uptime);
      site_circuit_stat_repo.save(circuit);
      //return circuit;
      try {
        get_circuit_downtime(circuit, site_id, loopback, status);
      } catch (
        NumberFormatException
        | IOException
        | InterruptedException
        | JSONException
        | ParseException e1
      ) {
        // TODO Auto-generated catch block
        System.out.println(circuit.getSite_id() + ": " + e1.getMessage());
      }
    }
  }

  public void get_circuit_downtime(
    site_circuit_status circuit,
    String site_id,
    String loopback,
    Integer status
  )
    throws NumberFormatException, IOException, InterruptedException, JSONException, ParseException {
    //long startTime = System.currentTimeMillis();
    try {
      LocalDate cur_date = LocalDate.now();
      List<Map<String, Object>> history = new ArrayList<Map<String, Object>>();
      try {
        history =
          prometheus_help.get_historical_status_list(
            site_id,
            loopback,
            cur_date.toString(),
            Long.valueOf("1")
          );
      } catch (Exception e) {
        history =
          prometheus_help.get_historical_status_list(
            site_id,
            loopback,
            cur_date.toString(),
            Long.valueOf("1")
          );
        //System.out.println("EMPTY HISTORY");
      }
      if (status == 0) {
        Integer down_time = 0;
        String cur_date_data = null;
        for (Map<String, Object> map : history) {
          Integer circuit_stat = Integer.parseInt(map.get("status").toString());
          cur_date_data = map.get("date").toString();
          String last_updated = circuit.getLast_updated();

          SimpleDateFormat df = new SimpleDateFormat(
            "MMM dd yyyy HH:mm:ss.SSS zzz"
          );
          df.setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")));

          Integer interval = df
            .parse(cur_date_data)
            .compareTo(df.parse(last_updated));

          if (last_updated == null) {
            if (circuit_stat == 1) {
              down_time = 0;
            } else {
              down_time = down_time + 1;
            }
          } else {
            if (interval > 0) {
              if (circuit_stat == 1) {
                down_time = 0;
              } else {
                down_time = down_time + 1;
              }
            }
          }
          //System.out.println("Circuit Status: " + circuit_stat);
        }

        if (down_time > 0) {
          circuit.setMinutes_down(
            circuit.getMinutes_down() + (down_time.longValue() * 5)
          );
        } else {
          circuit.setMinutes_down(Long.valueOf("0"));
        }
        circuit.setLast_updated(cur_date_data);
        site_circuit_stat_repo.save(circuit);
      } else {
        circuit.setMinutes_down(Long.valueOf("0"));
        circuit.setLast_updated(
          history.get(history.size() - 1).get("date").toString()
        );
        site_circuit_stat_repo.save(circuit);
      }
    } catch (Exception e) {
      //System.out.println("ERROR "+site_id+": "+e.getMessage());
      //e.printStackTrace();
    }
    //long endtime = System.currentTimeMillis();
    //System.out.println("----------------DONE UPDATING SITES LINK DOWN TIME----------------Duration: "+(endtime-startTime)+"ms----------------");
  }

  public void update_site_openAndnew_tickets() {
    long startTime = System.currentTimeMillis();
    try {
      List<Map<String, Object>> tickets = new ArrayList<Map<String, Object>>();
      tickets =
        rt.get_site_ticket_url(
          rt_url,
          "Telco Escalation | Site ID",
          "Telco Escalations"
        );
      ticket_repo.deleteAll();
      for (Map<String, Object> ticket : tickets) {
        Long ticket_id = Long.valueOf(
          ticket.get("id").toString().split("/")[1]
        );
        String ticket_url = ticket.get("link").toString();
        String site_id = ticket.get("Site ID").toString().trim();
        try {
          site_tickets ticketz = ticket_repo.find_by_ticket_id(
            site_id,
            ticket_id
          );
          ticketz.setId(ticket_id);
          ticketz.setSite_id(site_id);
          ticketz.setRt_url(ticket_url);
          ticket_repo.save(ticketz);
        } catch (Exception e) {
          site_tickets ticketz = new site_tickets();
          ticketz.setId(ticket_id);
          ticketz.setSite_id(site_id);
          ticketz.setRt_url(ticket_url);
          ticket_repo.save(ticketz);
        }
      }
    } catch (Exception e) {
      //e.printStackTrace();
    }
    long endtime = System.currentTimeMillis();
    System.out.println(
      "----------------DONE UPDATING SITES TICKETS----------------Duration: " +
      (endtime - startTime) +
      "ms----------------"
    );
  }
}
