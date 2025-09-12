// package com.tracker_app.tracker.Config;

// import java.time.Duration;
// import org.elasticsearch.client.RestHighLevelClient;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.ComponentScan;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.elasticsearch.client.ClientConfiguration;
// import org.springframework.data.elasticsearch.client.RestClients;
// import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
// import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
// import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

// @Configuration
// @EnableElasticsearchRepositories(
//   basePackages = "com.tracker_app.tracker.DataSource_elasticsearch.repo"
// )
// @ComponentScan(
//   basePackages = { "com.tracker_app.tracker.DataSource_elasticsearch.service" }
// )
// public class DataSource_elasticsearch {

//   @Bean
//   public RestHighLevelClient client() {
//     ClientConfiguration clientConfiguration = ClientConfiguration
//       .builder()
//       .connectedTo("192.168.43.153:4678")
//       .withSocketTimeout(Duration.ofSeconds(10))
//       .build();

//     return RestClients.create(clientConfiguration).rest();
//   }

//   @Bean
//   public ElasticsearchOperations elasticsearchTemplate() {
//     return new ElasticsearchRestTemplate(client());
//   }
// }
