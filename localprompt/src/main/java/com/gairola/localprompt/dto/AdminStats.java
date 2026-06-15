package com.gairola.localprompt.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AdminStats {
    private final long totalFiles;
    private final long totalUrls;
    private final long totalSources;
    private final long totalConversations;
    private final String lastUpload;
    private final String status;
}