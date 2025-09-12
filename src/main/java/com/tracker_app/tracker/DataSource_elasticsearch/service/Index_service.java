package com.tracker_app.tracker.DataSource_elasticsearch.service;

import java.util.List;

import javax.annotation.PostConstruct;

import com.tracker_app.tracker.DataSource_elasticsearch.helper.Indices;
import com.tracker_app.tracker.DataSource_elasticsearch.helper.util;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Index_service {
    private static final List<String> INDICES = List.of(Indices.SITE_SUM_INDEX,Indices.DC_ACTIVE);
    private final RestHighLevelClient client;

    @Autowired
    public Index_service(RestHighLevelClient client) {
        this.client = client;
    }
    @PostConstruct
    public void tryToCreateIndices() {
        recreateIndices(false);
    }

    public void recreateIndices(final boolean deleteExisting) {
        final String settings = util.loadAsString("static/es-settings.json");

        if (settings == null) {
            return;
        }

        for (final String indexName : INDICES) {
            try {
                final boolean indexExists = client
                        .indices()
                        .exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
                if (indexExists) {
                    if (!deleteExisting) {
                        continue;
                    }

                    client.indices().delete(
                            new DeleteIndexRequest(indexName),
                            RequestOptions.DEFAULT
                    );
                }

                final CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
                createIndexRequest.settings(settings, XContentType.JSON);

                final String mappings = loadMappings(indexName);
                if (mappings != null) {
                    createIndexRequest.mapping(mappings, XContentType.JSON);
                }

                client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            } catch (final Exception e) {

            }
        }
    }

    private String loadMappings(String indexName) {
        final String mappings = util.loadAsString("static/mappings/" + indexName + ".json");
        if (mappings == null) {
            return null;
        }
        return mappings;
    }

}
