package com.gairola.localprompt.controller;

import com.gairola.localprompt.dto.ChatForm;
import com.gairola.localprompt.entity.ChatMessage;
import com.gairola.localprompt.entity.UploadedContent;
import com.gairola.localprompt.service.ChatService;
import com.gairola.localprompt.service.FileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ModelAttribute; // Add this
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final FileService fileService;
    private final ChatService chatService;

    // ===== CHAT PAGE
    @GetMapping("/")
    public ModelAndView home(HttpSession session) {
        ModelAndView mv = new ModelAndView("index");
        List<ChatMessage> history = getHistory(session);
        mv.addObject("history", history);
        return mv;
    }

    @PostMapping("/chat")
    public ModelAndView chat(@RequestParam("question") String question,
                             HttpSession session) {
        String answer = chatService.answer(question);

        List<ChatMessage> history = getHistory(session);
        history.add(new ChatMessage("user", question));
        history.add(new ChatMessage("assistant", answer));
        session.setAttribute("history", history);

        ModelAndView mv = new ModelAndView("index");
        mv.addObject("history", history);
        return mv;
    }

    // ===== FIXED: Upload File with RedirectAttributes
    @PostMapping("/upload-file")
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws Exception {
        UploadedContent saved = fileService.saveFile(file);

        ModelAndView mv = new ModelAndView("redirect:/upload");
        redirectAttributes.addAttribute("message",
                "✅ File uploaded successfully! Data updated to database: " + saved.getFilename());
        return mv;
    }

    // ===== FIXED: Upload URL with RedirectAttributes
    @PostMapping("/upload-url")
    public ModelAndView uploadUrl(@RequestParam("url") String url,
                                  RedirectAttributes redirectAttributes) throws Exception {
        UploadedContent saved = fileService.saveUrl(url);

        ModelAndView mv = new ModelAndView("redirect:/upload");
        redirectAttributes.addAttribute("message",
                "✅ Website imported successfully! Data updated to database: " + saved.getSourceUrl());
        return mv;
    }

    @PostMapping("/delete-source")
    public ModelAndView deleteSource(@RequestParam("id") Long id,
                                     RedirectAttributes redirectAttributes) {
        fileService.deleteSource(id);
        ModelAndView mv = new ModelAndView("redirect:/upload");
        redirectAttributes.addAttribute("message", "✅ Source deleted from database!");
        return mv;
    }

    @PostMapping("/crawl-url")
    public ModelAndView crawlUrl(@RequestParam("url") String url,
                                 RedirectAttributes redirectAttributes) throws Exception {
        fileService.crawlAndStore(url, 5);
        ModelAndView mv = new ModelAndView("redirect:/upload");
        redirectAttributes.addAttribute("message",
                "✅ Crawled up to 5 levels from: " + url + " | Data updated to database!");
        return mv;
    }

    @GetMapping("/upload")
    public String upload(Model model,
                         @RequestParam(name = "message", required = false) String message) {

        List<UploadedContent> files = fileService.all();

        files.forEach(f -> {
            System.out.println("ID=" + f.getId());
            System.out.println("TYPE=" + f.getSourceType());
            System.out.println("FILE=" + f.getFilename());
            System.out.println("----------------");
        });

        model.addAttribute("files", files);

        // ✅ Add message if it exists from redirect
        if (message != null) {
            model.addAttribute("message", message);
        }

        return "upload";
    }

    // ===== HELPER
    private List<ChatMessage> getHistory(HttpSession session) {
        List<ChatMessage> history = (List<ChatMessage>) session.getAttribute("history");
        if (history == null) {
            history = new ArrayList<>();
        }
        return history;
    }
}