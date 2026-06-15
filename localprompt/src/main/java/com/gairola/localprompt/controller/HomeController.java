package com.gairola.localprompt.controller;

import com.gairola.localprompt.dto.ChatForm;
import com.gairola.localprompt.entity.UploadedContent;
import com.gairola.localprompt.service.ChatService;
import com.gairola.localprompt.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final FileService fileService;
    private final ChatService chatService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("files", fileService.all());
        return "index";
    }

    @PostMapping("/upload-file")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) throws Exception {
        UploadedContent saved = fileService.saveFile(file);
        model.addAttribute("message", "Uploaded: " + saved.getFilename());
        model.addAttribute("files", fileService.all());
        return "index";
    }

    @PostMapping("/upload-url")
    public String uploadUrl(@RequestParam("url") String url, Model model) throws Exception {
        UploadedContent saved = fileService.saveUrl(url);
        model.addAttribute("message", "Stored URL: " + saved.getSourceUrl());
        model.addAttribute("files", fileService.all());
        return "index";
    }
    @PostMapping("/chat")
    public org.springframework.web.servlet.ModelAndView chat(
            @RequestParam("question") String question) {

        System.out.println("QUESTION=" + question);
        String answer = chatService.answer(question);
        System.out.println("ANSWER_TO_VIEW = [" + answer + "]");

        org.springframework.web.servlet.ModelAndView mv =
                new org.springframework.web.servlet.ModelAndView("index");
        mv.addObject("answer", answer);
        mv.addObject("files", fileService.all());
        return mv;
    }
}