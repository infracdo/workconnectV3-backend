package com.tracker_app.tracker.DataSource_elasticsearch.helper;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.stereotype.Component;

@Component
public class ElasticSearchQueryBuilder {

  public QueryBuilder buildQuery(String field, long fromDate, long toDate) {
    RangeQueryBuilder rangeQuery = QueryBuilders
      .rangeQuery(field)
      .gte(fromDate)
      .lte(toDate);

    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(rangeQuery);

    return boolQuery;
  }
}
