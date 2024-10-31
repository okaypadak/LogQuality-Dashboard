package tr.gov.ptt.LogQualityDashboard.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.GetIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.json.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class elasticsearchService {

    private final ElasticsearchClient elasticsearchClient;

    @Autowired
    public elasticsearchService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }


    public List<String> getIndexNames() throws IOException {
        GetIndexRequest request = new GetIndexRequest.Builder().index("*").build();
        GetIndexResponse response = elasticsearchClient.indices().get(request);

        return response.result().keySet().stream()
                .filter(indexName -> !indexName.startsWith("."))
                .collect(Collectors.toList());
    }

    public List<Map> getLogById(String indexName, String id) {
        try {

            BoolQuery boolQuery = BoolQuery.of(b ->
                    b.must(m ->
                            m.match(mq -> mq
                                    .field("logId")
                                    .query(id)
                            )
                    )
            );

            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index(indexName)
                    .query(Query.of(q -> q.bool(boolQuery)))
            );

            SearchResponse<Map> searchResponse = elasticsearchClient.search(searchRequest, Map.class);

            return searchResponse.hits().hits().stream()
                    .map(Hit::source)
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Map> getErrorWarn(String indexName) {
        try {

            BoolQuery boolQuery = BoolQuery.of(b -> b
                    .must(m1 -> m1.match(me -> me.field("INFO").query("ERROR")))
                    .must(m2 -> m2.match(mw -> mw.field("INFO").query("WARN")))
            );

            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index(indexName)
                    .query(Query.of(q -> q.bool(boolQuery)))
            );

            SearchResponse<Map> searchResponse = elasticsearchClient.search(searchRequest, Map.class);

            return searchResponse.hits().hits().stream()
                    .map(Hit::source)
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Map> search(String indexName, String keyword, String startTime, String endTime) {
        try {

            Instant startInstant = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(startTime + "T00:00:00Z"));
            Instant endInstant = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(endTime + "T23:59:59Z"));

            BoolQuery boolQuery = BoolQuery.of(
                    b -> b
                    .must(m -> m.range(r -> r
                                            .field("@timestamp")
                                            .gte(JsonData.of(startInstant.toString()))
                                            .lte(JsonData.of(endInstant.toString()))
                                    )
                            )
                    .must(m -> m.match(mq -> mq
                                    .field("message")
                                    .query(keyword)
                            )
                    )
            );

            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index(indexName)
                    .query(Query.of(q -> q.bool(boolQuery)))
            );

            SearchResponse<Map> searchResponse = elasticsearchClient.search(searchRequest, Map.class);


            return searchResponse.hits().hits().stream()
                    .map(Hit::source)
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
