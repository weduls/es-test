package com.wedul.estest.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(ElasticsearchConfiguration.class)
public class RestHighLevelClientConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient(ElasticsearchConfiguration configuration) {
        List<HttpHost> httpHosts = new ArrayList<>();
        httpHosts.add(new HttpHost(configuration.getHost(), configuration.getPort()));
        return new RestHighLevelClient(RestClient.builder(httpHosts.toArray(new HttpHost[0])));
    }

}
