package com.tracker_app.tracker.DataSource_Atis.Repo;

import com.tracker_app.tracker.DataSource_Atis.Entity.products_asset;
import java.util.List;
import java.util.Map;
import javax.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

public interface products_assets_repo
  extends JpaRepository<products_asset, Long> {
  @QueryHints(
    @QueryHint(
      name = org.hibernate.annotations.QueryHints.CACHEABLE,
      value = "true"
    )
  )
  @Query(value = "select * from products_asset", nativeQuery = true)
  public List<products_asset> get_Assets();

  @QueryHints(
    @QueryHint(
      name = org.hibernate.annotations.QueryHints.CACHEABLE,
      value = "true"
    )
  )
  @Query(
    value = "select pa.id, ss.site_id, pp.name, pa.serial_number, pa.property_code, pa.equipment_code, pa.condition, pa.status " +
    "from products_asset pa, products_product pp, sites_site ss " +
    "where (pa.product_id = pp.id) " +
    "AND (pa.site_id = ss.id) " +
    "AND (:site_id IS NULL OR ss.site_id LIKE :site_id%) ",
    nativeQuery = true
  )
  public List<Map<String, Object>> find_Assets_with_siteid(
    @Param("site_id") String site_id
  );

  @QueryHints(
    @QueryHint(
      name = org.hibernate.annotations.QueryHints.CACHEABLE,
      value = "true"
    )
  )
  @Query(
    value = "select pa.id, ss.site_id, pp.name, pa.serial_number, pa.property_code, pa.equipment_code, pa.condition, pa.status " +
    "from products_asset pa, products_product pp, sites_site ss " +
    "where (pa.product_id = pp.id) " +
    "AND (pa.site_id = ss.id) ",
    nativeQuery = true
  )
  public Page<Map<String, Object>> find_Assets(Pageable pageable);

  @Query(
    value = "select pa.id, pp.name, pa.serial_number, pa.property_code, pa.equipment_code, pa.condition, pa.status " +
    "from products_asset pa, products_product pp " +
    "where " +
    "(pa.status = :status) " +
    "AND (pa.staging_area_id = :staging_area_id) " +
    "AND (pa.product_id = pp.id) ",
    nativeQuery = true
  )
  public Page<Map<String, Object>> get_assets_list_by_status(
    @Param("status") String status,
    @Param("staging_area_id") Integer staging_area_id,
    Pageable pageable
  );

  @Query(
    value = "select pa.id, pp.name, pa.serial_number, pa.property_code, pa.equipment_code, pa.condition, pa.status " +
    "from products_asset pa, products_product pp " +
    "where " +
    "(pa.id = :id) " +
    "AND (pa.product_id = pp.id) ",
    nativeQuery = true
  )
  public Map<String, Object> get_asset_by_id(@Param("id") Integer id);
}
