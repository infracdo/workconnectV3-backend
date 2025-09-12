package com.tracker_app.tracker.DataSource_Atis.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "products_product")
public class products_product {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "odoo_id")
  private Integer odoo_id;

  @Column(name = "equipment_type")
  private String equipment_type;

  @Column(name = "equipment_type_prefix")
  private String equipment_type_prefix;

  @Column(name = "locator_code")
  private String locator_code;

  @Column(name = "is_active")
  private Boolean is_active;

  @Column(name = "counter_dr")
  private Integer counter_dr;
}
