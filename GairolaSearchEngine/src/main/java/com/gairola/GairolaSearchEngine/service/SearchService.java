package com.gairola.GairolaSearchEngine.service;


import com.gairola.GairolaSearchEngine.entity.WebPage;
import com.gairola.GairolaSearchEngine.repository.WebPageRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchService {

    private final WebPageRepository repo;

    public SearchService(WebPageRepository repo) {
        this.repo = repo;
    }

    public Page<WebPage> search(String query, Pageable pageable) {

        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        String clean = query.toLowerCase().trim();

        List<WebPage> content = repo.searchPagesManual(
                clean,                    // for DBMS_LOB.INSTR
                "%" + clean + "%",        // for LIKE title
                offset,
                offset + pageSize
        );

        long total = repo.countByQuery(
                clean,
                "%" + clean + "%"
        );

        return new PageImpl<>(content, pageable, total);
    }

    // ----------------------------


    public SearchResult search(String query, int page) {

        if (query == null || query.isBlank()) {
            long total = repo.count();
            return new SearchResult(List.of(),
                    generateSummary(0, query, total), total);
        }

        String clean = query.toLowerCase().trim();
        Pageable pageable = PageRequest.of(page, 20);

        Page<WebPage> resultsPage =
                repo.searchPages(clean, "%" + clean + "%", pageable);

        String summary = generateSummary(
                resultsPage.getTotalElements(), query, repo.count());

        return new SearchResult(
                resultsPage.getContent(),
                summary,
                repo.count()
        );
    }

    private String generateSummary(long found, String query, long total) {
        if (found == 0) {
            return "🔍 No results for \"" + query + "\" (" + total + " indexed pages)";
        }
        return "✅ Found " + found + "/" + total + " pages for \"" + query + "\"";
    }

    public Page<WebPage> searchOracle(String query, int page, int size) {

        int start = page * size;
        int end = start + size;

        List<WebPage> results = repo.searchOracle(
                query,
                "%" + query.toLowerCase() + "%",
                start,
                end
        );

        long total = repo.countOracle(
                query,
                "%" + query.toLowerCase() + "%"
        );

        return new PageImpl<>(
                results,
                PageRequest.of(page, size),
                total
        );
    }

    public Page<WebPage> searchOracle_Final(String query, Pageable pageable) {

        int size = pageable.getPageSize();
        int page = pageable.getPageNumber();

        int startRow = page * size;
        int endRow   = (page + 1) * size;

        List<WebPage> rawResults =
                repo.searchOracle11gNoIndex(query.toLowerCase(), startRow, endRow);

        long total = repo.countOracle11gNoIndex(query.toLowerCase());

        // 🔥 1. Remove duplicate titles
        Set<String> seenTitles = new HashSet<>();
        List<WebPage> uniqueResults = new ArrayList<>();

        for (WebPage wp : rawResults) {
            if (wp.getTitle() != null && seenTitles.add(wp.getTitle())) {
                uniqueResults.add(wp);
            }
        }

        // 🔥 2. Domain diversity (max 2 per domain)
        Map<String, Integer> domainCount = new HashMap<>();
        List<WebPage> filtered = new ArrayList<>();

        for (WebPage wp : uniqueResults) {
            try {
                String domain = new java.net.URL(wp.getUrl()).getHost();

                if (domainCount.getOrDefault(domain, 0) < 2) {
                    filtered.add(wp);
                    domainCount.put(domain,
                            domainCount.getOrDefault(domain, 0) + 1);
                }

            } catch (Exception ignored) {}
        }

        return new PageImpl<>(filtered, pageable, total);
    }

    public Page<WebPage> searchOracle_Final1234(String query, Pageable pageable) {

        int size = pageable.getPageSize();
        int page = pageable.getPageNumber(); // 0-based

        int startRow = page * size;
        int endRow   = (page + 1) * size;

        List<WebPage> content =
                repo.searchOracle11gNoIndex(query, startRow, endRow);

        long total = repo.countOracle11gNoIndex(query);

        return new PageImpl<>(content, pageable, total);
    }




    public record SearchResult(List<WebPage> results, String summary, long total) {}
}
