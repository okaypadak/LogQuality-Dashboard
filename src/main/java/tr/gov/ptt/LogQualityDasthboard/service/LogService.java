package tr.gov.ptt.LogQualityDasthboard.service;

import entry.LogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.gov.ptt.LogQualityDasthboard.repository.LogEntryRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogEntryRepository logEntryRepository;

    public List<LogEntry> getLogsByDateAndMessage(String index, LocalDateTime start, LocalDateTime end, String message) {
        return logEntryRepository.findByIndexAndTimestampBetweenAndMessage(index, start, end, message);
    }

    public List<LogEntry> getLogsInLast10Minutes() {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        return logEntryRepository.findRecentLogs(tenMinutesAgo);
    }
}