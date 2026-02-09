package com.gairola.GairolaSearchEngine.service;


import com.gairola.GairolaSearchEngine.entity.WebPage;
import com.gairola.GairolaSearchEngine.repository.WebPageRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public Page<WebPage> searchOracle(String q, int page, int size) {

        int start = page * size;
        int end   = start + size;

        List<WebPage> data = repo.searchOracle(
                q,
                "%" + q + "%",
                start,
                end
        );

        long total = repo.countOracle(
                q,
                "%" + q + "%"
        );

        Pageable pageable = PageRequest.of(page, size);

        return new PageImpl<>(data, pageable, total);
    }


    public record SearchResult(List<WebPage> results, String summary, long total) {}
}
