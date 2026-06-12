package com.gairola.olamarag.service;

import com.gairola.olamarag.entity.KnowledgeItem;
import com.gairola.olamarag.repository.KnowledgeRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class KnowledgeService implements CommandLineRunner {

    private final KnowledgeRepository knowledgeRepository;
    private final VectorStore vectorStore;

    public KnowledgeService(KnowledgeRepository knowledgeRepository, VectorStore vectorStore) {
        this.knowledgeRepository = knowledgeRepository;
        this.vectorStore = vectorStore;
    }

    @Override
    public void run(String... args) {
        if (knowledgeRepository.count() == 0) {
            saveKnowledge("About India",
                    "India is a country in South Asia. Its capital is New Delhi. It has 28 states and 8 union territories.");
            saveKnowledge("About Spring Boot",
                    "Spring Boot helps developers create stand-alone, production-ready Spring applications quickly.");
            saveKnowledge("About Oracle Database",
                    "Oracle Database is a relational database management system used in enterprise applications.");
        } else {
            reindexAll();
        }
    }

    public KnowledgeItem saveKnowledge(String title, String content) {
        KnowledgeItem item = knowledgeRepository.save(new KnowledgeItem(title, content));
        indexToVectorStore(item);
        return item;
    }

    public void reindexAll() {
        List<KnowledgeItem> items = knowledgeRepository.findAll();
        for (KnowledgeItem item : items) {
            indexToVectorStore(item);
        }
    }

    private void indexToVectorStore(KnowledgeItem item) {
        Document document = Document.builder()
                .id(String.valueOf(item.getId()))
                .text(item.getTitle() + "\n" + item.getContent())
                .metadata(Map.of(
                        "knowledgeId", item.getId(),
                        "title", item.getTitle()
                ))
                .build();

        vectorStore.add(List.of(document));
    }
}