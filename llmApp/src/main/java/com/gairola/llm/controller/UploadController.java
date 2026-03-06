package com.gairola.llm.controller;

import com.gairola.llm.service.DocumentParserService;
import com.gairola.llm.service.DocxReaderService;
import com.gairola.llm.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UploadController {

    private final DocumentParserService parser;
    private final EmbeddingService embeddingService;
    private final DocxReaderService docxReaderService ;


    @PostMapping("/upload")
    @ResponseBody
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {

        String text = "";

        if(file.getOriginalFilename().endsWith(".docx")){

            text = docxReaderService.readDocx(file.getInputStream());

        }

        embeddingService.storeDocument(text);

        return "Uploaded Successfully";
    }

    @PostMapping("/upload-url")
    public String uploadWebsite(@RequestParam String url) throws Exception {

        String text = parser.parseWebsite(url);

        embeddingService.storeDocument(text);

        return "Website content stored!";
    }
}