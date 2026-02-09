package com.gairola.GairolaSearchEngine.service;

import com.gairola.GairolaSearchEngine.entity.WebPage;
import com.gairola.GairolaSearchEngine.repository.WebPageRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class DepthScraperService {

    private final WebPageRepository repository;
    private final @Qualifier("crawlerExecutor") TaskExecutor executor;

    /** Prevent duplicate crawling */
    private final Set<String> visited = ConcurrentHashMap.newKeySet();

    /** 🔍 In-memory search index (keyword -> URLs) */
    private final Map<String, Set<String>> searchIndex = new ConcurrentHashMap<>();

    public void startDepthIndex(String rootUrl, HttpSession session) {

        visited.clear();
        searchIndex.clear();

        DepthIndexStatus status = new DepthIndexStatus(rootUrl, 3, 100);
        status.setStatus("running");
        session.setAttribute("depthIndex", status);

        // ✅ Run root crawl with completion safety net
        executor.execute(() -> {
            try {
                crawl(rootUrl, 0, status, session);
            } finally {
                // ✅ Safety net: always complete if not stopped
                if (!"completed".equals(status.getStatus()) &&
                        !"stopped".equals(status.getStatus())) {
                    status.setStatus("completed");
                    session.setAttribute("depthIndex", status);
                    log.info("✅ Depth indexing completed: {}", rootUrl);
                }
            }
        });
    }

    private void crawl(String url, int depth,
                       DepthIndexStatus status,
                       HttpSession session) {

        // ✅ STOP CONDITIONS FIRST
        if ("stopped".equals(status.getStatus()) ||
                "completed".equals(status.getStatus())) {
            return;
        }

        if (depth > status.getMaxDepth()) return;

        // ✅ COMPLETE WHEN MAX REACHED
        if (status.getTotalPages().get() >= status.getMaxPages()) {
            status.setStatus("completed");
            session.setAttribute("depthIndex", status);
            log.info("✅ Max pages reached: {}", status.getMaxPages());
            return;
        }

        if (!visited.add(url)) return;

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (GairolaSearchBot/1.0)")
                    .timeout(10000)
                    .get();

            WebPage page = saveOrUpdate(url, depth, doc);

            status.getIndexedPages().add(page);
            status.getTotalPages().incrementAndGet();

            indexPage(page);           // 🔍 build search index
            updateProgress(status, session);

            // ✅ Only recurse if not completed
            if (depth < status.getMaxDepth() && !"completed".equals(status.getStatus())) {
                for (String link : extractInternalLinks(doc, status.getRootUrl())) {
                    executor.execute(() ->
                            crawl(link, depth + 1, status, session));
                }
            }

        } catch (Exception e) {
            log.warn("❌ Failed to crawl: {}", url, e);
        }
    }

    // -------------------------------------------------
    // 🔍 SEARCH INDEX
    // -------------------------------------------------

    public Set<String> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return Set.of();
        return searchIndex.getOrDefault(keyword.toLowerCase(), Set.of());
    }

    private void indexPage(WebPage page) {
        String text = (page.getTitle() + " " + page.getContent()).toLowerCase();
        String[] tokens = text.split("\\W+");

        for (String token : tokens) {
            if (token.length() < 3) continue;
            searchIndex.computeIfAbsent(token, k -> ConcurrentHashMap.newKeySet())
                    .add(page.getUrl());
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
        log.info("✅ Indexed [D{}] {}", depth, url);

        return page;
    }

    private List<String> extractInternalLinks(Document doc, String rootUrl) {
        String domain = rootUrl.replaceAll("https?://([^/]+).*", "$1");
        Elements links = doc.select("a[href]");

        return links.stream()
                .map(link -> link.attr("abs:href"))
                .filter(this::isValid)
                .filter(abs -> abs.contains(domain))
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
}
