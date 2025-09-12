package com.tracker_app.tracker.DataSource_elasticsearch.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class Indices {

  // public static final String PERSON_INDEX = "site_summary_index";
  public static final String SITE_SUM_INDEX = "#{@environment.getProperty('index.sites')}";
  public static final String DC_ACTIVE = "#{@environment.getProperty('index.dc_active')}";
  public static final String WORKCONNECT_INDEX = "#{@environment.getProperty('index.workconnectv2')}";
  public static final String CIRCUITS_LOGS = "#{@environment.getProperty('index.circuits_logs')}";
}
