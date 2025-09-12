package com.tracker_app.tracker.DataSource_elasticsearch.service;

import com.tracker_app.tracker.DataSource_Atis.Entity.sites_site;
import com.tracker_app.tracker.DataSource_Atis.Repo.atis_repo;
import com.tracker_app.tracker.DataSource_NMS.Entity.mikrotik;
import com.tracker_app.tracker.DataSource_NMS.Entity.ruijie;
import com.tracker_app.tracker.DataSource_NMS.Repo.circuit_repo;
import com.tracker_app.tracker.DataSource_NMS.Repo.mikrotik_repo;
import com.tracker_app.tracker.DataSource_NMS.Repo.ruijie_repo;
import com.tracker_app.tracker.DataSource_WorkConnect.Entity.site_circuit_status;
import com.tracker_app.tracker.DataSource_elasticsearch.model.ElasticSiteSummary;
import com.tracker_app.tracker.DataSource_elasticsearch.repo.ElasticSiteSummary_repository;
import com.tracker_app.tracker.Helper.atis_helper;
import com.tracker_app.tracker.Helper.prometheus_helper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ElasticSiteSummary_service {

  private final ElasticSiteSummary_repository repository;

  @Value("${ATIS.URL}")
  private String atis_url;

  @Autowired
  private atis_helper atishelper;

  @Autowired
  private prometheus_helper prometheus_help;

  @Autowired
  private circuit_repo circuit_repository;

  @Autowired
  private mikrotik_repo mikrotik_repo;

  @Autowired
  private ruijie_repo ruijie_repo;

  @Autowired
  private atis_repo atis_repo;

  @Autowired
  public ElasticSiteSummary_service(ElasticSiteSummary_repository repository) {
    this.repository = repository;
  }

  public void save(final ElasticSiteSummary site) {
    repository.save(site);
  }

  public ElasticSiteSummary findById(final String id) {
    return repository.findById(id).orElse(null);
  }

  public ElasticSiteSummary findBySiteId(String site_id) {
    return repository.findBySiteID(site_id);
  }

  public Iterable<ElasticSiteSummary> get_all() {
    return repository.findAll();
  }

  public Iterable<ElasticSiteSummary> get_all_ActiveAndTempClosed(
    List<String> status,
    String tenant
  ) {
    BoolQueryBuilder query = QueryBuilders.boolQuery();

    BoolQueryBuilder links_query = QueryBuilders.boolQuery();
    // links_query.should().add(QueryBuilders.matchQuery("status","*active*").operator(Operator.AND));
    // links_query.should().add(QueryBuilders.matchQuery("status","*temporarily_closed*").operator(Operator.AND));
    // links_query.should().add(QueryBuilders.matchQuery("status","*shipped*").operator(Operator.AND));
    for (String string : status) {
      links_query
        .should()
        .add(
          QueryBuilders
            .matchQuery("status", "*" + string + "*")
            .operator(Operator.AND)
        );
    }
    query.must().add(links_query);

    BoolQueryBuilder tenant_query = QueryBuilders.boolQuery();
    // tenant_query.should().add(QueryBuilders.matchQuery("tenant_name","*philippine
    // seven corporation*").operator(Operator.AND));
    // tenant_query.should().add(QueryBuilders.matchQuery("tenant_name","*ultra mega
    // multi-Sales inc*").operator(Operator.AND));
    // tenant_query.should().add(QueryBuilders.matchQuery("tenant_name","*dict*").operator(Operator.AND));
    tenant_query
      .should()
      .add(
        QueryBuilders
          .matchQuery("tenant_name", "*" + tenant + "*")
          .operator(Operator.AND)
      );
    query.must().add(tenant_query);

    System.out.println(query);

    Iterable<ElasticSiteSummary> results = repository.search(query);
    return results;
  }

  public Object findSites(
    String site_id,
    String tenant,
    String name,
    String status,
    String links_up,
    String region,
    String group,
    String circuit_provider
  ) throws IOException, InterruptedException {
    BoolQueryBuilder query = QueryBuilders.boolQuery();
    if (site_id != null) {
      String[] segment = site_id.split("-");
      BoolQueryBuilder site_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        site_query
          .must()
          .add(new WildcardQueryBuilder("site_id", "*" + string + "*"));
      }
      query.must().add(site_query);
    }
    if (tenant != null) {
      String[] segment = tenant.split(",");
      BoolQueryBuilder tenant_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        tenant_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("tenant_name", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(tenant_query);
      // query.must().add(QueryBuilders.matchQuery("tenant_name",
      // "*"+tenant+"*").operator(Operator.AND));
    }
    if (name != null) {
      String[] segment = name.split(" ");
      BoolQueryBuilder name_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        name_query
          .must()
          .add(new WildcardQueryBuilder("name", "*" + string + "*"));
      }
      query.must().add(name_query);
      // query.must().add(QueryBuilders.matchQuery("name",
      // "*"+name+"*").operator(Operator.OR));
      // query.must().add(new WildcardQueryBuilder("name", "*"+name+"*"));
    }
    if (status != null) {
      String[] segment = status.split(",");
      BoolQueryBuilder status_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        status_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("status", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(status_query);
      // query.must().add(QueryBuilders.matchQuery("status","*"+status+"*").operator(Operator.AND));
    }
    if (links_up != null) {
      // query.must().add(QueryBuilders.matchQuery("links_up",links_up).operator(Operator.AND));

      if (links_up.contains("up")) {
        BoolQueryBuilder links_query = QueryBuilders.boolQuery();
        links_query
          .should()
          .add(QueryBuilders.matchQuery("links_up", 1).operator(Operator.AND));
        links_query
          .should()
          .add(QueryBuilders.matchQuery("links_up", 2).operator(Operator.AND));
        links_query
          .should()
          .add(QueryBuilders.matchQuery("links_up", 3).operator(Operator.AND));
        query.must().add(links_query);
      }
      if (links_up.contains("down")) {
        query
          .must()
          .add(QueryBuilders.matchQuery("links_up", 0).operator(Operator.AND));
      }
      if (links_up.contains("unmonitored")) {
        query
          .must()
          .add(QueryBuilders.matchQuery("links_up", -1).operator(Operator.AND));
      }
    }
    if (region != null) {
      String[] segment = region.split(",");
      BoolQueryBuilder region_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        region_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("region", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(region_query);
      // query.must().add(QueryBuilders.matchQuery("region","*"+region+"*").operator(Operator.AND));
    }
    if (group != null) {
      String[] segment = group.split(",");
      BoolQueryBuilder group_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        group_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("island_group", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(group_query);
      // query.must().add(QueryBuilders.matchQuery("island_group","*"+group+"*").operator(Operator.AND));
    }
    if (circuit_provider != null) {
      String[] segment = circuit_provider.split(",");
      BoolQueryBuilder circuit_provider_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        circuit_provider_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("circuits.circuit_provider", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(circuit_provider_query);
      // query.must().add(QueryBuilders.matchQuery("circuits.circuit_provider","*"+circuit_provider+"*").operator(Operator.AND));
    }
    // if(circuit_provider!=null){
    // query.must().add(QueryBuilders.matchQuery("circuits.circuit_provider",circuit_provider));
    // }

    System.out.println(query.toString());
    Page<ElasticSiteSummary> result = (Page<ElasticSiteSummary>) repository.search(
      query
    );
    List<ElasticSiteSummary> resultList = result.getContent();
    List<Map<String, Object>> mapped_result = new ArrayList<Map<String, Object>>();
    for (ElasticSiteSummary summary : resultList) {
      try {
        // Object interfaces = atishelper.get_site_interface(atis_url,
        // summary.getSite_id());
        // JSONArray interface_arr = new JSONArray(interfaces.toString());
        Map<String, Object> obj = new HashMap<String, Object>();
        List<site_circuit_status> circuits = summary.getCircuits();
        if (circuits != null) {
          for (site_circuit_status circuit : circuits) {
            obj.put("site_id", summary.getSite_id());
            obj.put("name", summary.getName());
            obj.put("island_group", summary.getIsland_group());
            obj.put("address", summary.getAddress());
            obj.put("tenant_name", summary.getTenant_name());
            obj.put("region", summary.getRegion());
            obj.put("status", summary.getStatus());
            Integer number_of_links = summary.getLinks_up();
            if (number_of_links > 0) {
              obj.put("network_status", "up");
            } else {
              obj.put("network_status", "down");
            }
            obj.put("circuit_name", circuit.getCircuit_type());
            obj.put("circuit_ip", circuit.getCircuit_ip());
            obj.put("circuit_provider", circuit.getCircuit_provider());
            obj.put("circuit_account", circuit.getCircuit_account_number());
            /*
             * if(circuit.getCircuit_type().contains("Primary")){
             * for(int i=0; i<interface_arr.length(); i++) {
             * JSONObject int_objec = interface_arr.getJSONObject(i);
             * if(int_objec.getString("name").contains("loopback 1")){
             * obj.put("circuit_type",int_objec.getString("type"));
             * }else continue;
             * }
             * }
             * else if(circuit.getCircuit_type().contains("Backup")){
             * for(int i=0; i<interface_arr.length(); i++) {
             * JSONObject int_objec = interface_arr.getJSONObject(i);
             * if(int_objec.getString("name").contains("loopback 2")){
             * obj.put("circuit_type",int_objec.getString("type"));
             * }else continue;
             * }
             * }
             * else if(circuit.getCircuit_type().contains("ThirdCircuit")){
             * for(int i=0; i<interface_arr.length(); i++) {
             * JSONObject int_objec = interface_arr.getJSONObject(i);
             * if(int_objec.getString("name").contains("loopback 3")){
             * obj.put("circuit_type",int_objec.getString("type"));
             * }else continue;
             * }
             * }
             * else{
             * obj.put("circuit_type","virtual");
             * }
             */

            mapped_result.add(obj);
          }
        } else {
          obj.put("site_id", summary.getSite_id());
          obj.put("name", summary.getName());
          obj.put("island_group", summary.getIsland_group());
          obj.put("address", summary.getAddress());
          obj.put("tenant_name", summary.getTenant_name());
          obj.put("region", summary.getRegion());
          obj.put("status", summary.getStatus());
          obj.put("number_of_links", summary.getLinks_up());
          Integer number_of_links = summary.getLinks_up();
          if (number_of_links > 0) {
            obj.put("network_status", "up");
          } else if (number_of_links < 0) {
            obj.put("network_status", "unmonitored");
          } else {
            obj.put("network_status", "down");
          }
          obj.put("circuit_name", "");
          obj.put("circuit_ip", "");
          obj.put("circuit_provider", "");
          obj.put("circuit_account", "");

          mapped_result.add(obj);
        }
      } catch (Exception e) {
        continue;
      }
    }
    return mapped_result;
  }

  public Page<ElasticSiteSummary> findSitesPaged(
    String site_id,
    String tenant,
    String name,
    String status,
    String links_up,
    String region,
    String group,
    String circuit_provider,
    Integer page_size,
    Integer page_num
  ) {
    BoolQueryBuilder query = QueryBuilders.boolQuery();
    if (site_id != null) {
      String[] segment = site_id.split("-");
      BoolQueryBuilder site_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        site_query
          .must()
          .add(new WildcardQueryBuilder("site_id", "*" + string + "*"));
      }
      query.must().add(site_query);
    }
    if (tenant != null) {
      String[] segment = tenant.split(",");
      BoolQueryBuilder tenant_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        tenant_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("tenant_name", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(tenant_query);
      // query.must().add(QueryBuilders.matchQuery("tenant_name",
      // "*"+tenant+"*").operator(Operator.AND));
    }
    if (name != null) {
      String[] segment = name.split(" ");
      BoolQueryBuilder name_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        name_query
          .must()
          .add(new WildcardQueryBuilder("name", "*" + string + "*"));
      }
      query.must().add(name_query);
      // query.must().add(QueryBuilders.matchQuery("name",
      // "*"+name+"*").operator(Operator.OR));
      // query.must().add(new WildcardQueryBuilder("name", "*"+name+"*"));
    }
    if (status != null) {
      String[] segment = status.split(",");
      BoolQueryBuilder status_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        status_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("status", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(status_query);
      // query.must().add(QueryBuilders.matchQuery("status","*"+status+"*").operator(Operator.AND));
    }
    if (links_up != null) {
      // query.must().add(QueryBuilders.matchQuery("links_up",links_up).operator(Operator.AND));

      if (links_up.contains("up")) {
        BoolQueryBuilder links_query = QueryBuilders.boolQuery();
        links_query
          .should()
          .add(QueryBuilders.matchQuery("links_up", 1).operator(Operator.AND));
        links_query
          .should()
          .add(QueryBuilders.matchQuery("links_up", 2).operator(Operator.AND));
        links_query
          .should()
          .add(QueryBuilders.matchQuery("links_up", 3).operator(Operator.AND));
        query.must().add(links_query);
      }
      if (links_up.contains("down")) {
        query
          .must()
          .add(QueryBuilders.matchQuery("links_up", 0).operator(Operator.AND));
      }
      if (links_up.contains("unmonitored")) {
        query
          .must()
          .add(QueryBuilders.matchQuery("links_up", -1).operator(Operator.AND));
      }
    }
    if (region != null) {
      String[] segment = region.split(",");
      BoolQueryBuilder region_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        region_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("region", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(region_query);
      // query.must().add(QueryBuilders.matchQuery("region","*"+region+"*").operator(Operator.AND));
    }
    if (group != null) {
      String[] segment = group.split(",");
      BoolQueryBuilder group_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        group_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("island_group", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(group_query);
      // query.must().add(QueryBuilders.matchQuery("island_group","*"+group+"*").operator(Operator.AND));
    }
    if (circuit_provider != null) {
      String[] segment = circuit_provider.split(",");
      BoolQueryBuilder circuit_provider_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        circuit_provider_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("circuits.circuit_provider", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(circuit_provider_query);
      // query.must().add(QueryBuilders.matchQuery("circuits.circuit_provider","*"+circuit_provider+"*").operator(Operator.AND));
    }
    // if(circuit_provider!=null){
    // query.must().add(QueryBuilders.matchQuery("circuits.circuit_provider",circuit_provider));
    // }

    System.out.println(query.toString());
    Pageable pageable = PageRequest.of(page_num, page_size);
    Page<ElasticSiteSummary> result = repository.search(query, pageable);
    return result;
  }

  public List<Map<String, Object>> findSiteLocation(
    String site_id,
    String tenant,
    String name,
    String status,
    String links_up,
    String region,
    String group,
    String circuit_provider
  ) {
    BoolQueryBuilder query = QueryBuilders.boolQuery();
    if (site_id != null) {
      String[] segment = site_id.split("-");
      BoolQueryBuilder site_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        site_query
          .must()
          .add(new WildcardQueryBuilder("site_id", "*" + string + "*"));
      }
      query.must().add(site_query);
    }
    if (tenant != null) {
      String[] segment = tenant.split(",");
      BoolQueryBuilder tenant_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        tenant_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("tenant_name", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(tenant_query);
      // query.must().add(QueryBuilders.matchQuery("tenant_name",
      // "*"+tenant+"*").operator(Operator.AND));
    }
    if (name != null) {
      String[] segment = name.split(" ");
      BoolQueryBuilder name_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        name_query
          .must()
          .add(new WildcardQueryBuilder("name", "*" + string + "*"));
      }
      query.must().add(name_query);
      // query.must().add(QueryBuilders.matchQuery("name",
      // "*"+name+"*").operator(Operator.AND));
      // query.must().add(new WildcardQueryBuilder("name", "*"+name+"*"));
    }
    if (status != null) {
      String[] segment = status.split(",");
      BoolQueryBuilder status_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        status_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("status", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(status_query);
      // query.must().add(QueryBuilders.matchQuery("status","*"+status+"*").operator(Operator.AND));
    }
    if (links_up != null) {
      // query.must().add(QueryBuilders.matchQuery("links_up",links_up).operator(Operator.AND));

      if (links_up.contains("up")) {
        BoolQueryBuilder links_query = QueryBuilders.boolQuery();
        links_query
          .should()
          .add(QueryBuilders.matchQuery("links_up", 1).operator(Operator.AND));
        links_query
          .should()
          .add(QueryBuilders.matchQuery("links_up", 2).operator(Operator.AND));
        links_query
          .should()
          .add(QueryBuilders.matchQuery("links_up", 3).operator(Operator.AND));
        query.must().add(links_query);
      }
      if (links_up.contains("down")) {
        query
          .must()
          .add(QueryBuilders.matchQuery("links_up", 0).operator(Operator.AND));
      }
      if (links_up.contains("unmonitored")) {
        query
          .must()
          .add(QueryBuilders.matchQuery("links_up", -1).operator(Operator.AND));
      }
    }
    if (region != null) {
      String[] segment = region.split(",");
      BoolQueryBuilder region_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        region_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("region", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(region_query);
      // query.must().add(QueryBuilders.matchQuery("region","*"+region+"*").operator(Operator.AND));
    }
    if (group != null) {
      String[] segment = group.split(",");
      BoolQueryBuilder group_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        group_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("island_group", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(group_query);
      // query.must().add(QueryBuilders.matchQuery("island_group","*"+group+"*").operator(Operator.AND));
    }
    if (circuit_provider != null) {
      String[] segment = circuit_provider.split(",");
      BoolQueryBuilder circuit_provider_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        circuit_provider_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("circuits.circuit_provider", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(circuit_provider_query);
      // query.must().add(QueryBuilders.matchQuery("circuits.circuit_provider","*"+circuit_provider+"*").operator(Operator.AND));
    }

    System.out.println(query.toString());
    Iterable<ElasticSiteSummary> result = repository.search(query);
    List<Map<String, Object>> site_locs = new ArrayList<Map<String, Object>>();
    for (ElasticSiteSummary elasticSiteSummary : result) {
      Map<String, Object> site_loc = new HashMap<String, Object>();
      site_loc.put("site_id", elasticSiteSummary.getSite_id());
      site_loc.put("name", elasticSiteSummary.getName());
      site_loc.put("no_links_up", elasticSiteSummary.getLinks_up());
      site_loc.put("latitude", elasticSiteSummary.getLatitude());
      site_loc.put("longitude", elasticSiteSummary.getLongitude());
      site_loc.put("status", elasticSiteSummary.getStatus());
      List<Map<String, Object>> circuitz = new ArrayList<Map<String, Object>>();
      List<site_circuit_status> circuits = elasticSiteSummary.getCircuits();
      try {
        String[] circuit_providers = {};
        if (circuit_provider != null) circuit_providers =
          circuit_provider.split(",");
        for (site_circuit_status site_circuit_status : circuits) {
          Map<String, Object> circuit = new HashMap<String, Object>();
          try {
            if (
              Arrays
                .stream(circuit_providers)
                .anyMatch(el ->
                  site_circuit_status
                    .getCircuit_provider()
                    .toLowerCase()
                    .contains(el.toLowerCase())
                ) ||
              circuit_provider == null
            ) {
              circuit.put("circuit_type", site_circuit_status);
              circuit.put(
                "circuit_provider",
                site_circuit_status.getCircuit_provider()
              );
              circuit.put(
                "circuit_status",
                site_circuit_status.getCircuit_status()
              );
              circuitz.add(circuit);
            } else continue;
          } catch (Exception e) {
            circuit.put("circuit_type", site_circuit_status.getCircuit_type());
            circuit.put(
              "circuit_provider",
              site_circuit_status.getCircuit_provider()
            );
            circuit.put(
              "circuit_status",
              site_circuit_status.getCircuit_status()
            );
            circuitz.add(circuit);
          }
        }
      } catch (Exception e) {
        // TODO: handle exception
      }

      site_loc.put("circuits", circuitz);
      site_locs.add(site_loc);
    }
    return site_locs;
  }

  public List<Map<String, Object>> findAllSites(
    String site_id,
    String tenant,
    String name,
    String status,
    String links_up,
    String region,
    String group,
    String circuit_provider
  ) {
    Iterable<mikrotik> mikrotik_units = mikrotik_repo.findAll();
    Iterable<ruijie> ruijie_units = ruijie_repo.findAll();
    Iterable<sites_site> sites_info = atis_repo.findAll();

    BoolQueryBuilder query = QueryBuilders.boolQuery();
    if (site_id != null) {
      String[] segment = site_id.split("-");
      BoolQueryBuilder site_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        site_query
          .must()
          .add(new WildcardQueryBuilder("site_id", "*" + string + "*"));
      }
      query.must().add(site_query);
    }
    if (tenant != null) {
      String[] segment = tenant.split(",");
      BoolQueryBuilder tenant_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        tenant_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("tenant_name", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(tenant_query);
      // query.must().add(QueryBuilders.matchQuery("tenant_name",
      // "*"+tenant+"*").operator(Operator.AND));
    }
    if (name != null) {
      String[] segment = name.split(" ");
      BoolQueryBuilder name_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        name_query
          .must()
          .add(new WildcardQueryBuilder("name", "*" + string + "*"));
      }
      query.must().add(name_query);
      // query.must().add(QueryBuilders.matchQuery("name",
      // "*"+name+"*").operator(Operator.AND));
      // query.must().add(new WildcardQueryBuilder("name", "*"+name+"*"));
    }
    if (status != null) {
      String[] segment = status.split(",");
      BoolQueryBuilder status_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        status_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("status", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(status_query);
      // query.must().add(QueryBuilders.matchQuery("status","*"+status+"*").operator(Operator.AND));
    }
    if (links_up != null) {
      // query.must().add(QueryBuilders.matchQuery("links_up",links_up).operator(Operator.AND));

      if (links_up.contains("up")) {
        BoolQueryBuilder links_query = QueryBuilders.boolQuery();
        links_query
          .should()
          .add(QueryBuilders.matchQuery("links_up", 1).operator(Operator.AND));
        links_query
          .should()
          .add(QueryBuilders.matchQuery("links_up", 2).operator(Operator.AND));
        links_query
          .should()
          .add(QueryBuilders.matchQuery("links_up", 3).operator(Operator.AND));
        query.must().add(links_query);
      }
      if (links_up.contains("down")) {
        query
          .must()
          .add(QueryBuilders.matchQuery("links_up", 0).operator(Operator.AND));
      }
      if (links_up.contains("unmonitored")) {
        query
          .must()
          .add(QueryBuilders.matchQuery("links_up", -1).operator(Operator.AND));
      }
    }
    if (region != null) {
      String[] segment = region.split(",");
      BoolQueryBuilder region_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        region_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("region", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(region_query);
      // query.must().add(QueryBuilders.matchQuery("region","*"+region+"*").operator(Operator.AND));
    }
    if (group != null) {
      String[] segment = group.split(",");
      BoolQueryBuilder group_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        group_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("island_group", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(group_query);
      // query.must().add(QueryBuilders.matchQuery("island_group","*"+group+"*").operator(Operator.AND));
    }
    if (circuit_provider != null) {
      String[] segment = circuit_provider.split(",");
      BoolQueryBuilder circuit_provider_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        circuit_provider_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("circuits.circuit_provider", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(circuit_provider_query);
      // query.must().add(QueryBuilders.matchQuery("circuits.circuit_provider","*"+circuit_provider+"*").operator(Operator.AND));
    }

    System.out.println(query.toString());
    Iterable<ElasticSiteSummary> result = repository.search(query);
    // return result;
    List<Map<String, Object>> site_locs = new ArrayList<Map<String, Object>>();
    for (ElasticSiteSummary elasticSiteSummary : result) {
      Map<String, Object> site_loc = new HashMap<String, Object>();
      site_loc.put("site_id", elasticSiteSummary.getSite_id());
      site_loc.put("name", elasticSiteSummary.getName());
      site_loc.put("tenant", elasticSiteSummary.getTenant_name());
      site_loc.put("group", elasticSiteSummary.getIsland_group());
      site_loc.put("region", elasticSiteSummary.getRegion());

      Integer number_of_links = elasticSiteSummary.getLinks_up();
      if (number_of_links > 0) {
        site_loc.put("network_status", "up");
      } else if (number_of_links < 0) {
        site_loc.put("network_status", "unmonitored");
      } else {
        site_loc.put("network_status", "down");
      }
      site_loc.put("status", elasticSiteSummary.getStatus());
      // site_loc.put("no_links_up", elasticSiteSummary.getLinks_up());
      // site_loc.put("latitude", elasticSiteSummary.getLatitude());
      // site_loc.put("longitude", elasticSiteSummary.getLongitude());

      try {
        for (sites_site site : sites_info) {
          if (
            site.getSite_id().equalsIgnoreCase(elasticSiteSummary.getSite_id())
          ) {
            site_loc.put("address", site.getAddress());
          }
        }
      } catch (Exception e) {
        // TODO: handle exception
      }

      try {
        for (mikrotik unit : mikrotik_units) {
          if (
            unit
              .getSiteId()
              .equalsIgnoreCase(elasticSiteSummary.getSite_id()) &&
            unit.getRouter().equalsIgnoreCase("mikrotik")
          ) {
            // if (!unit.getPort2().equals("") || !unit.getPort2().equals("NO PROVIDER"))
            site_loc.put("primary_provider", unit.getPort2());

            // if (!unit.getPort1().equals("") || !unit.getPort1().equals("NO PROVIDER"))
            site_loc.put("backup_provider", unit.getPort1());
          }
        }
      } catch (Exception e) {
        // TODO: handle exception
      }

      try {
        for (ruijie unit : ruijie_units) {
          if (
            unit
              .getSite_id()
              .equalsIgnoreCase(elasticSiteSummary.getSite_id()) &&
            unit.getRouter().equalsIgnoreCase("ruijie")
          ) {
            // if (!unit.getVlan10().equals("") || !unit.getVlan10().equals("NO PROVIDER"))
            site_loc.put("primary_provider", unit.getVlan10());

            // if (!unit.getCellular().equals("") || !unit.getCellular().equals("NO
            // PROVIDER"))
            site_loc.put("backup_provider", unit.getCellular());

            // if (!unit.getVlan20().equals("") || !unit.getVlan20().equals("NO PROVIDER"))
            site_loc.put("third_provider", unit.getVlan20());
          }
        }
      } catch (Exception e) {
        // TODO: handle exception
      }

      List<site_circuit_status> circuits = elasticSiteSummary.getCircuits();

      try {
        String[] circuit_providers = {};
        if (circuit_provider != null) circuit_providers =
          circuit_provider.split(",");
        for (site_circuit_status site_circuit_status : circuits) {
          try {
            if (
              Arrays
                .stream(circuit_providers)
                .anyMatch(el ->
                  site_circuit_status
                    .getCircuit_provider()
                    .toLowerCase()
                    .contains(el.toLowerCase())
                ) ||
              circuit_provider == null
            ) {
              if (site_circuit_status.getCircuit_type().equals("LOOPBACK 0")) {
                site_loc.put(
                  "loop_back_0_downtime",
                  site_circuit_status.getMinutes_down().floatValue()
                );
                site_loc.put(
                  "loop_back_0_ip",
                  site_circuit_status.getCircuit_ip()
                );
                site_loc.put(
                  "loop_back_0_status",
                  site_circuit_status.getCircuit_status()
                );
                site_loc.put(
                  "loop_back_0_uptime",
                  site_circuit_status.getCircuit_uptime()
                );
                // site_loc.put("loop_back_0_uptime",
                // prometheus_help.getUptimeAvailability(elasticSiteSummary.getSite_id(),
                // "loopback 0"));
                continue;
              }

              if (site_circuit_status.getCircuit_type().equals("Primary")) {
                site_loc.put(
                  "primary_downtime",
                  site_circuit_status.getMinutes_down().floatValue()
                );
                site_loc.put("primary_ip", site_circuit_status.getCircuit_ip());
                site_loc.put(
                  "primary_status",
                  site_circuit_status.getCircuit_status()
                );
                site_loc.put(
                  "primary_uptime",
                  site_circuit_status.getCircuit_uptime()
                );
                continue;
              }
              if (site_circuit_status.getCircuit_type().equals("Backup")) {
                site_loc.put(
                  "backup_downtime",
                  site_circuit_status.getMinutes_down().floatValue()
                );
                site_loc.put("backup_ip", site_circuit_status.getCircuit_ip());
                site_loc.put(
                  "backup_status",
                  site_circuit_status.getCircuit_status()
                );
                site_loc.put(
                  "backup_uptime",
                  site_circuit_status.getCircuit_uptime()
                );
                continue;
              }
              if (
                site_circuit_status.getCircuit_type().equals("ThirdCircuit")
              ) {
                site_loc.put(
                  "third_downtime",
                  site_circuit_status.getMinutes_down().floatValue()
                );
                site_loc.put("third_ip", site_circuit_status.getCircuit_ip());
                site_loc.put(
                  "third_status",
                  site_circuit_status.getCircuit_status()
                );
                site_loc.put(
                  "third_uptime",
                  site_circuit_status.getCircuit_uptime()
                );
                continue;
              }
            } else continue;
          } catch (Exception ee) {
            if (site_circuit_status.getCircuit_type().equals("LOOPBACK 0")) {
              site_loc.put(
                "loop_back_0_downtime",
                site_circuit_status.getMinutes_down().floatValue()
              );
              site_loc.put(
                "loop_back_0_uptime",
                site_circuit_status.getCircuit_uptime()
              );
              site_loc.put(
                "loop_back_0_ip",
                site_circuit_status.getCircuit_ip()
              );
              site_loc.put(
                "loop_back_0_status",
                site_circuit_status.getCircuit_status()
              );
              continue;
            }

            if (site_circuit_status.getCircuit_type().equals("Primary")) {
              site_loc.put(
                "primary_downtime",
                site_circuit_status.getMinutes_down().floatValue()
              );
              site_loc.put("primary_ip", site_circuit_status.getCircuit_ip());
              site_loc.put(
                "primary_status",
                site_circuit_status.getCircuit_status()
              );
              site_loc.put(
                "primary_uptime",
                site_circuit_status.getCircuit_uptime()
              );
              continue;
            }
            if (site_circuit_status.getCircuit_type().equals("Backup")) {
              site_loc.put(
                "backup_downtime",
                site_circuit_status.getMinutes_down().floatValue()
              );
              site_loc.put("backup_ip", site_circuit_status.getCircuit_ip());
              site_loc.put(
                "backup_status",
                site_circuit_status.getCircuit_status()
              );
              site_loc.put(
                "backup_uptime",
                site_circuit_status.getCircuit_uptime()
              );
              continue;
            }
            if (site_circuit_status.getCircuit_type().equals("ThirdCircuit")) {
              site_loc.put(
                "third_downtime",
                site_circuit_status.getMinutes_down().floatValue()
              );
              site_loc.put("third_ip", site_circuit_status.getCircuit_ip());
              site_loc.put(
                "third_status",
                site_circuit_status.getCircuit_status()
              );
              site_loc.put(
                "third_uptime",
                site_circuit_status.getCircuit_uptime()
              );
              continue;
            }
          }
        }
      } catch (Exception e) {
        // TODO: handle exception
      }
      site_locs.add(site_loc);
    }
    return site_locs;
  }

  public Map<String, Object> CountAllSites(
    // String site_id,
    String tenant
    // String name,
    // String status,
    // String links_up,
    // String region,
    // String group,
    // String circuit_provider
  ) throws IOException, JSONException {
    BoolQueryBuilder query = QueryBuilders.boolQuery();
    // if(site_id!=null){
    // String[] segment = site_id.split("-");
    // BoolQueryBuilder site_query = QueryBuilders.boolQuery();
    // for (String string : segment) {
    // site_query.must().add(new WildcardQueryBuilder("site_id", "*"+string+"*"));
    // }
    // query.must().add(site_query);
    // }
    if (tenant != null) {
      String[] segment = tenant.split(",");
      BoolQueryBuilder tenant_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        tenant_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("tenant_name", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(tenant_query);
      // query.must().add(QueryBuilders.matchQuery("tenant_name",
      // "*"+tenant+"*").operator(Operator.AND));
    }
    // if(name!=null){
    // String[] segment = name.split(" ");
    // BoolQueryBuilder name_query = QueryBuilders.boolQuery();
    // for (String string : segment) {
    // name_query.must().add(new WildcardQueryBuilder("name", "*"+string+"*"));
    // }
    // query.must().add(name_query);
    // //query.must().add(QueryBuilders.matchQuery("name",
    // "*"+name+"*").operator(Operator.AND));
    // //query.must().add(new WildcardQueryBuilder("name", "*"+name+"*"));
    // }
    String status = "active";
    if (status != null) {
      String[] segment = status.split(",");
      BoolQueryBuilder status_query = QueryBuilders.boolQuery();
      for (String string : segment) {
        status_query
          .should()
          .add(
            QueryBuilders
              .matchQuery("status", "*" + string + "*")
              .operator(Operator.AND)
          );
      }
      query.must().add(status_query);
      // query.must().add(QueryBuilders.matchQuery("status","*"+status+"*").operator(Operator.AND));

    }
    // if(links_up!=null){
    // //query.must().add(QueryBuilders.matchQuery("links_up",links_up).operator(Operator.AND));
    //
    // if(links_up.contains("up")){
    // BoolQueryBuilder links_query = QueryBuilders.boolQuery();
    // links_query.should().add(QueryBuilders.matchQuery("links_up",1).operator(Operator.AND));
    // links_query.should().add(QueryBuilders.matchQuery("links_up",2).operator(Operator.AND));
    // links_query.should().add(QueryBuilders.matchQuery("links_up",3).operator(Operator.AND));
    // query.must().add(links_query);
    // }
    // if(links_up.contains("down")){
    // query.must().add(QueryBuilders.matchQuery("links_up",0).operator(Operator.AND));
    // }
    // if(links_up.contains("unmonitored")){
    // query.must().add(QueryBuilders.matchQuery("links_up",-1).operator(Operator.AND));
    // }
    // }
    // if(region!=null){
    // String[] segment = region.split(",");
    // BoolQueryBuilder region_query = QueryBuilders.boolQuery();
    // for (String string : segment) {
    // region_query.should().add(QueryBuilders.matchQuery("region","*"+string+"*").operator(Operator.AND));
    // }
    // query.must().add(region_query);
    // //query.must().add(QueryBuilders.matchQuery("region","*"+region+"*").operator(Operator.AND));
    // }
    // if(group!=null){
    // String[] segment = group.split(",");
    // BoolQueryBuilder group_query = QueryBuilders.boolQuery();
    // for (String string : segment) {
    // group_query.should().add(QueryBuilders.matchQuery("island_group","*"+string+"*").operator(Operator.AND));
    // }
    // query.must().add(group_query);
    // //query.must().add(QueryBuilders.matchQuery("island_group","*"+group+"*").operator(Operator.AND));
    // }
    // if(circuit_provider!=null){
    // String[] segment = circuit_provider.split(",");
    // BoolQueryBuilder circuit_provider_query = QueryBuilders.boolQuery();
    // for (String string : segment) {
    // circuit_provider_query.should().add(QueryBuilders.matchQuery("circuits.circuit_provider","*"+string+"*").operator(Operator.AND));
    // }
    // query.must().add(circuit_provider_query);
    // //query.must().add(QueryBuilders.matchQuery("circuits.circuit_provider","*"+circuit_provider+"*").operator(Operator.AND));
    // }

    System.out.println(query.toString());
    Iterable<ElasticSiteSummary> result = repository.search(query);
    int total_down_stores = 0;
    int total_down_stores_ruijie = 0;
    int total_down_stores_mikrotik = 0;
    int total_down_stores_unkown = 0;

    int problematic_primary_circuit = 0;
    int primary_circuit_less_than_95_percent = 0;
    int primary_up = 0;
    int primary_no_internet = 0;
    int primary_number_circuit = 0;
    int primary_wireless = 0;

    int no_of_wireless_down_primary = 0;

    int problematic_backup_circuit = 0;
    int backup_circuit_less_than_95_percent = 0;
    int backup_up = 0;
    int backup_no_internet = 0;
    int backup_number_circuit = 0;
    int backup_wired = 0;

    int no_of_wireless_down_backup = 0;

    int single_circuit = 0;
    int wireless_only = 0;
    int no_internet = 0;
    int dual_circuit = 0;

    int problematic_stores = 0;
    int stores_less_than_95_percent = 0;

    Map<String, Object> provider = new HashMap<String, Object>();

    for (ElasticSiteSummary elasticSiteSummary : result) {
      List<site_circuit_status> circuits = elasticSiteSummary.getCircuits();

      int no_of_ciruits_up = 0;
      int no_of_wireless_up = 0;
      int no_of_circuits = 0;

      boolean problematic_lo0 = false;
      boolean problematic_primary = false;
      boolean problematic_backup = false;

      try {
        String[] circuit_providers = {};
        boolean has_primary = false;
        boolean has_backup = false;
        for (site_circuit_status site_circuit_status : circuits) {
          try {
            if (
              Arrays
                .stream(circuit_providers)
                .anyMatch(el ->
                  site_circuit_status
                    .getCircuit_provider()
                    .toLowerCase()
                    .contains(el.toLowerCase())
                ) ||
              true
            ) {
              if (site_circuit_status.getCircuit_type().equals("LOOPBACK 0")) {
                if (
                  (
                    site_circuit_status.getCircuit_status().equals("DOWN") ||
                    (site_circuit_status.getMinutes_down().floatValue() > 0)
                  ) &&
                  !site_circuit_status.getCircuit_ip().isEmpty()
                ) {
                  total_down_stores++;

                  if (
                    site_circuit_status.getCircuit_provider().contains("SIM")
                  ) {}
                  if (
                    isInRange(
                      ipToInt(site_circuit_status.getCircuit_ip()),
                      ipToInt("10.9.0.0"),
                      ipToInt("10.9.31.255")
                    )
                  ) {
                    total_down_stores_ruijie++;
                  } else if (
                    isInRange(
                      ipToInt(site_circuit_status.getCircuit_ip()),
                      ipToInt("10.9.32.0"),
                      ipToInt("10.9.63.255")
                    )
                  ) {
                    total_down_stores_mikrotik++;
                  } else {
                    total_down_stores_unkown++;
                  }

                  problematic_lo0 = true;
                }

                if (
                  Float.parseFloat(site_circuit_status.getCircuit_uptime()) <
                  0.95
                ) {
                  problematic_lo0 = true;
                  stores_less_than_95_percent++;
                }

                // site_loc.put("loop_back_0_uptime",
                // prometheus_help.getUptimeAvailability(elasticSiteSummary.getSite_id(),
                // "loopback 0"));
                continue;
              }
              if (site_circuit_status.getCircuit_type().equals("Primary")) {
                has_primary = true;
                if (
                  site_circuit_status.getCircuit_status().equals("DOWN") ||
                  (site_circuit_status.getMinutes_down().floatValue() > 0)
                ) {
                  if (
                    provider.containsKey(
                      site_circuit_status.getCircuit_provider()
                    )
                  ) {
                    provider.put(
                      site_circuit_status.getCircuit_provider(),
                      Integer.parseInt(
                        provider
                          .get(site_circuit_status.getCircuit_provider())
                          .toString()
                      ) +
                      1
                    );
                  } else {
                    provider.put(site_circuit_status.getCircuit_provider(), 1);
                  }
                  primary_no_internet++;
                  problematic_primary = true;
                } else {
                  primary_up++;
                  no_of_ciruits_up++;
                }
                if (site_circuit_status.getCircuit_provider().contains("SIM")) {
                  primary_wireless++;
                  no_of_wireless_up++;
                }
                no_of_circuits++;

                if (
                  Float.parseFloat(site_circuit_status.getCircuit_uptime()) <
                  0.95
                ) {
                  problematic_primary = true;
                  primary_circuit_less_than_95_percent++;
                }

                continue;
              }
              if (site_circuit_status.getCircuit_type().equals("Backup")) {
                has_backup = true;
                if (
                  site_circuit_status.getCircuit_status().equals("DOWN") ||
                  (site_circuit_status.getMinutes_down().floatValue() > 0)
                ) {
                  if (
                    provider.containsKey(
                      site_circuit_status.getCircuit_provider()
                    )
                  ) {
                    provider.put(
                      site_circuit_status.getCircuit_provider(),
                      Integer.parseInt(
                        provider
                          .get(site_circuit_status.getCircuit_provider())
                          .toString()
                      ) +
                      1
                    );
                  } else {
                    provider.put(site_circuit_status.getCircuit_provider(), 1);
                  }
                  backup_no_internet++;
                  problematic_backup = true;
                } else {
                  backup_up++;
                  no_of_ciruits_up++;
                }
                if (site_circuit_status.getCircuit_provider().contains("SIM")) {
                  no_of_wireless_up++;
                } else {
                  backup_wired++;
                }
                no_of_circuits++;

                if (
                  Float.parseFloat(site_circuit_status.getCircuit_uptime()) <
                  0.95
                ) {
                  problematic_backup = true;
                  backup_circuit_less_than_95_percent++;
                }

                continue;
              }
              // if(site_circuit_status.getCircuit_type().equals("ThirdCircuit")){
              // if(site_circuit_status.getCircuit_status().equals("DOWN")||(site_circuit_status.getMinutes_down().floatValue()>0)){
              // if(provider.containsKey(site_circuit_status.getCircuit_provider())){
              // provider.put(site_circuit_status.getCircuit_provider(),
              // Integer.parseInt(provider.get(site_circuit_status.getCircuit_provider()).toString())+1);
              // }
              // else{
              // provider.put(site_circuit_status.getCircuit_provider(),1);
              // }
              // }else{
              // no_of_ciruits_up++;
              // }
              // if(site_circuit_status.getCircuit_provider().contains("SIM")){
              // no_of_wireless_up++;
              // }
              // no_of_circuits++;
              // continue;
              // }
            } else continue;
          } catch (Exception e) {
            if (site_circuit_status.getCircuit_type().equals("LOOPBACK 0")) {
              if (
                site_circuit_status.getCircuit_status().equals("DOWN") ||
                (site_circuit_status.getMinutes_down().floatValue() > 0)
              ) {
                total_down_stores++;
                if (
                  isInRange(
                    ipToInt(site_circuit_status.getCircuit_ip()),
                    ipToInt("10.9.0.0"),
                    ipToInt("10.9.31.255")
                  )
                ) {
                  total_down_stores_ruijie++;
                } else if (
                  isInRange(
                    ipToInt(site_circuit_status.getCircuit_ip()),
                    ipToInt("10.9.32.0"),
                    ipToInt("10.9.63.255")
                  )
                ) {
                  total_down_stores_mikrotik++;
                } else {
                  total_down_stores_unkown++;
                }
                problematic_lo0 = true;
              }
              if (
                Float.parseFloat(site_circuit_status.getCircuit_uptime()) < 0.95
              ) {
                problematic_lo0 = true;
                stores_less_than_95_percent++;
              }
              continue;
            }
            if (site_circuit_status.getCircuit_type().equals("Primary")) {
              has_primary = true;

              if (
                site_circuit_status.getCircuit_status().equals("DOWN") ||
                site_circuit_status.getMinutes_down().floatValue() > 0
              ) {
                primary_no_internet++;
                problematic_primary = true;
              } else {
                primary_up++;
                no_of_ciruits_up++;
              }
              if (site_circuit_status.getCircuit_provider().contains("SIM")) {
                no_of_wireless_up++;
                primary_wireless++;
              }

              no_of_circuits++;

              if (
                Float.parseFloat(site_circuit_status.getCircuit_uptime()) < 0.95
              ) {
                problematic_primary = true;
                primary_circuit_less_than_95_percent++;
              }

              continue;
            }
            if (site_circuit_status.getCircuit_type().equals("Backup")) {
              has_backup = true;
              if (
                site_circuit_status.getCircuit_status().equals("DOWN") ||
                (site_circuit_status.getMinutes_down().floatValue() > 0)
              ) {
                if (
                  provider.containsKey(
                    site_circuit_status.getCircuit_provider()
                  )
                ) {
                  provider.put(
                    site_circuit_status.getCircuit_provider(),
                    Integer.parseInt(
                      provider
                        .get(site_circuit_status.getCircuit_provider())
                        .toString()
                    ) +
                    1
                  );
                } else {
                  provider.put(site_circuit_status.getCircuit_provider(), 1);
                }
                backup_no_internet++;
                problematic_backup = true;
              } else {
                backup_up++;
                no_of_ciruits_up++;
              }
              if (site_circuit_status.getCircuit_provider().contains("SIM")) {
                no_of_wireless_up++;
              } else {
                backup_wired++;
              }

              no_of_circuits++;

              if (
                Float.parseFloat(site_circuit_status.getCircuit_uptime()) < 0.95
              ) {
                problematic_backup = true;
                backup_circuit_less_than_95_percent++;
              }

              continue;
            }
            // if(site_circuit_status.getCircuit_type().equals("ThirdCircuit")){
            // if(site_circuit_status.getCircuit_status().equals("DOWN")||(site_circuit_status.getMinutes_down().floatValue()>0)){
            // if(provider.containsKey(site_circuit_status.getCircuit_provider())){
            // provider.put(site_circuit_status.getCircuit_provider(),
            // Integer.parseInt(provider.get(site_circuit_status.getCircuit_provider()).toString())+1);
            // }
            // else{
            // provider.put(site_circuit_status.getCircuit_provider(),1);
            // }
            // }else{
            // no_of_ciruits_up++;
            // }
            // if(site_circuit_status.getCircuit_provider().contains("SIM")){
            // no_of_wireless_up++;
            // }
            // no_of_circuits++;
            // continue;
            // }
          }
        }

        if (!has_primary) {
          primary_number_circuit++;
        }
        if (!has_backup) {
          backup_number_circuit++;
        }

        if (no_of_ciruits_up > 1) {
          dual_circuit++;
        } else if (no_of_ciruits_up == 1) {
          single_circuit++;
        } else {
          no_internet++;
        }
        if (
          (no_of_wireless_up == no_of_ciruits_up) ||
          (no_of_wireless_up == no_of_circuits)
        ) {
          wireless_only++;
        }
      } catch (Exception e) {
        // TODO: handle exception
      }
      if ((problematic_lo0 && problematic_primary && problematic_backup)) {
        problematic_stores++;
      }
      if (problematic_primary) {
        problematic_primary_circuit++;
      }
      if (problematic_backup) {
        problematic_backup_circuit++;
      }
    }
    Map<String, Object> results = new HashMap<String, Object>();
    results.put("total_down_stores", total_down_stores);
    results.put("total_down_stores_ruijie", total_down_stores_ruijie);
    results.put("total_down_stores_mikrotik", total_down_stores_mikrotik);
    results.put("total_down_stores_unkown", total_down_stores_unkown);

    results.put("problematic_stores", problematic_stores);
    results.put("stores_less_than_95_percent", stores_less_than_95_percent);
    results.put("single_circuit", single_circuit);
    results.put("wireless_only", wireless_only);

    results.put("problematic_primary_circuit", problematic_primary_circuit);
    results.put(
      "primary_circuit_less_than_95_percent",
      primary_circuit_less_than_95_percent
    );
    results.put("primary_up", primary_up);
    results.put("primary_no_internet", primary_no_internet);
    results.put("primary_no_circuit", primary_number_circuit);
    results.put("primary_wireless", primary_wireless);

    results.put("problematic_backup_circuit", problematic_backup_circuit);
    results.put(
      "backup_circuit_less_than_95_percent",
      backup_circuit_less_than_95_percent
    );
    results.put("backup_up", backup_up);
    results.put("backup_no_internet", backup_no_internet);
    results.put("backup_no_circuit", backup_number_circuit);
    results.put("backup_wired", backup_wired);

    results.put("dual_circuit", dual_circuit);
    results.put("no_internet", no_internet);

    // results.put("intermittent_primary", intermittent_primary);
    // results.put("intermittent_backup", intermittent_backup);

    // results.put("problematic_stores",
    // prometheus_help.getProblematicCircuits("loopback 0"));
    // results.put("problematic_stores",
    // prometheus_help.getUptimeAvailability("ST-0003","loopback 0"));
    results.put("down_stores_per_providers", provider);
    return results;
  }

  public int ipToInt(String ipAddress) {
    String[] parts = ipAddress.split("\\.");
    int result = 0;
    for (String part : parts) {
      result = (result << 8) | Integer.parseInt(part);
    }
    return result;
  }

  public boolean isInRange(int ipToCheck, int start, int end) {
    return ipToCheck >= start && ipToCheck <= end;
  }
}
