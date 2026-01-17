package com.gairola.GairolaSearchEngine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "web_pages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", length = 2000, nullable = false)
    private String url;

    @Column(name = "title", length = 500)
    private String title;

    @Lob
    @Column(name = "content", columnDefinition = "CLOB")
    private String content;

    @Column(name = "depth", nullable = false)
    private int depth = 0;

    @Column(name = "scraped_at", nullable = false)
    private LocalDateTime scrapedAt = LocalDateTime.now();

    // Transient - computed during scraping, not persisted
    @Transient
    private List<String> childLinks = new ArrayList<>();

    // Default constructor for JPA
    public WebPage(String url) {
        this.url = url;
    }

    // Convenience constructors
    public WebPage(String url, String title, String content, int depth) {
        this.url = url;
        this.title = title;
        this.content = content;
        this.depth = depth;
        this.scrapedAt = LocalDateTime.now();
    }

    // Business methods
    public boolean isHomepage() {
        return depth == 0;
    }

    public String getDomain() {
        return url.replaceAll("https?://([^/]+).*", "$1");
    }

    public String getSnippet(int maxLength) {
        if (content == null || content.isEmpty()) return "";
        return content.length() > maxLength
                ? content.substring(0, maxLength).trim() + "..."
                : content.trim();
    }

    @Override
    public String toString() {
        return String.format("WebPage{id=%d, title='%s', url='%s', depth=%d}",
                id, title, url, depth);
    }
}
