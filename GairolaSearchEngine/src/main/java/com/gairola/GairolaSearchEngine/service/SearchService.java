package com.gairola.GairolaSearchEngine.service;


import com.gairola.GairolaSearchEngine.entity.WebPage;
import com.gairola.GairolaSearchEngine.repository.WebPageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SearchService {

    private final WebPageRepository repo;

    public SearchService(WebPageRepository repo) {
        this.repo = repo;
    }

    public SearchResult search(String query, int page) {
        if (query == null || query.trim().isEmpty()) {
            return new SearchResult(List.of(), null, 0);
        }

        var pageable = PageRequest.of(page, 20);
        var resultsPage = repo.findByContentOrTitleContainingIgnoreCase(query.trim(), pageable);

        // ✅ Convert Page to List for Thymeleaf
        List<WebPage> results = resultsPage.getContent();
        String summary = generateSummary(resultsPage.getTotalElements(), query);

        return new SearchResult(results, summary, resultsPage.getTotalElements());
    }

    private String generateSummary(long count, String query) {
        if (count == 0) return null;

        return String.format("Found %d pages about \"%s\". Top results include Spring Boot tutorials, Java documentation, and web development resources.",
                count, query);
    }

    // ✅ FIXED: Proper record (Java 17+) OR regular class
    public record SearchResult(List<WebPage> results, String summary, long total) {}

    // OR use this regular class if Java < 17:
    /*
    public static class SearchResult {
        private final List<WebPage> results;
        private final String summary;
        private final long total;

        public SearchResult(List<WebPage> results, String summary, long total) {
            this.results = results;
            this.summary = summary;
            this.total = total;
        }

        // Getters
        public List<WebPage> getResults() { return results; }
        public String getSummary() { return summary; }
        public long getTotal() { return total; }
    }
    */
}