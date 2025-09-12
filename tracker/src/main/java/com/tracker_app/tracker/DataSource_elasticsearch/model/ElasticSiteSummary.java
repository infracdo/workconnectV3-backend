package com.tracker_app.tracker.DataSource_elasticsearch.model;

import java.sql.Timestamp;
import java.util.List;

import com.tracker_app.tracker.DataSource_WorkConnect.Entity.site_circuit_status;
import com.tracker_app.tracker.DataSource_WorkConnect.Entity.sites_summary;
import com.tracker_app.tracker.DataSource_elasticsearch.helper.Indices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import lombok.Data;

@Data
@Document(indexName = Indices.SITE_SUM_INDEX)
@Setting(settingPath = "static/es-settings.json")
public class ElasticSiteSummary {
    @Id
    @Field(type=FieldType.Keyword)
    private String id;
    private String site_id;
    private String name;
    private String island_group; 
    private String address;
    private String contact_person;
    private String contact_nos;
    private String tenant_name;

    private String zipcode;

    private String date_created;
    private String date_last_updated;

    private String region;
    private String status;
    private Integer links_up;
    private Float latitude; 
    private Float longitude;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<site_circuit_status> circuits;
}
