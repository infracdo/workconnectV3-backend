package com.tracker_app.tracker.DataSource_Atis.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "products_asset")
public class products_asset {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Long id;

  @Column(name = "property_code")
  private String property_code;

  @Column(name = "equipment_code")
  private String equipment_code;

  @Column(name = "status")
  private String status;

  @Column(name = "condition")
  private String condition;

  /*
    @ManyToOne
    private products_product product;
    @ManyToOne
    staging_area_id
    @ManyToOne
    private sites_site site;
    */
  @Column(name = "odoo_id")
  private Integer odoo_id;

  @Column(name = "serial_number")
  private String serial_number;

  @Column(name = "equipment_label")
  private String equipment_label;

  @Column(name = "is_active")
  private Boolean is_active;

  @Column(name = "assigned_to_id")
  private Integer assigned_to_id;
}
