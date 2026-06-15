package com.gairola.localprompt.service;

import com.gairola.localprompt.dto.AdminStats;
import com.gairola.localprompt.repository.UploadedContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UploadedContentRepository repo;

    public AdminStats getStats() {
        var all = repo.findAll();
        long totalFiles = all.stream().filter(f -> "FILE".equals(f.getSourceType())).count();
        long totalUrls  = all.stream().filter(f -> "URL".equals(f.getSourceType())).count();

        String lastUpload = all.stream()
                .max((a, b) -> a.getUploadedAt().compareTo(b.getUploadedAt()))
                .map(f -> f.getUploadedAt().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")))
                .orElse("—");

        return new AdminStats(totalFiles, totalUrls, all.size(), 0, lastUpload, "Active");
    }

    public void deleteSource(Long id) {
        repo.deleteById(id);
    }

    public void clearAll() {
        repo.deleteAll();
    }
}