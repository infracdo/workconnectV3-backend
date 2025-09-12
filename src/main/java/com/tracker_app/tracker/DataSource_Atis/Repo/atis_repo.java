package com.tracker_app.tracker.DataSource_Atis.Repo;

import com.tracker_app.tracker.DataSource_Atis.Entity.sites_site;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface atis_repo extends CrudRepository<sites_site, String> {
  @Query("SELECT name FROM sites_site  WHERE site_id = :siteId")
  String findNameBySiteId(String siteId);
}
