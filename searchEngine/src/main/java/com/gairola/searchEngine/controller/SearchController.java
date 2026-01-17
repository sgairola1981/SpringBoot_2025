package com.gairola.searchEngine.controller;
import com.gairola.searchEngine.entity.WebPage;
import com.gairola.searchEngine.repository.WebPageRepository;
import com.gairola.searchEngine.service.WebScraperService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class SearchController {

    private final WebPageRepository repository;
    private final WebScraperService scraperService;

    public SearchController(WebPageRepository repository, WebScraperService scraperService) {
        this.repository = repository;
        this.scraperService = scraperService;
    }

    // 🏠 Home / Search Page
    @GetMapping
    public String home(Model model) {
        return "search";
    }
    // 🏠 Home / Search Page
    @GetMapping("/searchBatch")
    public String home1(Model model) {
        return "searchBatch";
    }

    // 🔍 Search Results
    @GetMapping("/search")
    public String search(@RequestParam("q") String query, Model model) {
        List<WebPage> results = repository.search(query.trim());
        String summary = generateSummary(results.size(), query);

        model.addAttribute("results", results);
        model.addAttribute("query", query);
        model.addAttribute("summary", summary);
        return "search";
    }

    // 🕷️ Scrape & Index Web Page (GET & POST both work)
    @GetMapping("/scrape")
    @PostMapping("/scrape")
    @ResponseBody
    public WebPage scrape(@RequestParam("url") String urlParam) {
        // 🔧 Fix: Decode URL (handles %3A%2F etc.)
        String url = URLDecoder.decode(urlParam, StandardCharsets.UTF_8);
        System.out.println("🌐 Scraping: " + url);

        WebPage page = scraperService.scrape(url);
        System.out.println("✅ Saved: " + page.getTitle());
        return page;
    }

    // 📊 Debug: List all indexed pages
    @GetMapping("/pages")
    @ResponseBody
    public List<WebPage> listPages() {
        return repository.findAll();
    }

    // 🧹 Clear all data (for testing)
    @DeleteMapping("/clear")
    @ResponseBody
    public String clearAll() {
        repository.deleteAll();
        return "🗑️ Cleared all indexed pages";
    }

    // Private helper
    private String generateSummary(int count, String query) {
        if (count == 0) {
            return "No results found for '" + query + "'. Try scraping more pages!";
        }
        return String.format("✨ Found %d result%s for '%s'",
                count, count > 1 ? "s" : "", query);
    }
    @PostMapping("/batch-scrape")
    @ResponseBody
    public List<WebPage> batchScrape(@RequestBody List<String> urls) {
        List<WebPage> results = new ArrayList<>();
        for (String url : urls) {
            try {
                String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8);
                WebPage page = scraperService.scrape(decodedUrl);
                results.add(page);
                System.out.println("✅ Indexed: " + page.getTitle());
            } catch (Exception e) {
                System.err.println("❌ Failed: " + url + " - " + e.getMessage());
            }
        }
        return results;
    }
}