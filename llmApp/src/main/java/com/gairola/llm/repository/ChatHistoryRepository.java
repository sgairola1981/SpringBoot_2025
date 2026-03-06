package com.gairola.llm.repository;

import com.gairola.llm.entity.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatHistoryRepository
        extends JpaRepository<ChatHistory,Long> {
}