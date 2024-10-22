package tr.gov.ptt.LogQualityDashboard.repository;


import tr.gov.ptt.LogQualityDashboard.entry.LogEntry;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LogEntryRepository extends ElasticsearchRepository<LogEntry, String> {

    @Query("{\"range\": {\"@timestamp\": {\"gte\": \"?0\"}}}")
    List<LogEntry> findRecentLogs(LocalDateTime tenMinutesAgo);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"index\": \"?0\"}}, " +
            "{\"range\": {\"@timestamp\": {\"gte\": \"?1\", \"lte\": \"?2\"}}}, " +
            "{\"wildcard\": {\"message\": \"*?3*\"}}]}}")
    List<LogEntry> findByIndexAndTimestampBetweenAndMessage(String index, String start, String end, String message);
}