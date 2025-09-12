package com.tracker_app.tracker.DataSource_elasticsearch.repo;

import com.tracker_app.tracker.DataSource_elasticsearch.model.ElasticSiteSummary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

public interface  ElasticSiteSummary_repository extends ElasticsearchRepository<ElasticSiteSummary, String> {
    
    @Query("{\"bool\": {\"must\": ["
    +"{\"match\": {\"site_id\": \"*?0*\"}}"
    +"]}}")
    ElasticSiteSummary findBySiteID(String site_id);

    @Query("{\"bool\": {\"must\": ["
    +"\"*?0*\""
    +"]}}")
    ElasticSiteSummary testquery(String test);


    @Query("{\"bool\": {\"should\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?3*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"links_up\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findByAllOR(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("status") String status,
        @Param("links_up") String links_up, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?3*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?5\",\"fields\":[\"region\"]}},"
    +"{\"query_string\":{ \"query\":\"?6\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDNameANDStatusANDLinksUPANDregionANDgroup(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("status") String status,
        @Param("links_up") String links_up,
        @Param("region") String region,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?3*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?5\",\"fields\":[\"region\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDNameANDStatusANDLinksUPANDregion(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("status") String status,
        @Param("links_up") String links_up,
        @Param("region") String region,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?3*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?5\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDNameANDStatusANDLinksUPANDgroup(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("status") String status,
        @Param("links_up") String links_up,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?3*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"links_up\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDNameANDStatusANDLinksUP(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("status") String status,
        @Param("links_up") String links_up, 
        Pageable pageable
    );
    
    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"region\"]}},"
    +"{\"query_string\":{ \"query\":\"?5\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDNameANDLinksUPANDregionANDgroup(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("links_up") String links_up,
        @Param("region") String region,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"region\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDNameANDLinksUPANDregion(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("links_up") String links_up,
        @Param("region") String region,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDNameANDLinksUPANDgroup(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("links_up") String links_up,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"links_up\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDNameANDLinksUP(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("links_up") String links_up, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"region\"]}},"
    +"{\"query_string\":{ \"query\":\"?5\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDStatusANDLinksUPANDregionANDgroup(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant,
        @Param("status") String status,
        @Param("links_up") String links_up,
        @Param("region") String region,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"region\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDStatusANDLinksUPANDregion(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant,
        @Param("status") String status,
        @Param("links_up") String links_up,
        @Param("region") String region,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDStatusANDLinksUPANDgroup(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant,
        @Param("status") String status,
        @Param("links_up") String links_up,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"links_up\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDStatusANDLinksUP(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant,
        @Param("status") String status,
        @Param("links_up") String links_up, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"region\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDLinksUPANDregionANDgroup(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant,
        @Param("links_up") String links_up,
        @Param("region") String region,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"region\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDLinksUPANDregion(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant,
        @Param("links_up") String links_up,
        @Param("region") String region,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDLinksUPANDgroup(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant,
        @Param("links_up") String links_up,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDLinksUP(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant,
        @Param("links_up") String links_up, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"region\"]}},"
    +"{\"query_string\":{ \"query\":\"?5\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDNameANDStatusANDLinksUPANDregionANDgroup(
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("status") String status,
        @Param("links_up") String links_up,
        @Param("region") String region,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"region\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDNameANDStatusANDLinksUPANDregion(
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("status") String status,
        @Param("links_up") String links_up,
        @Param("region") String region,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDNameANDStatusANDLinksUPANDgroup(
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("status") String status,
        @Param("links_up") String links_up,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"links_up\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDNameANDStatusANDLinksUP(
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("status") String status,
        @Param("links_up") String links_up, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"region\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDNameANDLinksUPANDregionANDgroup(
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("links_up") String links_up,
        @Param("region") String region,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"region\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDNameANDLinksUPANDregion(
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("links_up") String links_up,
        @Param("region") String region,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDNameANDLinksUPANDgroup(
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("links_up") String links_up,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDNameANDLinksUP(
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("links_up") String links_up, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"region\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDStatusANDLinksUPANDregionANDgroup(
        @Param("tenant") String tenant,
        @Param("status") String status,
        @Param("links_up") String links_up,
        @Param("region") String region,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"region\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDStatusANDLinksUPANDregion(
        @Param("tenant") String tenant,
        @Param("status") String status,
        @Param("links_up") String links_up,
        @Param("region") String region,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDStatusANDLinksUPANDgroup(
        @Param("tenant") String tenant,
        @Param("status") String status,
        @Param("links_up") String links_up,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDStatusANDLinksUP(
        @Param("tenant") String tenant,
        @Param("status") String status,
        @Param("links_up") String links_up, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"?1\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"region\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDLinksUPANDregionANDgroup(
        @Param("tenant") String tenant,
        @Param("links_up") String links_up,
        @Param("region") String region,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"?1\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"region\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDLinksUPANDregion(
        @Param("tenant") String tenant,
        @Param("links_up") String links_up,
        @Param("region") String region,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"?1\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDLinksUPANDgroup(
        @Param("tenant") String tenant,
        @Param("links_up") String links_up,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"?1\",\"fields\":[\"links_up\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDLinksUP(
        @Param("tenant") String tenant,
        @Param("links_up") String links_up, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"region\"]}},"
    +"{\"query_string\":{ \"query\":\"?5\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDNameANDStatusANDLinksUPANDregionANDgroup(
        @Param("site_id") String site_id, 
        @Param("name") String name,
        @Param("status") String status,
        @Param("links_up") String links_up,
        @Param("region") String region,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"region\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDNameANDStatusANDLinksUPANDregion(
        @Param("site_id") String site_id, 
        @Param("name") String name,
        @Param("status") String status,
        @Param("links_up") String links_up,
        @Param("region") String region,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"region\"]}},"
    +"{\"query_string\":{ \"query\":\"?5\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDNameANDStatusANDLinksUPANDgroup(
        @Param("site_id") String site_id, 
        @Param("name") String name,
        @Param("status") String status,
        @Param("links_up") String links_up,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"links_up\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDNameANDStatusANDLinksUP(
        @Param("site_id") String site_id,  
        @Param("name") String name,
        @Param("status") String status,
        @Param("links_up") String links_up, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"region\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDNameANDLinksUPANDregionANDgroup(
        @Param("site_id") String site_id, 
        @Param("name") String name,
        @Param("links_up") String links_up,
        @Param("region") String region,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"region\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDNameANDLinksUPANDregion(
        @Param("site_id") String site_id, 
        @Param("name") String name,
        @Param("links_up") String links_up,
        @Param("region") String region,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDNameANDLinksUPANDgroup(
        @Param("site_id") String site_id, 
        @Param("name") String name,
        @Param("links_up") String links_up,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDNameANDLinksUP(
        @Param("site_id") String site_id,  
        @Param("name") String name,
        @Param("links_up") String links_up, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"region\"]}},"
    +"{\"query_string\":{ \"query\":\"?4\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDStatusANDLinksUPANDregionANDgroup(
        @Param("site_id") String site_id, 
        @Param("status") String status,
        @Param("links_up") String links_up,
        @Param("region") String region,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"region\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDStatusANDLinksUPANDregion(
        @Param("site_id") String site_id, 
        @Param("status") String status,
        @Param("links_up") String links_up,
        @Param("region") String region,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDStatusANDLinksUPANDgroup(
        @Param("site_id") String site_id, 
        @Param("status") String status,
        @Param("links_up") String links_up,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDStatusANDLinksUP(
        @Param("site_id") String site_id,
        @Param("status") String status,
        @Param("links_up") String links_up, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"?1\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"region\"]}},"
    +"{\"query_string\":{ \"query\":\"?3\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDLinksUPANDregionANDgroup(
        @Param("site_id") String site_id, 
        @Param("links_up") String links_up,
        @Param("region") String region,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"?1\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"region\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDLinksUPANDregion(
        @Param("site_id") String site_id, 
        @Param("links_up") String links_up,
        @Param("region") String region,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"?1\",\"fields\":[\"links_up\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"group\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDLinksUPANDgroup(
        @Param("site_id") String site_id, 
        @Param("links_up") String links_up,
        @Param("group") String group, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"?1\",\"fields\":[\"links_up\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDLinksUP(
        @Param("site_id") String site_id,
        @Param("links_up") String links_up, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?2\",\"fields\":[\"links_up\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findNameANDStatusANDLinksUP(
        @Param("name") String name,
        @Param("status") String status,
        @Param("links_up") String links_up, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"?1\",\"fields\":[\"links_up\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findNameANDLinksUP(
        @Param("name") String name,
        @Param("links_up") String links_up, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"status\"]}},"
    +"{\"query_string\":{ \"query\":\"?1\",\"fields\":[\"links_up\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findStatusANDLinksUP(
        @Param("status") String status,
        @Param("links_up") String links_up, 
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"?0\",\"fields\":[\"links_up\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findLinksUP(
        @Param("links_up") String links_up, 
        Pageable pageable
    );

//##########################################################################
    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?3*\",\"fields\":[\"status\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDNameANDStatus(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("status") String status,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"name\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDName(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant, 
        @Param("name") String name,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"status\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenantANDStatus(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant,
        @Param("status") String status,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"tenant_name\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDTenant(
        @Param("site_id") String site_id, 
        @Param("tenant") String tenant,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"status\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDNameANDStatus(
        @Param("tenant") String tenant, 
        @Param("name") String name,
        @Param("status") String status,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDName(
        @Param("tenant") String tenant, 
        @Param("name") String name,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"status\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenantANDStatus(
        @Param("tenant") String tenant,
        @Param("status") String status,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"tenant_name\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findTenant(
        @Param("tenant") String tenant,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?2*\",\"fields\":[\"status\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDNameANDStatus(
        @Param("site_id") String site_id,
        @Param("name") String name,
        @Param("status") String status,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"name\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDName(
        @Param("site_id") String site_id,
        @Param("name") String name,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"status\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_idANDStatus(
        @Param("site_id") String site_id,
        @Param("status") String status,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findSite_id(
        @Param("site_id") String site_id,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"name\"]}},"
    +"{\"query_string\":{ \"query\":\"*?1*\",\"fields\":[\"status\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findNameANDStatus(
        @Param("name") String name,
        @Param("status") String status,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"name\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findName(
        @Param("name") String name,
        Pageable pageable
    );

    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"status\"]}}"
    +"]}}")
    Page<ElasticSiteSummary> findStatus(
        @Param("status") String status,
        Pageable pageable
    );
}
