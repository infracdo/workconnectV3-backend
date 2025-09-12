package com.tracker_app.tracker.DataSource_elasticsearch.repo;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.tracker_app.tracker.DataSource_elasticsearch.model.ElasticCircuitsLog;

public interface ElasticCircuitsLogRepo
        extends ElasticsearchRepository<ElasticCircuitsLog, String> {

    // @Query("{\"bool\": {\"must\": [{\"term\": {\"SiteID.keyword\": \"?0\"}}]}}")
    @Query("{\"bool\": {\"must\": [{\"match\": {\"SiteID.keyword\": \"?0\"}}, {\"bool\": {\"must_not\": [{\"term\": {\"Mac_status.keyword\": \"unchanged\"}}, {\"term\": {\"Provider_status.keyword\": \"unchanged\"}}]}}]}}")
    List<ElasticCircuitsLog> findBySiteID(String siteID);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"SiteID.keyword\": \"?0\"}}, {\"terms\": {\"Port.keyword\": [\"vlan10\", \"vlan20\", \"port1\", \"port2\"]}}, {\"bool\": {\"must\": [{\"terms\": {\"Mac_status.keyword\": [\"added\", \"removed\", \"changed\"]}}, {\"exists\": {\"field\": \"Mac\"}}]}}]}}")
    List<ElasticCircuitsLog> findBySiteIdMac(String siteId, PageRequest pageable);

    @Query("{\"bool\": {\"must\": [{\"terms\": {\"Port.keyword\": [\"vlan10\", \"vlan20\", \"port1\", \"port2\"]}}, {\"bool\": {\"must\": [{\"terms\": {\"Mac_status.keyword\": [\"added\", \"removed\", \"changed\"]}}, {\"exists\": {\"field\": \"Mac\"}}]}}]}}")
    List<ElasticCircuitsLog> findMac(PageRequest pageable);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"SiteID.keyword\": \"?0\"}}, {\"terms\": {\"Port.keyword\": [\"vlan10\", \"vlan20\", \"port1\", \"port2\", \"Cellular\"]}}, {\"bool\": {\"must\": [{\"terms\": {\"Provider_status.keyword\": [\"added\", \"removed\", \"changed\"]}}, {\"exists\": {\"field\": \"Provider\"}}]}}]}}")
    List<ElasticCircuitsLog> findBySiteIdProvider(String siteId, PageRequest pageable);

    @Query("{\"bool\": {\"must\": [{\"terms\": {\"Port.keyword\": [\"vlan10\", \"vlan20\", \"port1\", \"port2\", \"Cellular\"]}}, {\"bool\": {\"must\": [{\"terms\": {\"Provider_status.keyword\": [\"added\", \"removed\", \"changed\"]}}, {\"exists\": {\"field\": \"Provider\"}}]}}]}}")
    List<ElasticCircuitsLog> findProvider(PageRequest pageable);

    @Query("{\"bool\": {\"must\": [{\"term\": {\"SiteID.keyword\": \"?0\"}}, {\"term\": {\"Port.keyword\": \"Cellular\"}}, {\"bool\": {\"must_not\": [{\"term\": {\"Provider_status.keyword\": \"unchanged\"}}]}}]}}")
    List<ElasticCircuitsLog> findBySiteIdCellular(String siteId, PageRequest pageable);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"SiteID.keyword\": \"?0\"}}, {\"terms\": {\"Port.keyword\": [\"Cellular\"]}}, {\"bool\": {\"must\": [{\"terms\": {\"Mac_status.keyword\": [\"added\", \"removed\", \"changed\"]}}, {\"exists\": {\"field\": \"IMSI\"}}]}}]}}")
    List<ElasticCircuitsLog> findBySiteIdNonBlankImsi(String siteId, PageRequest pageable);

    @Query("{\"bool\": {\"must\": [{\"terms\": {\"Port.keyword\": [\"Cellular\"]}}, {\"bool\": {\"must\": [{\"terms\": {\"Mac_status.keyword\": [\"added\", \"removed\", \"changed\"]}}, {\"exists\": {\"field\": \"IMSI\"}}]}}]}}")
    List<ElasticCircuitsLog> findNonBlankImsi(PageRequest pageable);

    @Query("{\"bool\": {\"must\": [{\"term\": {\"Port.keyword\": \"?0\"}}]}}")
    List<ElasticCircuitsLog> findByPort(String port);

    @Query("{\"bool\": {\"must\": [{\"term\": {\"Mac_status.keyword\": \"?0\"}}]}}")
    List<ElasticCircuitsLog> findByMacStatus(String mac_status);

    @Query("{\"bool\": {\"must\": [{\"term\": {\"Provider_status.keyword\": \"?0\"}}]}}")
    List<ElasticCircuitsLog> findByProviderStatus(String provider_status);

}
