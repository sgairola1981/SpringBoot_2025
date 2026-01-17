package com.gairola.GairolaSearchEngine.controller;

import com.gairola.GairolaSearchEngine.entity.WebPage;
import com.gairola.GairolaSearchEngine.service.DepthIndexStatus;
import com.gairola.GairolaSearchEngine.service.DepthScraperService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class DepthIndexController {

    private final DepthScraperService scraperService;

    @GetMapping("/depth-index")
    public String depthIndex(@RequestParam(required = false) String url,
                             Model model,
                             HttpSession session) {

        DepthIndexStatus status = getStatus(session);
        model.addAttribute("status", status.getStatus());
        model.addAttribute("rootUrl", status.getRootUrl());
        model.addAttribute("maxDepth", status.getMaxDepth());
        model.addAttribute("maxPages", status.getMaxPages());

        // Populate stats for idle/completed
        if ("idle".equals(status.getStatus()) || "completed".equals(status.getStatus())) {
            model.addAttribute("totalPages", status.getTotalPages().get());
            model.addAttribute("homepageCount", status.getHomepageCount().get());
            model.addAttribute("depth1Count", status.getDepth1Count().get());
            model.addAttribute("depth2Count", status.getDepth2Count().get());
            model.addAttribute("uniqueUrls", status.getIndexedPages().stream()
                    .map(WebPage::getUrl).distinct().count());
            model.addAttribute("avgContentLength", (int) status.getIndexedPages().stream()
                    .mapToInt(p -> p.getContent() != null ? p.getContent().length() : 0)
                    .average().orElse(0));
            model.addAttribute("indexedPages", status.getIndexedPages().stream()
                    .sorted(Comparator.comparing(WebPage::getDepth).thenComparing(WebPage::getTitle))
                    .limit(50).collect(Collectors.toList()));
        }

        model.addAttribute("progress", String.format("%.1f", status.getProgress()));
        model.addAttribute("currentPage", status.getCurrentPage());
        model.addAttribute("speed", status.getSpeed());
        model.addAttribute("timeElapsed", status.getTimeElapsed());

        return "depth-index";
    }

    @PostMapping("/depth-index/start")
    public String startIndexing(@RequestParam String url,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        // Clear previous
        session.removeAttribute("depthIndex");
        // Start async
        scraperService.startDepthIndex(url, session);
        return "redirect:/depth-index";
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
}
