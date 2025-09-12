package com.tracker_app.tracker.DataSource_Atis.Helper;

import com.tracker_app.tracker.DataSource_Atis.Repo.products_assets_repo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class products_helper {

  @Autowired
  private products_assets_repo asset_repo;

  public ResponseEntity<Object> getAssets(
    Integer pageNo,
    Integer pageSize,
    String site_id
  ) {
    try {
      System.out.println(site_id);
      List<Map<String, Object>> assets = asset_repo.find_Assets_with_siteid(
        site_id
      );
      return new ResponseEntity<Object>(assets, HttpStatus.OK);
    } catch (Exception e) {
      System.out.println("no site_id");

      Pageable limit = PageRequest.of(
        pageNo,
        pageSize,
        Sort.by("id").descending()
      );
      Page<Map<String, Object>> pg_asset = asset_repo.find_Assets(limit);
      List<Map<String, Object>> assets = new ArrayList<Map<String, Object>>();
      for (Map<String, Object> products_asset : pg_asset) {
        assets.add(products_asset);
      }
      System.out.println("Get assets " + assets);

      //List<Map<String, Object>> assets = asset_repo.find_Assets();
      return new ResponseEntity<Object>(assets, HttpStatus.OK);
    }
  }
}
