package com.gairola.GairolaSearchEngine.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class IndexingStatus {

    private String status = "idle"; // idle | running | completed
    private int totalPages;
    private int currentPage;
    private int progress;
    private long startTime;

    private String rootUrl;

    public String getTimeElapsed() {
        if (startTime == 0) return "0s";
        long seconds = (System.currentTimeMillis() - startTime) / 1000;
        return seconds + "s";
    }
}

