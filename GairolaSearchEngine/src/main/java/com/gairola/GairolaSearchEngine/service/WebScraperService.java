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
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

@Service
public class WebScraperService {

    private final WebPageRepository repo;

    private final Set<String> visited = ConcurrentHashMap.newKeySet();
    private final Queue<CrawlTask> queue = new ConcurrentLinkedQueue<>();

    private String baseDomain;

    private static final int MAX_DEPTH = 3;
    private static final int MAX_PAGES = 500;
    private static final long CRAWL_DELAY = 2000;
    private static final int CONTENT_LIMIT = 2000;

    public WebScraperService(WebPageRepository repo) {
        this.repo = repo;
    }

    // ================================
    // MAIN SINGLE PAGE SCRAPE
    // ================================
    @Transactional
    public WebPage scrape(String url) {
        url = normalizeUrl(url);

        Optional<WebPage> existing = checkExists(url);
        if (existing.isPresent()) {
            System.out.println("✅ Already indexed: " + url);
            return existing.get();
        }

        WebPage page = retryScrape(url, 3);
        if (page != null && !page.getTitle().startsWith("Error")) {
            visited.add(url);
            return page;
        }

        return page;
    }

    // ================================
    // FULL WEBSITE CRAWL
    // ================================
    public void crawlWebsite(String startUrl) {
        try {
            baseDomain = new URL(startUrl).getHost();
            visited.clear();
            queue.clear();

            startUrl = normalizeUrl(startUrl);

            if (!loadSitemap(startUrl)) {
                queue.add(new CrawlTask(startUrl, 0));
            }

            crawlBatch();

        } catch (Exception e) {
            throw new RuntimeException("Crawl failed: " + startUrl, e);
        }
    }

    // ================================
    // CACHE CHECK
    // ================================
    @Cacheable(value = "urls", key = "#url")
    public Optional<WebPage> checkExists(String url) {
        return repo.findByUrl(url);
    }

    // ================================
    // RETRY SCRAPER
    // ================================
    private WebPage retryScrape(String url, int maxRetries) {
        for (int retry = 0; retry < maxRetries; retry++) {
            try {
                TimeUnit.MILLISECONDS.sleep(CRAWL_DELAY + (retry * 1000));

                Document doc = fetchDocument(url);
                WebPage page = extractPageData(url, doc, 0);

                repo.save(page);
                return page;

            } catch (Exception e) {
                System.err.println("Retry " + (retry + 1) + " failed: " + url);
            }
        }
        return createErrorPage(url, "All retries failed");
    }

    // ================================
    // ASYNC CRAWL
    // ================================
    @Async("crawlerExecutor")
    public void crawlBatch() {

        int count = 0;

        while (!queue.isEmpty() && count < MAX_PAGES) {

            CrawlTask task = queue.poll();
            if (task == null || task.depth > MAX_DEPTH) continue;

            String normalized = normalizeUrl(task.url);
            if (!visited.add(normalized)) continue;

            try {
                TimeUnit.MILLISECONDS.sleep(CRAWL_DELAY);

                Document doc = fetchDocument(normalized);
                WebPage page = extractPageData(normalized, doc, task.depth);

                repo.save(page);
                enqueueLinks(doc, task.depth);

                count++;
                System.out.println("📄 Crawled: " + normalized);

            } catch (Exception e) {
                System.err.println("Failed: " + normalized);
            }
        }

        System.out.println("✅ Crawl complete. Indexed: " + count);
    }

    // ================================
    // FETCH DOCUMENT
    // ================================
    private Document fetchDocument(String url) throws Exception {

        Connection conn = Jsoup.connect(url)
                .userAgent(getUserAgent())
                .header("Accept", "text/html")
                .referrer("https://www.google.com/")
                .timeout(20000)
                .followRedirects(true);

        return conn.get();
    }

    // ================================
    // CLEAN CONTENT EXTRACTION
    // ================================
    private WebPage extractPageData(String url, Document doc, int depth) {

        doc.select("nav, header, footer, script, style, noscript, svg, iframe").remove();
        doc.select(".navbar, .menu, .sidebar, .advertisement, .ads, .cookie, .banner").remove();

        Element main = doc.selectFirst("main, article, .content, .post, .article, .entry-content");

        String text = (main != null ? main.text() : doc.body().text());
        text = text.replaceAll("\\s+", " ").trim();

        if (text.length() < 50) {
            text = "Content too short or not meaningful.";
        }
        if (text.length() > CONTENT_LIMIT) {
            text = text.substring(0, CONTENT_LIMIT);
        }

        WebPage page = new WebPage();
        page.setUrl(url);
        page.setTitle(doc.title().isEmpty() ? "No Title" : doc.title().trim());
        page.setContent(text);
        page.setDepth(depth);
        page.setScrapedAt(LocalDateTime.now());

        return page;
    }

    // ================================
    // ENQUEUE LINKS (NO RE-DOWNLOAD)
    // ================================
    private void enqueueLinks(Document doc, int depth) {

        if (depth >= MAX_DEPTH || queue.size() >= MAX_PAGES) return;

        Elements links = doc.select("a[href]");

        for (Element link : links) {
            String absUrl = normalizeUrl(link.attr("abs:href"));

            if (isSameDomain(absUrl)
                    && !visited.contains(absUrl)
                    && isValidHtmlUrl(absUrl)) {
                queue.add(new CrawlTask(absUrl, depth + 1));
            }
        }
    }

    private boolean isValidHtmlUrl(String url) {
        return !url.matches("(?i).+\\.(pdf|jpg|jpeg|png|gif|svg|zip|rar|mp4|mp3|doc|docx|xls|xlsx)$");
    }


    // ================================
    // SITEMAP LOADER
    // ================================
    private boolean loadSitemap(String baseUrl) {
        try {
            String sitemapUrl = baseUrl.replaceAll("/$", "") + "/sitemap.xml";

            Document sitemap = fetchDocument(sitemapUrl);
            Elements locs = sitemap.select("loc");

            for (Element el : locs) {
                String url = normalizeUrl(el.text().trim());
                if (isSameDomain(url)) {
                    queue.add(new CrawlTask(url, 0));
                }
            }

            return !locs.isEmpty();

        } catch (Exception e) {
            return false;
        }
    }

    // ================================
    // URL NORMALIZATION
    // ================================
    private String normalizeUrl(String url) {
        try {
            URL u = new URL(url);
            String path = u.getPath().isEmpty() ? "/" : u.getPath();

            String clean = u.getProtocol() + "://" + u.getHost() + path;

            return clean.endsWith("/") && clean.length() > 1
                    ? clean.substring(0, clean.length() - 1)
                    : clean;

        } catch (Exception e) {
            return url;
        }
    }


    private boolean isSameDomain(String urlStr) {
        try {
            return new URL(urlStr).getHost().equalsIgnoreCase(baseDomain);
        } catch (Exception e) {
            return false;
        }
    }

    public long getPageCount() {
        return repo.count();
    }

    private WebPage createErrorPage(String url, String error) {
        WebPage page = new WebPage();
        page.setUrl(url);
        page.setTitle("Error");
        page.setContent(error);
        page.setDepth(0);
        page.setScrapedAt(LocalDateTime.now());
        return page;
    }

    private String getUserAgent() {
        String[] agents = {
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64)",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)",
                "Mozilla/5.0 (X11; Linux x86_64)"
        };
        return agents[(int) (System.currentTimeMillis() / 1000 % agents.length)];
    }


    @Data
    static class CrawlTask {
        final String url;
        final int depth;
    }
}
