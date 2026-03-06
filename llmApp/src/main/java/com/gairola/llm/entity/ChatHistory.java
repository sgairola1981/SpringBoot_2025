package com.gairola.llm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="CHAT_HISTORY")
public class ChatHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userMessage;

    private String aiResponse;

    private LocalDateTime createdAt = LocalDateTime.now();

}