package com.gairola.GairolaSearchEngine.service;


import com.gairola.GairolaSearchEngine.entity.WebPage;
import com.gairola.GairolaSearchEngine.repository.WebPageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;@Service
public class SearchService {
    private final WebPageRepository repo;

    public SearchService(WebPageRepository repo) {
        this.repo = repo;
    }


    //********************************

    public Page<WebPage> search(String query, Pageable pageable) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        List<WebPage> content = repo.searchPagesManual(
                "%" + query.toLowerCase() + "%",  // Add % wildcards
                offset,
                offset + pageSize
        );

        long total = repo.countByQuery("%" + query.toLowerCase() + "%");

        return new PageImpl<>(content, pageable, total);
    }



    //*************************************

    public SearchResult search(String query, int page) {
        if (query == null || query.trim().isEmpty()) {
            return new SearchResult(List.of(),
                    generateSummary(0, query, repo.count()), repo.count());
        }

        String searchQuery = "%" + query.trim().toLowerCase() + "%";
        var pageable = PageRequest.of(page, 20);
        var resultsPage = repo.searchPages(searchQuery, pageable);  // ✅ Native query

        List<WebPage> results = resultsPage.getContent();
        String summary = generateSummary(resultsPage.getTotalElements(), query, repo.count());

        return new SearchResult(results, summary, repo.count());
    }

    private String generateSummary(long foundCount, String query, long totalIndexed) {
        if (foundCount == 0) {
            return String.format("""
                    🔍 No results for "%s" (%d/%d indexed pages).
                    
                    💡 **Quick fixes:**
                    • Try "spring", "html", "thymeleaf", or "boot"
                    • Index more sites using buttons below
                    • Check spelling
                    
                    📊 %d pages ready!""",
                    query, foundCount, totalIndexed, totalIndexed);
        }
        return String.format("✅ Found %d/%d pages about \"%s\".", foundCount, totalIndexed, query);
    }

    public record SearchResult(List<WebPage> results, String summary, long total) {}
}
