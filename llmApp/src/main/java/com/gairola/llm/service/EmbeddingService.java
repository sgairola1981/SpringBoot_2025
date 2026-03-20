package com.gairola.llm.service;

import com.gairola.llm.entity.DocumentChunk;
import com.gairola.llm.repository.DocumentRepository;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.data.embedding.Embedding;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;
    private final DocumentRepository repository;

    public EmbeddingService(EmbeddingModel embeddingModel,
                            DocumentRepository repository) {
        this.embeddingModel = embeddingModel;
        this.repository = repository;
    }

    // Create embedding for query
    public float[] createEmbedding(String text) {

        Embedding embedding = embeddingModel.embed(text).content();

        return embedding.vector();
    }

    // Store document with chunking
    public void storeDocument(String text) {

        List<String> chunks = splitIntoChunks(text, 300);

        int index = 0;

        for (String chunk : chunks) {

            Embedding embedding = embeddingModel.embed(chunk).content();

            DocumentChunk entity = new DocumentChunk();

            entity.setContent(chunk);
            entity.setChunkIndex(index++);
            entity.setEmbedding(Arrays.toString(embedding.vector()));

            repository.save(entity);
        }
    }

    // Better chunk splitting
    private List<String> splitIntoChunks(String text, int size) {

        return java.util.stream.IntStream
                .iterate(0, i -> i < text.length(), i -> i + size)
                .mapToObj(i -> text.substring(i, Math.min(text.length(), i + size)))
                .toList();
    }
}