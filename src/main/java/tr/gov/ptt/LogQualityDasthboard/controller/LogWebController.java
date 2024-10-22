package tr.gov.ptt.LogQualityDasthboard.controller;

import entry.LogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tr.gov.ptt.LogQualityDasthboard.service.LogService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class LogWebController {

    @Autowired
    private LogService logService;

    @GetMapping("/logs")
    public String getLogs(@RequestParam String index,
                          @RequestParam String startDate,
                          @RequestParam String endDate,
                          @RequestParam(required = false) String message,
                          Model model) {
        // Date conversion
        LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");

        List<LogEntry> logEntries = logService.getLogsByDateAndMessage(index, start, end, message);
        model.addAttribute("logEntries", logEntries);
        return "index";
    }

    @GetMapping("/simpleLogs")
    @ResponseBody
    public List<LogEntry> getLogs(@RequestParam String index,
                                  @RequestParam String startDate,
                                  @RequestParam String endDate,
                                  @RequestParam(required = false) String message) {
        // Date conversion
        LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");

        return logService.getLogsByDateAndMessage(index, start, end, message);
    }
}
