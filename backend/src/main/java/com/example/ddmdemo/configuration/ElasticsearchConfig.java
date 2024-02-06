package com.example.ddmdemo.configuration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.erhlc.RestClients;


@Configuration
public class ElasticsearchConfig {


    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private int port;

    @Value("${elasticsearch.username}")
    private String userName;

    @Value("${elasticsearch.password}")
    private String password;


    @Bean
    public RestHighLevelClient elasticsearchClient() {
        System.out.println("RestHighLevelClient elasticsearchClient()");
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(host + ":" + port) // Adjust the Elasticsearch server URL
                .withBasicAuth(userName, password).build();

        return RestClients.create(clientConfiguration).rest();
    }
}
