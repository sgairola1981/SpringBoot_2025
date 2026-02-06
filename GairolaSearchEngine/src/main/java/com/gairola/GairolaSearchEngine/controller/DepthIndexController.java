package com.gairola.GairolaSearchEngine.controller;

import com.gairola.GairolaSearchEngine.service.DepthIndexStatus;
import com.gairola.GairolaSearchEngine.service.DepthScraperService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class DepthIndexController {

    private final DepthScraperService scraperService;

    @GetMapping("/depth-index")
    public String depthIndex(@RequestParam(required = false) String url,
                             Model model,
                             HttpSession session) {

        DepthIndexStatus status = getStatus(session);

        // Safe Java 8 null handling
        model.addAttribute("status", status.getStatus());
        model.addAttribute("rootUrl", status.getRootUrl());
        model.addAttribute("maxDepth", status.getMaxDepth());
        model.addAttribute("maxPages", status.getMaxPages());
        model.addAttribute("totalPages", status.getTotalPages().get());
        model.addAttribute("progress", String.format("%.1f", status.getProgress()));
        model.addAttribute("currentPage", status.getCurrentPage());
        model.addAttribute("speed", String.format("%.2f", status.getSpeed()));
        model.addAttribute("timeElapsed", status.getTimeElapsed());
        model.addAttribute("homepageCount", status.getHomepageCount());
        model.addAttribute("depth1Count", status.getDepth1Count());
        model.addAttribute("depth2Count", status.getDepth2Count());
        model.addAttribute("uniqueUrls", status.getUniqueUrls());
        model.addAttribute("avgContentLength", status.getAvgContentLength());
        model.addAttribute("indexedPages", status.getIndexedPages());

        // ✅ JAVA 8 SAFE session attributes
        model.addAttribute("currentUrl", safeString(session.getAttribute("currentUrl")));
        model.addAttribute("currentTitle", safeString(session.getAttribute("currentTitle")));
        model.addAttribute("currentDepth", safeInt(session.getAttribute("currentDepth"), 0));

        return "depth-index";
    }

    @PostMapping("/depth-index/start")
    public String startIndexing(@RequestParam String url, HttpSession session) {
        System.out.println("/depth-index/start********************* -> " + url);
        scraperService.startDepthIndex(url, session);
        return "redirect:/depth-index?url=" + url;
    }

    @GetMapping("/depth-index/stop")
    public String stopIndexing(HttpSession session) {
        DepthIndexStatus status = getStatus(session);
        status.setStatus("stopped");
        session.setAttribute("depthIndex", status);
        return "redirect:/depth-index";
    }

    private DepthIndexStatus getStatus(HttpSession session) {
        DepthIndexStatus status = (DepthIndexStatus) session.getAttribute("depthIndex");
        return status != null ? status : new DepthIndexStatus();
    }

    // ✅ Java 8 helpers
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
}
