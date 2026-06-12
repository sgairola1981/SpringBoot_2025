package com.gairola.olamarag.repository;

import com.gairola.olamarag.entity.KnowledgeItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KnowledgeRepository extends JpaRepository<KnowledgeItem, Long> {
}