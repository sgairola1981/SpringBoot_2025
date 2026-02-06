package com.gairola.GairolaSearchEngine.service;

import com.gairola.GairolaSearchEngine.entity.WebPage;
import com.gairola.GairolaSearchEngine.repository.WebPageRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepthScraperService {

    private final WebPageRepository repository;
    private final @Qualifier("crawlerExecutor") TaskExecutor executor;

    public void startDepthIndex(String rootUrl, HttpSession session) {
        executor.execute(() -> {
            System.out.println("startDepthIndex********************* -> " + rootUrl);
            DepthIndexStatus status = new DepthIndexStatus(rootUrl, 3, 100);
            session.setAttribute("depthIndex", status);
            crawlSite(rootUrl, 0, status, session);
        });
    }

    private void crawlSite(String url, int depth, DepthIndexStatus status, HttpSession session) {
        System.out.println("crawlSite[" + depth + "] ********************* " + url);

        session.setAttribute("currentUrl", url);
        session.setAttribute("currentDepth", depth);
        session.setAttribute("currentTitle", "Loading...");

        if (status.getTotalPages().get() >= status.getMaxPages() || depth > status.getMaxDepth()) {
            status.setStatus("completed");
            return;
        }

        // 🔥 THREAD-SAFE UPDATE/INSERT
        synchronized (url.intern()) {
            Optional<WebPage> existingOpt = repository.findByUrl(url);
            WebPage page;

            if (existingOpt.isPresent()) {
                // ✅ UPDATE existing (fresher content)
                System.out.println("🔄 Updating existing: " + url);
                page = existingOpt.get();
                WebPage freshData = scrape(url, depth);
                if (freshData != null) {
                    page.setTitle(freshData.getTitle());
                    page.setContent(freshData.getContent());
                    page.setDepth(Math.max(page.getDepth(), depth));  // Keep deepest
                    page.setScrapedAt(LocalDateTime.now());
                    repository.save(page);

                    // Refresh in status
                    status.getIndexedPages().removeIf(p -> p.getUrl().equals(url));
                    status.getIndexedPages().add(page);
                    session.setAttribute("currentTitle", page.getTitle());

                    log.info("🔄 Updated: {} (D{}) - Fresh content", url, page.getDepth());
                }
                updateStatus(status, session);
                return;
            }

            // ✅ NEW page
            page = scrape(url, depth);
            if (page != null) {
                repository.save(page);
                status.getIndexedPages().add(page);
                status.getTotalPages().incrementAndGet();
                session.setAttribute("currentTitle", page.getTitle());
                log.info("✅ NEW: {} (D{}) [{} total]", url, depth, status.getTotalPages().get());

                // Spawn children
                if (depth < status.getMaxDepth()) {
                    List<String> children = getInternalLinks(page, status.getRootUrl());
                    children.stream().limit(3)
                            .forEach(child -> executor.execute(() ->
                                    crawlSite(child, depth + 1, status, session)));
                }
            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
    }

    private void updateStatus(DepthIndexStatus status, HttpSession session) {
        long startTimeMs = status.getStartTime().toInstant(ZoneOffset.UTC).toEpochMilli();
        status.updateProgress(startTimeMs);
        session.setAttribute("depthIndex", status);
    }

    private WebPage scrape(String url, int depth) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (GairolaSearchBot/1.0)")
                    .timeout(10000)
                    .get();

            WebPage page = new WebPage();
            page.setUrl(url);
            page.setTitle(truncate(doc.title(), 200));
            page.setContent(truncate(doc.body().text(), 5000));
            page.setDepth(depth);
            page.setScrapedAt(LocalDateTime.now());
            return page;

        } catch (Exception e) {
            log.error("Scrape failed: {}", url, e);
            return null;
        }
    }

    private List<String> getInternalLinks(WebPage page, String rootDomain) {
        try {
            Document doc = Jsoup.connect(page.getUrl()).get();
            Elements links = doc.select("a[href]");
            String baseDomain = rootDomain.replaceAll("https?://([^/]+).*", "$1");

            return links.stream()
                    .map(link -> link.attr("abs:href"))
                    .filter(this::isValidLink)
                    .filter(link -> link.contains(baseDomain))
                    .distinct()
                    .limit(10)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.warn("Link extraction failed: {}", page.getUrl(), e);
            return new ArrayList<>();
        }
    }

    private boolean isValidLink(String link) {
        return link.startsWith("http") && link.length() < 2000 && !link.contains("#");
    }

    private String truncate(String text, int max) {
        return text != null && text.length() > max ? text.substring(0, max) + "..." : text;
    }
}
