package com.gairola.llm.service;

import com.gairola.llm.entity.DocumentChunk;
import com.gairola.llm.repository.DocumentRepository;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.data.embedding.Embedding;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;
      private final DocumentRepository repository;

    public EmbeddingService(EmbeddingModel embeddingModel, DocumentRepository repository) {
        this.embeddingModel = embeddingModel;
        this.repository = repository;
    }

    public float[] createEmbedding(String text) {

        Embedding embedding = embeddingModel.embed(text).content();

        return embedding.vector();
    }
    public void storeDocument(String text) {

        // split document into chunks
        String[] chunks = text.split("(?<=\\G.{500})");

        for (String chunk : chunks) {

            Embedding embedding = embeddingModel.embed(chunk).content();

            DocumentChunk entity = new DocumentChunk();

            entity.setContent(chunk);
            entity.setEmbedding(Arrays.toString(embedding.vector()));

            repository.save(entity);
        }
    }
}