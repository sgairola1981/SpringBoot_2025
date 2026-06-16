package com.gairola.localprompt.repository;

import com.gairola.localprompt.entity.UploadedContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import jakarta.persistence.QueryHint;

import java.util.List;

public interface UploadedContentRepository extends JpaRepository<UploadedContent, Long> {

    // ✅ FIXED: Use DBMS_LOB.INSTR() for CLOB search in Oracle 11g
    @Query(value = """
        SELECT * FROM uploaded_content 
        WHERE DBMS_LOB.INSTR(content, :query, 1, 1) > 0
           OR filename LIKE '%' || :query || '%'
        ORDER BY uploaded_at DESC
        """, nativeQuery = true)
    @QueryHints({
            @QueryHint(name = "org.hibernate.readOnly", value = "true")
    })
    List<UploadedContent> findFirstMatching(String query, int limit);
}