package com.gairola.localprompt.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "uploaded_content")
public class UploadedContent {

    @Id
    private Long id;

    @Column(name = "source_type")
    private String sourceType;

    private String filename;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Lob
    @Column(name = "content")
    private String content;

    @Lob
    @Column(name = "file_data")
    private byte[] fileData;

    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;
}