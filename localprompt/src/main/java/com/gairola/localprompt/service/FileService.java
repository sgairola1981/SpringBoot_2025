package com.gairola.localprompt.service;

import com.gairola.localprompt.entity.UploadedContent;
import com.gairola.localprompt.repository.UploadedContentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class FileService {

    private final UploadedContentRepository repo;
    private static final int MAX_PAGES_TOTAL = 200; // global safety limit

    public UploadedContent saveFile(MultipartFile file) throws Exception {
        UploadedContent item = new UploadedContent();
        item.setId(nextId());
        item.setSourceType("FILE");
        item.setFilename(file.getOriginalFilename());
        item.setFileType(file.getContentType());
        item.setFileSize(file.getSize());
        item.setContent(extractText(file));
        item.setFileData(file.getBytes());
        item.setUploadedAt(LocalDateTime.now());
        return repo.save(item);
    }

    public UploadedContent saveUrl(String url) throws Exception {
        String text = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(10_000)
                .get()
                .text();

        UploadedContent item = new UploadedContent();
        item.setId(nextId());
        item.setSourceType("URL");
        item.setFilename(url);
        item.setSourceUrl(url);
        item.setFileType("text/html");
        item.setFileSize((long) text.length());
        item.setContent(text);
        item.setUploadedAt(LocalDateTime.now());
        return repo.save(item);
    }

    public List<UploadedContent> all() {
        return repo.findAll();
    }

    private Long nextId() {
        return repo.findAll().stream()
                .map(UploadedContent::getId)
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L) + 1;
    }

    private String extractText(MultipartFile file) throws Exception {
        String name = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
        String ct = file.getContentType() == null ? "" : file.getContentType().toLowerCase();

        if (ct.contains("pdf") || name.endsWith(".pdf")) {
            try (InputStream in = file.getInputStream(); PDDocument doc = PDDocument.load(in)) {
                return new PDFTextStripper().getText(doc);
            }
        }

        if (name.endsWith(".docx")) {
            try (InputStream in = file.getInputStream(); XWPFDocument docx = new XWPFDocument(in)) {
                return new XWPFWordExtractor(docx).getText();
            }
        }

        if (name.endsWith(".doc")) {
            try (InputStream in = file.getInputStream(); HWPFDocument doc = new HWPFDocument(in)) {
                return new WordExtractor(doc).getText();
            }
        }

        return new String(file.getBytes(), StandardCharsets.UTF_8);
    }

    // ==== URL crawler up to maxDepth (e.g. 5) ====

    public void crawlAndStore(String rootUrl, int maxDepth) throws Exception {
        Set<String> visited = new HashSet<>();
        AtomicInteger pageCounter = new AtomicInteger(0);

        URI rootUri = URI.create(rootUrl);
        String rootHost = rootUri.getHost();

        crawlInternal(rootUrl, 0, maxDepth, visited, rootHost, pageCounter);
    }

    private void crawlInternal(String url,
                               int depth,
                               int maxDepth,
                               Set<String> visited,
                               String rootHost,
                               AtomicInteger pageCounter) throws Exception {

        if (depth > maxDepth) {
            return;
        }
        if (pageCounter.get() >= MAX_PAGES_TOTAL) {
            return;
        }
        if (visited.contains(url)) {
            return;
        }
        visited.add(url);

        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(10_000)
                .get();

        String text = doc.text();

        UploadedContent item = new UploadedContent();
        item.setId(nextId());
        item.setSourceType("URL");
        item.setFilename(url);
        item.setSourceUrl(url);
        item.setFileType("text/html");
        item.setFileSize((long) text.length());
        item.setContent(text);
        item.setUploadedAt(LocalDateTime.now());
        repo.save(item);
        pageCounter.incrementAndGet();

        if (depth == maxDepth) {
            return;
        }

        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String childUrl = link.attr("abs:href");
            if (childUrl == null || childUrl.isBlank()) {
                continue;
            }

            // same-domain filter
            try {
                URI childUri = URI.create(childUrl);
                String childHost = childUri.getHost();
                if (childHost == null || !childHost.equalsIgnoreCase(rootHost)) {
                    continue;
                }
            } catch (IllegalArgumentException e) {
                continue;
            }

            try {
                crawlInternal(childUrl, depth + 1, maxDepth, visited, rootHost, pageCounter);
            } catch (Exception e) {
                // ignore bad links and continue
            }
        }
    }
}