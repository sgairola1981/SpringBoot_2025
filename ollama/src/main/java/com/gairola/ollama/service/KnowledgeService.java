package com.gairola.ollama.service;

import com.gairola.ollama.entity.KnowledgeItem;
import com.gairola.ollama.repository.KnowledgeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeService implements CommandLineRunner {

    private final KnowledgeRepository knowledgeRepository;

    public KnowledgeService(KnowledgeRepository knowledgeRepository) {
        this.knowledgeRepository = knowledgeRepository;
    }

    @Override
    public void run(String... args) {
        if (knowledgeRepository.count() == 0) {
            knowledgeRepository.save(new KnowledgeItem(
                    "Spring Boot basics",
                    "Spring Boot helps you create stand-alone, production-grade Spring applications quickly."
            ));

            knowledgeRepository.save(new KnowledgeItem(
                    "Ollama integration",
                    "This application uses Spring AI with Ollama and the mistral:latest model for local chat responses."
            ));

            knowledgeRepository.save(new KnowledgeItem(
                    "Oracle database",
                    "Knowledge items are stored in Oracle XE using Spring Data JPA and Hibernate."
            ));

            knowledgeRepository.save(new KnowledgeItem(
                    "Thymeleaf UI",
                    "The user interface is built with Thymeleaf and submits prompts to a Spring Boot controller."
            ));
        }
    }

    public List<KnowledgeItem> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        List<KnowledgeItem> results = knowledgeRepository.search(keyword.trim());
        return results.stream().limit(5).toList();
    }

    public KnowledgeItem add(String title, String content) {
        return knowledgeRepository.save(new KnowledgeItem(title, content));
    }
}