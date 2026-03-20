package com.gairola.llm.service;

import com.gairola.llm.entity.DocumentChunk;
import com.gairola.llm.repository.DocumentRepository;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VectorSearchService {

    private final EmbeddingModel embeddingModel;
    private final DocumentRepository repository;

    public String search(String question) {

        System.out.println("🔍 Question: " + question);

        Embedding queryEmbedding = embeddingModel.embed(question).content();
        List<DocumentChunk> chunks = repository.findAll();

        System.out.println("📦 Total chunks in DB: " + chunks.size());

        // Score chunks
        List<ScoredChunk> scored = chunks.stream().limit(5)
                .map(chunk -> new ScoredChunk(
                        chunk.getContent(),
                        cosineSimilarity(
                                queryEmbedding.vector(),
                                parseEmbedding(chunk.getEmbedding())
                        )
                ))
                .sorted(Comparator.comparingDouble(ScoredChunk::score).reversed())
                .collect(Collectors.toList());

        if (scored.isEmpty()) return "";

        double bestScore = scored.get(0).score();
        System.out.println("🏆 Best Score: " + bestScore);

        // ✅ Similarity threshold
        if (bestScore < 0.35) {
            System.out.println("❌ Low similarity");
            return "";
        }

        // ✅ Top-K context (better RAG)
        String context = scored.stream()
                .limit(3)
                .map(ScoredChunk::content)
                .collect(Collectors.joining("\n\n"));

        return context;
    }

    private record ScoredChunk(String content, double score) {}

    private float[] parseEmbedding(String s) {
        String clean = s.replace("[", "").replace("]", "");
        String[] parts = clean.split(",");
        float[] v = new float[parts.length];
        for (int i = 0; i < parts.length; i++) {
            v[i] = Float.parseFloat(parts[i].trim());
        }
        return v;
    }

    private double cosineSimilarity(float[] a, float[] b) {
        int len = Math.min(a.length, b.length); // ✅ safe
        double dot = 0, normA = 0, normB = 0;

        for (int i = 0; i < len; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }

        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}