package com.gairola.searchEngine.service;


import com.gairola.searchEngine.entity.WebPage;
import com.gairola.searchEngine.repository.WebPageRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class WebScraperService {

    private final WebPageRepository repo;

    public WebScraperService(WebPageRepository repo) {
        this.repo = repo;
    }

    public WebPage scrape(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(15000)
                    .get();

            WebPage page = new WebPage();
            page.setUrl(url);
            page.setTitle(doc.title());

            // ✅ FIXED: Safe substring
            String fullContent = doc.body().text();
            page.setContent(fullContent.length() > 5000
                    ? fullContent.substring(0, 5000)
                    : fullContent);

            System.out.println("📄 Title: " + page.getTitle().substring(0, Math.min(100, page.getTitle().length())));
            System.out.println("📏 Content length: " + page.getContent().length());

            return repo.save(page);

        } catch (Exception e) {
            System.err.println("❌ Scrape failed for " + url + ": " + e.getMessage());
            throw new RuntimeException("Failed to scrape: " + url, e);
        }
    }
}
