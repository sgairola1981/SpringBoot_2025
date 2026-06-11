package com.gairola.ollama.repository;

import com.gairola.ollama.entity.KnowledgeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KnowledgeRepository extends JpaRepository<KnowledgeItem, Long> {

    @Query("""
        SELECT k
        FROM KnowledgeItem k
        WHERE LOWER(k.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR k.content LIKE CONCAT('%', :keyword, '%')
        ORDER BY k.id DESC
    """)
    List<KnowledgeItem> search(@Param("keyword") String keyword);
}