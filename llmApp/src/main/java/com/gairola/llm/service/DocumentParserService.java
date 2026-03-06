package com.gairola.llm.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class DocumentParserService {

    /*
    ==========================
    MAIN FILE PARSER
    ==========================
     */

    public String parseFile(MultipartFile file) throws Exception {

        String name = file.getOriginalFilename();

        if (name == null) {
            throw new RuntimeException("File name is null");
        }

        name = name.toLowerCase();

        if (name.endsWith(".pdf")) {
            return parsePDF(file);
        }

        if (name.endsWith(".docx")) {
            return parseDOCX(file);
        }

        if (name.endsWith(".txt")) {
            return new String(file.getBytes());
        }

        throw new RuntimeException("Unsupported file type");
    }

    /*
    ==========================
    PDF PARSER
    ==========================
     */

    private String parsePDF(MultipartFile file) throws Exception {

        PDDocument document = PDDocument.load(file.getInputStream());

        PDFTextStripper stripper = new PDFTextStripper();

        String text = stripper.getText(document);

        document.close();

        return text;
    }

    /*
    ==========================
    DOCX PARSER
    ==========================
     */

    private String parseDOCX(MultipartFile file) throws Exception {

        InputStream is = file.getInputStream();

        XWPFDocument doc = new XWPFDocument(is);

        List<XWPFParagraph> paragraphs = doc.getParagraphs();

        StringBuilder text = new StringBuilder();

        for (XWPFParagraph p : paragraphs) {
            text.append(p.getText()).append("\n");
        }

        doc.close();

        return text.toString();
    }

    /*
    ==========================
    WEBSITE PARSER
    ==========================
     */

    public String parseWebsite(String url) throws Exception {

        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(10000)
                .get()
                .body()
                .text();
    }
}