package tr.gov.ptt.LogQualityDashboard.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
public class ElasticsearchSearchService {

    private final ElasticsearchClient elasticsearchClient;

    @Autowired
    public ElasticsearchSearchService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public List<Map> search(String keyword, String startTime, String endTime) {
        try {
            // Zaman aralığı için Instant nesneleri oluştur
            Instant startInstant = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(startTime + "T00:00:00Z"));
            Instant endInstant = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(endTime + "T23:59:59Z"));

            // Bool sorgusu oluştur
            BoolQuery boolQuery = BoolQuery.of(b ->
                    b.must(m -> m.range(r -> r
                                            .field("@timestamp") // Zaman damgasının bulunduğu alan adı
                                            .gte(JsonData.of(startInstant.toString()))
                                            .lte(JsonData.of(endInstant.toString()))
                                    )
                            )
                            .must(m -> m.match(mq -> mq
                                            .field("message") // Mesajın bulunduğu alan adı
                                            .query(keyword)
                                    )
                            )
            );

            // Arama isteği oluştur
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index("testapp_logs") // Burada arama yapılacak indeksin adını girin
                    .query(Query.of(q -> q.bool(boolQuery)))
            );

            SearchResponse<Map> searchResponse = elasticsearchClient.search(searchRequest, Map.class);

            // Sonuçları döndür
            return searchResponse.hits().hits().stream()
                    .map(Hit::source)
                    .toList();

        } catch (Exception e) {
            // Hata durumunda loglama veya farklı bir işlem yapabilirsiniz
            e.printStackTrace();
            return List.of();
        }
    }
}
