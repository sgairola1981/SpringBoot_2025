package com.gairola.GairolaSearchEngine.service;

import com.gairola.GairolaSearchEngine.entity.SearchResult;
import com.gairola.GairolaSearchEngine.entity.WebPage;
import com.gairola.GairolaSearchEngine.repository.WebPageRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.hibernate.internal.util.StringHelper.count;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepthScraperService {

    private final WebPageRepository repository;
   // private final TaskExecutor crawlerExecutor;
    private final @Qualifier("crawlerExecutor") TaskExecutor crawlerExecutor;

    private final Set<String> visited = ConcurrentHashMap.newKeySet();
    private final Map<String, Map<String, Integer>> searchIndex = new ConcurrentHashMap<>();
    private static final Set<String> STOP_WORDS = Set.of(
            "the","and","for","with","that","this",
            "are","was","were","from","have","has"
    );

    public void startDepthIndex(String rootUrl, HttpSession session) {

        final String normalizedRootUrl =
                rootUrl.startsWith("http")
                        ? rootUrl
                        : "https://" + rootUrl;

        visited.clear();
        searchIndex.clear();

        DepthIndexStatus status =
                new DepthIndexStatus(normalizedRootUrl, 6, 1000);

        status.setStatus("running");
        session.setAttribute("depthIndex", status);

        crawlerExecutor.execute(() -> {
            try {
                crawl(normalizedRootUrl, 0, status, session);

                status.setStatus("completed");
                session.setAttribute("depthIndex", status);
                log.info("✅ Depth indexing completed: {}", normalizedRootUrl);

            } catch (Exception e) {
                log.error("❌ Error in depth indexing", e);
                status.setStatus("error");
            }
        });
    }

    private void crawl(String url,
                       int depth,
                       DepthIndexStatus status,
                       HttpSession session) {

        if ("stopped".equals(status.getStatus())) return;

        if (status.getTotalPages().get() >= status.getMaxPages()) {
            log.info("Reached max pages limit");
            return;
        }
        if (depth > status.getMaxDepth()) return;
        if (!visited.add(url)) return;

        try {
            log.info("🌐 Crawling depth {} -> {}", depth, url);

            Connection.Response response = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .ignoreHttpErrors(true)
                    .execute();

            if (response.statusCode() != 200) {
                log.warn("Skipping non-200 page: {}", url);
                return;
            }


            Document doc = response.parse();

            WebPage page = saveOrUpdate(url, depth, doc);

            status.getIndexedPages().add(page);
            status.getTotalPages().incrementAndGet();

            indexPage(page);
            updateProgress(status, session);

            // Recursive call WITHOUT executor
            if (depth < status.getMaxDepth()) {
                for (String link : extractInternalLinks(doc, status.getRootUrl())) {
                    crawl(link, depth + 1, status, session);
                }
            }

        } catch (Exception e) {
            log.warn("❌ Failed to crawl: {}", url);
        }
    }

     // -------------------------------------------------
    // SEARCH
    // -------------------------------------------------

    public List<SearchResult> search(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        final String normalizedKeyword = keyword.toLowerCase();

        Map<String, Integer> urlScores =
                searchIndex.getOrDefault(normalizedKeyword, Map.of());

        return urlScores.entrySet().stream()
                .map(entry -> repository.findByUrl(entry.getKey())
                        .map(page -> new SearchResult(
                                page,
                                calculateScore(page, normalizedKeyword)
                        ))
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(SearchResult::getScore).reversed())
                .toList();
    }

    private int calculateScore(WebPage page, String keyword) {
        int score = 0;

        score += count(page.getTitle(), keyword) * 5;
        score += count(page.getContent(), keyword);

        return score;
    }


    private void indexPage(WebPage page) {
        String url = page.getUrl();
        String text = (page.getTitle() + " " + page.getContent()).toLowerCase();
        String[] tokens = text.split("\\W+");

        Map<String, Integer> wordCount = new HashMap<>();

        for (String token : tokens) {
            if (token.length() < 3 || STOP_WORDS.contains(token)) continue;
            wordCount.merge(token, 1, Integer::sum);
        }

        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            searchIndex
                    .computeIfAbsent(entry.getKey(), k -> new ConcurrentHashMap<>())
                    .put(url, entry.getValue());
        }
    }

    // -------------------------------------------------
    // HELPERS
    // -------------------------------------------------

    private WebPage saveOrUpdate(String url, int depth, Document doc) {
        WebPage page = repository.findByUrl(url).orElse(new WebPage());

        page.setUrl(url);
        page.setTitle(truncate(doc.title(), 200));
        page.setContent(truncate(doc.body().text(), 5000));
        page.setDepth(depth);
        page.setScrapedAt(LocalDateTime.now());

        repository.save(page);

        log.info("✅ Indexed [Depth {}] {}", depth, url);

        return page;
    }

    private List<String> extractInternalLinks(Document doc, String rootUrl) {

        Elements links = doc.select("a[href]");

        return links.stream()
                .map(link -> link.attr("abs:href"))
                .filter(this::isValid)
                .filter(abs -> isSameDomain(abs, rootUrl))
                .distinct()
                .limit(10)
                .toList();
    }

    private boolean isValid(String url) {
        return url.startsWith("http")
                && !url.contains("#")
                && url.length() < 2000;
    }

    private void updateProgress(DepthIndexStatus status, HttpSession session) {
        long start = status.getStartTime()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli();

        status.updateProgress(start);
        session.setAttribute("depthIndex", status);
    }

    private String truncate(String text, int max) {
        if (text == null) return "";
        return text.length() > max ? text.substring(0, max) + "..." : text;
    }
    private boolean isSameDomain(String url, String rootUrl) {
        try {
            String rootHost = new java.net.URL(rootUrl).getHost().replace("www.", "");
            String host = new java.net.URL(url).getHost().replace("www.", "");
            return host.equalsIgnoreCase(rootHost);
        } catch (Exception e) {
            return false;
        }
    }
}
