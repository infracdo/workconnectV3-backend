package com.tracker_app.tracker.DataSource_Atis.Repo;

import com.tracker_app.tracker.DataSource_Atis.Entity.sites_site;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface atis_repo extends CrudRepository<sites_site, String> {
  @Query("SELECT name FROM sites_site  WHERE site_id = :siteId")
  String findNameBySiteId(String siteId);
  
  @Query(value = "SELECT * FROM sites_site WHERE site_id =?1", nativeQuery = true)
  sites_site findInfoBySiteId(String siteId);
}
