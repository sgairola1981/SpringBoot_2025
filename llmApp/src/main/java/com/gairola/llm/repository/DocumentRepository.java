package com.gairola.llm.repository;

import com.gairola.llm.entity.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository
        extends JpaRepository<DocumentChunk,Long> {

    @Query(value = """
            SELECT * 
            FROM DOCUMENT_CHUNKS 
            WHERE DBMS_LOB.INSTR(CONTENT, :text) > 0
            """, nativeQuery = true)
    List<DocumentChunk> searchContent(@Param("text") String text);

    @Query(value = "SELECT * FROM document_chunks WHERE ROWNUM <= 100", nativeQuery = true)
    List<DocumentChunk> findTop100();
}


