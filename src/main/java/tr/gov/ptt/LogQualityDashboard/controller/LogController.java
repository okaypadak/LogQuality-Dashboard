package tr.gov.ptt.LogQualityDashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tr.gov.ptt.LogQualityDashboard.service.elasticsearchService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class LogController {

    private final elasticsearchService elasticsearchService;

    @Autowired
    public LogController(elasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }


    @GetMapping("/logs")
    public String showLogSearchForm(Model model) throws IOException {
        List<String> indexNames = elasticsearchService.getIndexNames();
        model.addAttribute("indexNames", indexNames);
        return "logs";
    }

    @PostMapping("/logs")
    public String searchLogs(@RequestParam String indexName, @RequestParam String startTime, @RequestParam String endTime, @RequestParam(required = false) String keyword, Model model) throws IOException {
        List<Map> results = elasticsearchService.search(indexName, keyword, startTime, endTime);
        List<String> indexNames = elasticsearchService.getIndexNames();
        model.addAttribute("logs", results);
        model.addAttribute("indexNames", indexNames);
        model.addAttribute("selectedIndexName", indexName);
        model.addAttribute("goruldu", true);

        return "logs";
    }

    @GetMapping("/logId/{indexName}/{logId}")
    public String getLogDetails(@PathVariable String indexName, @PathVariable String logId, Model model) {
        List<Map> logDetails = elasticsearchService.getLogById(indexName, logId);
        model.addAttribute("logDetails", logDetails);
        model.addAttribute("logId", logId);
        return "logBlock";
    }
}