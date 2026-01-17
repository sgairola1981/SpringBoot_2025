package com.gairola.GairolaSearchEngine.service;

import com.gairola.GairolaSearchEngine.entity.WebPage;
import com.gairola.GairolaSearchEngine.repository.WebPageRepository;
import lombok.Data;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class WebScraperService {

    private final WebPageRepository repo;
    private final Set<String> visited = ConcurrentHashMap.newKeySet();
    private final Queue<CrawlTask> queue = new ConcurrentLinkedQueue<>();
    private String baseDomain;

    private static final int MAX_DEPTH = 2;
    private static final int MAX_PAGES = 500;
    private static final long CRAWL_DELAY = 2000;  // 2 seconds
    private static final int CONTENT_LIMIT = 2000;  // Fast search

    public WebScraperService(WebPageRepository repo) {
        this.repo = repo;
    }

    /** Single page scrape - MAIN ENDPOINT */
    @Transactional
    public WebPage scrape(String url) {
        // Cache check first (fixes N+1)
        Optional<WebPage> existing = checkExists(url);
        if (existing.isPresent()) {
            System.out.printf("✅ Already indexed: %s%n", url);
            return existing.get();
        }

        if (visited.contains(url)) {
            return repo.findByUrl(url).orElse(null);
        }

        // Anti-bot scrape with retry
        WebPage page = retryScrape(url, 3);
        if (page != null && !page.getTitle().startsWith("Error")) {
            visited.add(url);
            System.out.printf("✅ Scraped: %s (Title: %s)%n", url, page.getTitle());
            return page;
        }

        return page;
    }

    /** Full website crawl */
    public void crawlWebsite(String startUrl) {
        try {
            baseDomain = new URL(startUrl).getHost();
            visited.clear();
            queue.clear();

            System.out.println("🔍 Starting crawl: " + startUrl);

            // Try sitemap first
            if (!loadSitemap(startUrl)) {
                queue.add(new CrawlTask(startUrl, 0));
            }

            crawlBatch();

        } catch (Exception e) {
            throw new RuntimeException("Crawl failed for " + startUrl, e);
        }
    }

    /** Cacheable exists check - ELIMINATES N+1 PROBLEM */
    @Cacheable(value = "urls", key = "#url")
    public Optional<WebPage> checkExists(String url) {
        return repo.findByUrl(url);
    }

    /** Anti-bot retry logic */
    private WebPage retryScrape(String url, int maxRetries) {
        for (int retry = 0; retry < maxRetries; retry++) {
            try {
                TimeUnit.MILLISECONDS.sleep(CRAWL_DELAY + (retry * 1000));
                WebPage page = scrapePage(url);
                if (page.getTitle() != null && !page.getTitle().startsWith("Error")) {
                    repo.save(page);
                    return page;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            } catch (Exception e) {
                System.err.printf("❌ Retry %d/%d failed for %s: %s%n", retry + 1, maxRetries, url, e.getMessage());
            }
        }
        return createErrorPage(url, "All retries failed");
    }

    private boolean loadSitemap(String baseUrl) {
        try {
            String[] sitemaps = {
                    baseUrl.replaceAll("/$", "") + "/sitemap.xml",
                    baseUrl.replaceAll("/$", "") + "/sitemap_index.xml"
            };

            for (String sitemapUrl : sitemaps) {
                try {
                    Document sitemap = Jsoup.connect(sitemapUrl)
                            .userAgent(getUserAgent())
                            .referrer("https://www.google.com/")
                            .timeout(10000)
                            .get();

                    Elements locs = sitemap.select("loc");
                    long added = locs.stream()
                            .map(el -> el.text().trim())
                            .filter(this::isSameDomain)
                            .filter(url -> queue.size() < MAX_PAGES)
                            .peek(url -> queue.add(new CrawlTask(url, 0)))
                            .count();

                    if (added > 0) {
                        System.out.println("📋 Loaded " + added + " URLs from sitemap");
                        return true;
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            System.err.println("Sitemap load failed: " + e.getMessage());
        }
        return false;
    }

    @Async("crawlerExecutor")
    public void crawlBatch() {
        int count = 0;
        System.out.println("🐛 Starting async crawl batch...");

        while (!queue.isEmpty() && count < MAX_PAGES) {
            CrawlTask task = queue.poll();
            if (task == null || task.depth > MAX_DEPTH || !visited.add(task.url)) {
                continue;
            }

            try {
                TimeUnit.MILLISECONDS.sleep(CRAWL_DELAY);
                WebPage page = scrapePage(task.url);
                page.setDepth(task.depth);
                repo.save(page);
                enqueueLinks(task.url, task.depth);
                count++;
                System.out.printf("📄 Crawled %d/%d: %s%n", count, MAX_PAGES, task.url);
            } catch (Exception e) {
                System.err.println("Failed: " + task.url + " - " + e.getMessage());
            }
        }
        System.out.println("✅ Crawl complete: " + count + " pages indexed");
    }

    /** Production-ready scraper with full anti-bot headers */
    private WebPage scrapePage(String url) {
        try {
            Connection conn = Jsoup.connect(url)
                    .userAgent(getUserAgent())
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("DNT", "1")
                    .header("Connection", "keep-alive")
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("Sec-Fetch-Dest", "document")
                    .header("Sec-Fetch-Mode", "navigate")
                    .header("Sec-Fetch-Site", "none")
                    .referrer("https://www.google.com/")
                    .timeout(20000)
                    .followRedirects(true)
                    .maxBodySize(0);

            Document doc = conn.get();

            WebPage page = new WebPage();
            page.setUrl(url);
            page.setTitle(doc.title().trim().isEmpty() ? "No Title" : doc.title().trim());

            // Fast search content limit (Google-style)
            String fullContent = doc.body().text().trim();
            page.setContent(fullContent.length() > CONTENT_LIMIT ?
                    fullContent.substring(0, CONTENT_LIMIT) : fullContent);
            page.setScrapedAt(LocalDateTime.now());
            page.setDepth(0);

            return page;

        } catch (Exception e) {
            return createErrorPage(url, e.getMessage());
        }
    }

    private WebPage createErrorPage(String url, String error) {
        WebPage page = new WebPage();
        page.setUrl(url);
        page.setTitle("Error: " + error);
        page.setContent("Failed to scrape - " + error);
        page.setScrapedAt(LocalDateTime.now());
        page.setDepth(0);
        return page;
    }

    private void enqueueLinks(String url, int depth) {
        if (depth >= MAX_DEPTH || queue.size() >= MAX_PAGES) return;

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(getUserAgent())
                    .referrer("https://www.google.com/")
                    .timeout(10000)
                    .get();

            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String absUrl = link.attr("abs:href");
                if (isSameDomain(absUrl) && !visited.contains(absUrl) && queue.size() < MAX_PAGES) {
                    queue.add(new CrawlTask(absUrl, depth + 1));
                }
            }
        } catch (Exception ignored) {}
    }

    private boolean isSameDomain(String urlStr) {
        if (!urlStr.startsWith("http")) return false;
        try {
            return new URL(urlStr).getHost().equalsIgnoreCase(baseDomain);
        } catch (Exception e) {
            return false;
        }
    }

    // Stats
    public long getPageCount() {
        return repo.count();
    }

    /** Rotating User-Agent - defeats 403 bot detection */
    private String getUserAgent() {
        String[] agents = {
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/121.0",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
        };
        return agents[(int)(System.currentTimeMillis() / 1000 % agents.length)];
    }

    /** Inner class for crawl tasks */
    @Data
    static class CrawlTask {
        final String url;
        final int depth;

        CrawlTask(String url, int depth) {
            this.url = url;
            this.depth = depth;
        }
    }
}
