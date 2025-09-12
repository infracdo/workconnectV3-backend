package com.tracker_app.tracker.DataSource_elasticsearch.repo;

import com.tracker_app.tracker.DataSource_elasticsearch.model.DC_Active;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface  DC_Active_repository extends ElasticsearchRepository<DC_Active, String> {
    @Query("{\"bool\": {\"must\": ["
    +"{\"query_string\":{ \"query\":\"*?0*\",\"fields\":[\"site_id\"]}}"
    +"]}}")
    DC_Active findBySiteID(String site_id);
}
