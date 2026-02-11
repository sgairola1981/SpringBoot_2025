package com.gairola.GairolaSearchEngine.service;

import com.gairola.GairolaSearchEngine.entity.WebPage;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class DepthIndexStatus {
    private String rootUrl;
    private int maxDepth;
    private int maxPages;
    private String status = "idle";
    private LocalDateTime startTime = LocalDateTime.now();
    private AtomicInteger totalPages = new AtomicInteger(0);
    private double progress = 0.0;
    private double speed = 0.0;
    private String timeElapsed = "0s";
    private int currentPage = 0;
    private List<WebPage> indexedPages =  new java.util.concurrent.CopyOnWriteArrayList<>();

    public DepthIndexStatus() {}

    public DepthIndexStatus(String rootUrl, int maxDepth, int maxPages) {
        this.rootUrl = rootUrl;
        this.maxDepth = maxDepth;
        this.maxPages = maxPages;
        this.status = "running";
    }

    public void updateProgress(long startTimeMs) {
        long nowMs = System.currentTimeMillis();
        speed = totalPages.get() / ((nowMs - startTimeMs) / 1000.0);
        progress = Math.min(100.0, (totalPages.get() * 100.0 / maxPages));
        long elapsedSec = (nowMs - startTimeMs) / 1000;
        timeElapsed = elapsedSec + "s";
        currentPage = totalPages.get();
    }

    public int getHomepageCount() {
        return (int) new ArrayList<>(indexedPages)
                .stream()
                .filter(p -> p.getDepth() == 0)
                .count();
    }

    public int getDepth1Count() {
        return (int) indexedPages.stream().filter(p -> p.getDepth() == 1).count();
    }

    public int getDepth2Count() {
        return (int) indexedPages.stream().filter(p -> p.getDepth() == 2).count();
    }

    public long getUniqueUrls() {
        return indexedPages.stream().map(WebPage::getUrl).distinct().count();
    }

    public int getAvgContentLength() {
        return indexedPages.isEmpty() ? 0 :
                (int) indexedPages.stream().mapToInt(p -> p.getContent().length()).average().orElse(0);
    }
}
