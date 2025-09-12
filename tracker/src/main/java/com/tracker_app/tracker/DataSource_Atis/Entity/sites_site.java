package com.tracker_app.tracker.DataSource_Atis.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Entity // labels an entity that can be used for JPA
@Table(name = "sites_site")
public class sites_site {

  @Id
  @Field(type = FieldType.Keyword)
  private String id;

  private String site_id;

  public String getSite_id() {
    return site_id;
  }

  private String name;

  public String getName() {
    return name;
  }

  private String address;

  public String getAddress() {
    return address;
  }
}
