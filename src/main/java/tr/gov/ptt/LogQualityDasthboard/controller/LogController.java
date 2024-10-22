package tr.gov.ptt.LogQualityDasthboard.controller;

import entry.LogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tr.gov.ptt.LogQualityDasthboard.service.LogService;

import java.util.List;

@RestController
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/logs/recent")
    public List<LogEntry> getRecentLogs() {
        return logService.getLogsInLast10Minutes();
    }
}