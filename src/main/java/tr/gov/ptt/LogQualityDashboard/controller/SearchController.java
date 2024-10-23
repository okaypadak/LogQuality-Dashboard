package tr.gov.ptt.LogQualityDashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tr.gov.ptt.LogQualityDashboard.service.ElasticsearchSearchService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final ElasticsearchSearchService searchService;

    @Autowired
    public SearchController(ElasticsearchSearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public List<Map> search(
            @RequestParam String keyword,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        return searchService.search(keyword, startTime, endTime);
    }
}
