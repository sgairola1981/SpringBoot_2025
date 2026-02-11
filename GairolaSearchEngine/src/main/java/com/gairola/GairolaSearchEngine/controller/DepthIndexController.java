package com.gairola.GairolaSearchEngine.controller;

import com.gairola.GairolaSearchEngine.service.DepthIndexStatus;
import com.gairola.GairolaSearchEngine.service.DepthScraperService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
@Controller
@RequiredArgsConstructor
public class DepthIndexController {

    private final DepthScraperService scraperService;

    /**
     * Main view for depth indexing dashboard.
     */
    @GetMapping("/depth-index")
    public String view(@RequestParam(required = false) String url,
                       HttpSession session, Model model) {

        DepthIndexStatus status = (DepthIndexStatus) session.getAttribute("depthIndex");

        if (status == null) {
            status = new DepthIndexStatus(url, 3, 100);
            status.setStatus("idle");
            session.setAttribute("depthIndex", status);
        } else if (url != null && status.getRootUrl() == null) {
            status.setRootUrl(url);
        }

        // ✅ ALL 17 model attributes - perfect!
        model.addAttribute("status", status.getStatus());
        model.addAttribute("progress", status.getProgress());
        model.addAttribute("totalPages", status.getTotalPages().get());
        model.addAttribute("currentPage", status.getCurrentPage());
        model.addAttribute("rootUrl", status.getRootUrl());
        model.addAttribute("timeElapsed", status.getTimeElapsed());
        model.addAttribute("speed", status.getSpeed());
        model.addAttribute("indexedPages", status.getIndexedPages());

        model.addAttribute("maxDepth", status.getMaxDepth());
        model.addAttribute("maxPages", status.getMaxPages());
        model.addAttribute("homepageCount", status.getHomepageCount());
        model.addAttribute("depth1Count", status.getDepth1Count());
        model.addAttribute("depth2Count", status.getDepth2Count());
        model.addAttribute("uniqueUrls", status.getUniqueUrls());
        model.addAttribute("avgContentLength", status.getAvgContentLength());

        return "depth-index";
    }

    /**
     * Start deep indexing from given URL.
     * This matches: <form th:action="@{/depth-index/start}" method="get">
     */
    @GetMapping("/depth-index/start")
    public String startIndexing(@RequestParam String url, HttpSession session) {
        System.out.println("/depth-index/start -> " + url);
        scraperService.startDepthIndex(url, session);
        // Carry url back so header/input shows it
        return "redirect:/depth-index?url=" + url;
    }

    /**
     * Stop indexing (mark as completed).
     */
    @GetMapping("/depth-index/stop")
    public String stopIndexing(HttpSession session) {
        DepthIndexStatus status = getStatus(session);
        status.setStatus("completed");   // not "stopped", UI uses 'completed'
        session.setAttribute("depthIndex", status);
        return "redirect:/depth-index";
    }

    /**
     * Simple keyword search over indexed data.
     */
    @GetMapping("/search_data")
    public String search(@RequestParam String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        model.addAttribute("results", scraperService.search(keyword));
        return "search";
    }

    // Helpers

    private DepthIndexStatus getStatus(HttpSession session) {
        DepthIndexStatus status = (DepthIndexStatus) session.getAttribute("depthIndex");
        if (status == null) {
            status = new DepthIndexStatus(null, 3, 100);
            status.setStatus("idle");
            session.setAttribute("depthIndex", status);
        }
        return status;
    }

    private String safeString(Object obj) {
        return obj != null ? obj.toString() : "";
    }

    private int safeInt(Object obj, int defaultValue) {
        if (obj == null) return defaultValue;
        try {
            return Integer.parseInt(obj.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @GetMapping("/depth-status")
    public String getStatus(Model model) {
       // model.addAttribute("status", scraperService.getStatus());
        return "depth-index";
    }
}
