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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "web_pages_seq")
    @SequenceGenerator(name = "web_pages_seq", sequenceName = "web_pages_seq", allocationSize = 1)
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

    // ✅ NEW: Session tracking (36 chars)
    @Column(name = "session_id", length = 64)
    private String sessionId;

    // Transient - computed during scraping, not persisted
    @Transient
    private List<String> childLinks = new ArrayList<>();

    // Default constructor for JPA
    public WebPage(String url) {
        this.url = url;
    }

    // Convenience constructors (UPDATED)
    public WebPage(String url, String title, String content, int depth) {
        this(url, title, content, depth, null);
    }

    public WebPage(String url, String title, String content, int depth, String sessionId) {
        this.url = url;
        this.title = title;
        this.content = content;
        this.depth = depth;
        this.scrapedAt = LocalDateTime.now();
        this.sessionId = sessionId;  // ✅ Set sessionId
    }

    // Business methods (unchanged)
    public boolean isHomepage() { return depth == 0; }
    public String getDomain() { return url.replaceAll("https?://([^/]+).*", "$1"); }
    public String getSnippet(int maxLength) {
        if (content == null || content.isEmpty()) return "";
        return content.length() > maxLength ? content.substring(0, maxLength).trim() + "..." : content.trim();
    }

    @Override
    public String toString() {
        return String.format("WebPage{id=%d, title='%s', url='%s', depth=%d, sessionId='%s'}",
                id, title, url, depth, sessionId);
    }
}
