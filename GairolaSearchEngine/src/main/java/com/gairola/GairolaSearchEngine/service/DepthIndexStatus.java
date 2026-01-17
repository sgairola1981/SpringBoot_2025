package com.gairola.GairolaSearchEngine.service;

import com.gairola.GairolaSearchEngine.entity.WebPage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class DepthIndexStatus {
    private String status = "idle";
    private String rootUrl = "";
    private int maxDepth = 3;
    private int maxPages = 100;

    private AtomicInteger totalPages = new AtomicInteger(0);
    private AtomicInteger homepageCount = new AtomicInteger(0);
    private AtomicInteger depth1Count = new AtomicInteger(0);
    private AtomicInteger depth2Count = new AtomicInteger(0);
    private List<WebPage> indexedPages = new ArrayList<>();
    private int currentPage = 0;
    private double progress = 0;
    private String speed = "0";
    private String timeElapsed = "0s";
    private LocalDateTime startTime;

    // ✅ NO-ARG constructor (JPA/Session needs)
    public DepthIndexStatus() {}

    // ✅ 3-ARG constructor (Service needs)
    public DepthIndexStatus(String rootUrl, int maxDepth, int maxPages) {
        this.rootUrl = rootUrl;
        this.maxDepth = maxDepth;
        this.maxPages = maxPages;
        this.status = "running";
        this.startTime = LocalDateTime.now();
    }

    public void updateProgress(long startTimeMs) {
        this.progress = Math.min((double) totalPages.get() / maxPages * 100, 100);

        long elapsedMs = System.currentTimeMillis() - startTimeMs;
        long elapsedSec = elapsedMs / 1000;
        if (elapsedSec > 0) {
            double pps = totalPages.get() / (double) elapsedSec;
            this.speed = String.format("%.1f", pps);
        }
        this.timeElapsed = elapsedSec + "s";

        // Update depth stats
        this.homepageCount.set((int) indexedPages.stream().filter(p -> p.getDepth() == 0).count());
        this.depth1Count.set((int) indexedPages.stream().filter(p -> p.getDepth() == 1).count());
        this.depth2Count.set((int) indexedPages.stream().filter(p -> p.getDepth() >= 2).count());
    }
}
