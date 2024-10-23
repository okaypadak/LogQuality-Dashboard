package tr.gov.ptt.LogQualityDashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tr.gov.ptt.LogQualityDashboard.service.ElasticsearchSearchService;

import java.util.List;
import java.util.Map;

@Controller
public class LogController {

    private final ElasticsearchSearchService elasticsearchSearchService;

    @Autowired
    public LogController(ElasticsearchSearchService elasticsearchSearchService) {
        this.elasticsearchSearchService = elasticsearchSearchService;
    }

    @GetMapping("/logs")
    public String showLogsPage(Model model) {
        // Gerekli model verilerini ekleyebilirsiniz.
        return "logs"; // logs.html sayfasını döndürür
    }

    @PostMapping("/logs")
    public String searchLogs(@RequestParam String startTime, @RequestParam String endTime, @RequestParam(required = false) String keyword, Model model) {
        // Elasticsearch arama işlemi
        List<Map> results = elasticsearchSearchService.search(keyword, startTime, endTime);
        model.addAttribute("logs", results);
        return "logs"; // logs.html sayfasını döndürür
    }
}