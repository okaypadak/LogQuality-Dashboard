package tr.gov.ptt.LogQualityDashboard.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.List;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        // REST client oluşturuluyor
        RestClient restClient = RestClient.builder(new HttpHost("localhost", 30100)).build();

        // Transport client ayarı
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        // ElasticsearchClient oluşturuluyor
        return new ElasticsearchClient(transport);
    }
}