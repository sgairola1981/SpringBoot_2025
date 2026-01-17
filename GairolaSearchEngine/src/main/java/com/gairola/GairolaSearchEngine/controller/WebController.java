package com.gairola.GairolaSearchEngine.controller;

import com.gairola.GairolaSearchEngine.entity.WebPage;
import com.gairola.GairolaSearchEngine.service.SearchService;
import com.gairola.GairolaSearchEngine.service.WebScraperService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WebController {

    private final WebScraperService scraperService;
    private final SearchService searchService;

    public WebController(WebScraperService scraperService, SearchService searchService) {
        this.scraperService = scraperService;
        this.searchService = searchService;
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String q,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        if (q != null && !q.trim().isEmpty()) {
            var result = searchService.search(q.trim(), page);
            model.addAttribute("results", result.results());
            model.addAttribute("summary", result.summary());
            model.addAttribute("total", result.total());
            model.addAttribute("query", q);
        }
        return "index";
    }

    @GetMapping("/scrape")
    public String singleScrape(@RequestParam String url, Model model) {
        WebPage page = scraperService.scrape(url);
        model.addAttribute("message", page != null ?
                "✅ Indexed: " + page.getTitle() : "❌ Failed to index: " + url);
        return "redirect:/";
    }

    @PostMapping("/batch-scrape")
    @ResponseBody
    public String batchScrape(@RequestBody List<String> urls) {
        int success = 0;
        for (String url : urls) {
            if (scraperService.scrape(url) != null) {
                success++;
            }
        }
        return String.format("✅ Indexed %d/%d websites", success, urls.size());
    }

    @GetMapping("/crawl")
    @ResponseBody
    public String fullCrawl(@RequestParam String url) {
        scraperService.crawlWebsite(url);
        return "🚀 Full website crawl started: " + url;
    }

    @GetMapping("/stats")
    @ResponseBody
    public String stats() {
        long count = scraperService.getPageCount();  // ✅ Works!
        return "📊 Total indexed pages: " + count;
    }
}