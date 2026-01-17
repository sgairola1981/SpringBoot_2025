package com.gairola.GairolaSearchEngine.service;

import com.gairola.GairolaSearchEngine.entity.WebPage;
import com.gairola.GairolaSearchEngine.repository.WebPageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepthScraperService {

    private final WebPageRepository repository;

    @Async("taskExecutor")
    public void startDepthIndex(String rootUrl, HttpSession session) {
        DepthIndexStatus status = new DepthIndexStatus(rootUrl, 3, 100);
        session.setAttribute("depthIndex", status);

        if (!isValidUrl(rootUrl)) {
            status.setStatus("error");
            status.setRootUrl(rootUrl + " ❌ Invalid URL");
            return;
        }

        Set<String> visited = Collections.synchronizedSet(new HashSet<>());
        Queue<CrawlTask> queue = new LinkedList<>();

        // Start from root
        queue.add(new CrawlTask(rootUrl, 0));
        long startTime = System.currentTimeMillis();

        while (!queue.isEmpty() && status.getTotalPages().get() < status.getMaxPages()) {
            CrawlTask task = queue.poll();
            if (visited.contains(task.url) || task.depth > status.getMaxDepth()) continue;

            visited.add(task.url);

            try {
                WebPage page = scrape(task.url, task.depth);
                if (page != null) {
                    repository.save(page);
                    status.getIndexedPages().add(page);
                    status.getTotalPages().incrementAndGet();
                }

                // Add child links (same domain only)
                if (task.depth < status.getMaxDepth()) {
                    List<String> children = getInternalLinks(page, rootUrl);
                    children.stream().limit(3).forEach(child -> queue.add(new CrawlTask(child, task.depth + 1)));
                }

                status.updateProgress(startTime);
                session.setAttribute("depthIndex", status);
                Thread.sleep(800); // Polite crawler

            } catch (Exception e) {
                log.warn("Failed to crawl {}: {}", task.url, e.getMessage());
            }
        }

        status.setStatus("completed");
        log.info("✅ Depth index complete: {} pages from {}", status.getTotalPages(), rootUrl);
    }

    private WebPage scrape(String url, int depth) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (GairolaSearchBot/1.0)")
                    .timeout(10_000)
                    .get();

            WebPage page = new WebPage();
            page.setUrl(url);
            page.setTitle(doc.title().length() > 0 ? doc.title().substring(0, 200) : "No Title");
            page.setContent(doc.body().text().length() > 5000 ?
                    doc.body().text().substring(0, 5000) : doc.body().text());
            page.setDepth(depth);
            page.setScrapedAt(LocalDateTime.now());

            return page;

        } catch (Exception e) {
            log.error("Scrape failed: {}", url, e);
            return null;
        }
    }

    private List<String> getInternalLinks(WebPage page, String rootDomain) {
        List<String> links = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(page.getUrl()).get();
            Elements hrefs = doc.select("a[href]");

            String baseDomain = rootDomain.replaceAll("https?://([^/]+).*", "$1");

            for (Element link : hrefs) {
                String absUrl = link.attr("abs:href");
                if (isSameDomain(absUrl, baseDomain) && absUrl.length() < 2000) {
                    links.add(absUrl);
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return links.stream().distinct().limit(10).collect(Collectors.toList());
    }

    private boolean isSameDomain(String url, String baseDomain) {
        return Pattern.compile(baseDomain).matcher(url).find();
    }

    private boolean isValidUrl(String url) {
        return url != null && url.matches("https?://.+");
    }

    record CrawlTask(String url, int depth) {}
}
