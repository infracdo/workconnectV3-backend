package com.tracker_app.tracker.DataSource_elasticsearch.repo;

import com.tracker_app.tracker.DataSource_elasticsearch.model.ElasticLog;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.Repository;

public interface ElasticLogRepo
  extends ElasticsearchRepository<ElasticLog, String> {
  // @Query("{\"bool\": {\"must\": [{\"match\": {\"Payload\": \"?0\"}}]}}")
  // List<ElasticLog> findByPayloadContainingStoreId(String storeId);

  @Query("{\"bool\": {\"must\": [{\"match\": {\"Payload.keyword\": \"?0\"}}]}}")
  List<ElasticLog> findByPayloadContainingStoreId(String storeId);

  // @Query(
  //   "{\"bool\": {\"must\": [{\"range\": {\"TimestampMilliSec.keyword\": {\"gte\": \"?0\", \"lte\": \"?1\", \"boost\": 2.0}}}]}}"
  // )
  // List<ElasticLog> findByTimestampBetween(long fromTimestamp, long toTimestamp);

  // @Query(
  //   "{\"bool\": {\"must\": [{\"range\": {\"TimestampMilliSec\": {\"gte\": ?0, \"lte\": ?1, \"include_lower\": true, \"include_upper\": true}}}]}}"
  // )
  // List<ElasticLog> findByTimestampMilliSecBetween(
  //   long fromTimestamp,
  //   long toTimestamp
  // );
  List<ElasticLog> findByTimestampMilliSec(long timestampMilliSec);
}
