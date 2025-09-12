package com.tracker_app.tracker.DataSource_elasticsearch.model;

import java.util.Date;
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
@Document(indexName = Indices.CIRCUITS_LOGS)
@Setting(settingPath = "static/es-settings.json")
public class ElasticCircuitsLog {
  @Id
  private String id;

  @Field(type = FieldType.Keyword)
  private String Timestamp;

  @Field(type = FieldType.Keyword)
  private String Router;

  @Field(type = FieldType.Keyword)
  private String SiteID;

  @Field(type = FieldType.Keyword)
  private String Mac;

  @Field(type = FieldType.Keyword)
  private String Mac_status;

  @Field(type = FieldType.Keyword)
  private String IMSI;

  @Field(type = FieldType.Keyword)
  private String Provider;

  @Field(type = FieldType.Keyword)
  private String Provider_status;

  @Field(type = FieldType.Keyword)
  private String Previous;

  @Field(type = FieldType.Keyword)
  private String Public_IP;

  @Field(type = FieldType.Keyword)
  private String Port;

  public ElasticCircuitsLog() {
  }

  public ElasticCircuitsLog(String id, String timestamp, String router, String siteID, String mac,
      String mac_status, String iMSI, String iMSI_status, String provider,
      String provider_status, String public_IP, String port, String previous) {
    this.id = id;
    Timestamp = timestamp;
    Router = router;
    SiteID = siteID;
    Mac = mac;
    Mac_status = mac_status;
    IMSI = iMSI;
    Provider = provider;
    Provider_status = provider_status;
    Public_IP = public_IP;
    Port = port;
    Previous = previous;
  }
}
