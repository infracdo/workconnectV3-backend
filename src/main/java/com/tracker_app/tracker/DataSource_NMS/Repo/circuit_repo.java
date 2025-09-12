package com.tracker_app.tracker.DataSource_NMS.Repo;

import com.tracker_app.tracker.DataSource_NMS.Entity.circuit;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

public interface circuit_repo extends CrudRepository<circuit, Long> {
  // DB NMS

  @Query(
    value = "SELECT site_id FROM circuit_type where port2_type = 'WIRELESS' OR vlan10_type = 'WIRELESS'",
    nativeQuery = true
  )
  List<String> getPrimaryWirelessSiteIds();

  @Query(
    value = "SELECT site_id FROM circuit_type where port1_type = 'WIRED' OR cellular_type = 'WIRED'",
    nativeQuery = true
  )
  List<String> getBackupWiredSiteIds();

  @Query(
    value = "SELECT COUNT(site_id) FROM mac_mikrotik WHERE router = 'mikrotik' AND ( port1 = 'SMART SIM' OR port1 = 'GLOBE SIM') AND (port2 = 'SMART SIM' OR port2 = 'GLOBE SIM')",
    nativeQuery = true
  )
  int countProblematicWirelessOnly();

  @Query(
    value = "SELECT site_id FROM mac_mikrotik WHERE router = 'mikrotik' AND ( port1 = 'SMART SIM' OR port1 = 'GLOBE SIM') AND (port2 = 'SMART SIM' OR port2 = 'GLOBE SIM')",
    nativeQuery = true
  )
  List<String> findWirelessOnlyList();

  @Query(
    value = "SELECT COUNT(site_id) FROM mac_mikrotik WHERE router  = 'mikrotik' AND (port1 != 'NO PROVIDER' AND port2 = 'NO PROVIDER') OR (port2 != 'NO PROVIDER' AND port1 = 'NO PROVIDER')",
    nativeQuery = true
  )
  int countProblematicSingleCircuit();

  @Query(
    value = "SELECT site_id FROM mac_mikrotik WHERE router  = 'mikrotik' AND (port1 != 'NO PROVIDER' AND port2 = 'NO PROVIDER') OR (port2 != 'NO PROVIDER' AND port1 = 'NO PROVIDER')",
    nativeQuery = true
  )
  List<String> findSingleCircuitList();

  // @Query(
  //   value = "SELECT COUNT(site_id) FROM mac_mikrotik where port2 = 'NO PROVIDER' OR vlan10 = 'NO PROVIDER'",
  //   nativeQuery = true
  // )
  // int countPrimaryNoProvider();

  // @Query(
  //   value = "SELECT COUNT(site_id) FROM circuit_type where port1 = 'NO PROVIDER' OR vlan10 = 'NO PROVIDER'",
  //   nativeQuery = true
  // )
  // int countBackupNoProvider();
  @Query(
    value = "SELECT COUNT(site_id) FROM mac_mikrotik WHERE router = 'mikrotik'",
    nativeQuery = true
  )
  int countTotalMikrotikStores();

  @Query(
    value = "SELECT COUNT(site_id) FROM mac_ruijie WHERE router = 'ruijie'",
    nativeQuery = true
  )
  int countTotalRuijiStores();

  @Query(
    value = "SELECT COUNT(site_id) FROM mac_mikrotik WHERE port2 = 'NO PROVIDER' AND router = 'mikrotik'",
    nativeQuery = true
  )
  int countPrimaryMikrotikNoProvider();

  @Query(
    value = "SELECT COUNT(site_id) FROM mac_ruijie WHERE vlan10 = 'NO PROVIDER' AND router = 'ruijie'",
    nativeQuery = true
  )
  int countBackupMikrotikNoProvider();

  @Query(
    value = "SELECT COUNT(site_id) FROM mac_mikrotik WHERE port1 = 'NO PROVIDER' AND router = 'mikrotik'",
    nativeQuery = true
  )
  int countPrimaryRuijiNoProvider();

  @Query(
    value = "SELECT COUNT(site_id) FROM mac_ruijie WHERE cellular = '' AND router = 'ruijie'",
    nativeQuery = true
  )
  int countBackupRuijiNoProvider();
}
