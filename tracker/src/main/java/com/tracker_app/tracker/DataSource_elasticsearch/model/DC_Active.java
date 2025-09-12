package com.tracker_app.tracker.DataSource_elasticsearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import com.tracker_app.tracker.DataSource_elasticsearch.helper.Indices;

import lombok.Data;

@Data
@Document(indexName = Indices.DC_ACTIVE)
@Setting(settingPath = "static/es-settings.json")
public class DC_Active {
    @Id
    @Field(type=FieldType.Keyword)
    private String id;
    private String site_id;
    private String status;
}
