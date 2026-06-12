package com.gairola.localprompt.repository;



import com.gairola.localprompt.entity.UploadedContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedContentRepository extends JpaRepository<UploadedContent, Long> {
}