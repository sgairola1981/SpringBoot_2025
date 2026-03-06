package com.gairola.llm.controller;

import com.gairola.llm.service.PdfService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfService pdfService){
        this.pdfService = pdfService;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws Exception{

        return pdfService.extractText(file);

    }

}

