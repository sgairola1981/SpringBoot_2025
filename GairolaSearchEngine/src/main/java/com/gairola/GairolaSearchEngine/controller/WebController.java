package com.gairola.GairolaSearchEngine.controller;

import com.gairola.GairolaSearchEngine.entity.WebPage;
import com.gairola.GairolaSearchEngine.service.SearchService;
import com.gairola.GairolaSearchEngine.service.WebScraperService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        model.addAttribute("page", Page.empty());
        model.addAttribute("query", "");
        model.addAttribute("page", Page.empty()); // 🔥 REQUIRED
        return "index";
    }


    @GetMapping("/searchN")
    public String search9999(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        if (q == null || q.trim().isEmpty()) {
            model.addAttribute("results", null);
            model.addAttribute("page", Page.empty());
            model.addAttribute("query", "");
            return "index";
        }

        Page<WebPage> resultPage =
                searchService.searchOracle(q.trim(), page, size);

        model.addAttribute("results", resultPage.getContent());
        model.addAttribute("page", resultPage);
        model.addAttribute("query", q);
        model.addAttribute("summary",
                "Found " + resultPage.getTotalElements() + " results");
        System.out.println("Total Elements: " + resultPage.getTotalElements());
        System.out.println("Total Pages: " + resultPage.getTotalPages());
        System.out.println("Current Page: " + resultPage.getNumber());

        return "index";
    }


    @GetMapping("/searchN999999")
    public String search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        int size = 10; // 👈 THIS controls "10 results at a time"
        PageRequest pageable = PageRequest.of(page, size);

        Page<WebPage> resultPage = searchService.searchOracle_Final(q, pageable);

        model.addAttribute("query", q);
        model.addAttribute("results", resultPage.getContent());
        model.addAttribute("page", resultPage);

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