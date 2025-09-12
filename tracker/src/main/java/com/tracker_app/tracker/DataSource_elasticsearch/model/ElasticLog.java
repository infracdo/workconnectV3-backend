package com.tracker_app.tracker.DataSource_elasticsearch.model;

import com.tracker_app.tracker.DataSource_elasticsearch.helper.Indices;
import java.security.Timestamp;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.apache.http.protocol.HTTP;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Data
@Document(indexName = Indices.WORKCONNECT_INDEX)
@Setting(settingPath = "static/es-settings.json")
public class ElasticLog {

  @Id
  @Field(type = FieldType.Keyword)
  private String id;

  // @Field(type = FieldType.Keyword)
  // private String Timestamp;

  @Field(type = FieldType.Keyword)
  private long TimestampMilliSec;

  @Field(type = FieldType.Keyword)
  private String Status; // [HTTP CODE, Successful, Failed, Message]

  @Field(type = FieldType.Keyword)
  private String User; // keyclock username

  // @Field(type = FieldType.Keyword)
  // private String Name; // keyclock username

  @Field(type = FieldType.Keyword)
  private String Tenant;

  @Field(type = FieldType.Keyword)
  private String Service; // API URL

  @Field(type = FieldType.Keyword)
  private List<String> RoleGroup;

  @Field(type = FieldType.Keyword)
  private String Payload; // data sent to API/service

  @Field(type = FieldType.Keyword)
  private String Action; // data sent to API/service

  @Field(type = FieldType.Keyword)
  private String ClientIp; // data sent to API/service

  public ElasticLog() {}

  public ElasticLog(
    // String timestamp,
    String status,
    String user,
    String tenant,
    String service,
    List<String> roleGroup,
    String payload,
    String action,
    long timestampMilliSec,
    String clientIp
  ) {
    // Timestamp = timestamp;
    Status = status;
    User = user;
    Tenant = tenant;
    Service = service;
    RoleGroup = roleGroup;
    Payload = payload;
    Action = action;
    TimestampMilliSec = timestampMilliSec;
    ClientIp = clientIp;
    // Name = name;
  }
}
